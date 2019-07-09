package com.ddphin.ddphin.synchronizer.listener;

import com.ddphin.ddphin.synchronizer.bean.ESVersionBean;
import com.ddphin.ddphin.synchronizer.bean.ESVersionLogBean;
import com.ddphin.ddphin.synchronizer.service.ESVersionService;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: EBulkProcessorListener
 * Function:  EBulkProcessorListener
 * Date:      2019/7/6 下午4:42
 * Author     DaintyDolphin
 * Version    V1.0
 */
public class EBulkProcessorListener implements BulkProcessor.Listener {
    private ESVersionService esVersionService;

    public EBulkProcessorListener(ESVersionService esVersionService) {
        this.esVersionService = esVersionService;
    }

    @Override
    public void beforeBulk(long l, BulkRequest bulkRequest) {}

    @Override
    public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
        if (null != esVersionService) {
            int size = bulkRequest.requests().size();
            List<ESVersionLogBean> logList = new ArrayList<>(size);
            List<ESVersionBean> versionList = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                DocWriteRequest<?> request = bulkRequest.requests().get(i);
                BulkItemResponse response = bulkResponse.getItems()[i];

                ESVersionLogBean bean = new ESVersionLogBean();
                bean.setIndex(request.index());
                bean.setId(request.id());
                bean.setContext(request.toString());
                if (response.isFailed()) {
                    bean.setStatus(1);
                    bean.setMessage(response.getFailureMessage());
                }
                else {
                    bean.setStatus(0);
                    bean.setPrimaryTerm(response.getResponse().getPrimaryTerm());
                    bean.setSeqNo(response.getResponse().getSeqNo());
                    versionList.add(bean);
                }
                logList.add(bean);
            }
            this.syncLog(logList);
            this.syncVersion(versionList);
        }
    }

    @Override
    public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
        if (null != esVersionService) {
            int size = bulkRequest.requests().size();
            List<ESVersionLogBean> logList = new ArrayList<>(size);
            for (int i = 0; i < size; ++i) {
                DocWriteRequest<?> request = bulkRequest.requests().get(i);

                ESVersionLogBean bean = new ESVersionLogBean();
                bean.setIndex(request.index());
                bean.setId(request.id());
                bean.setContext(request.toString());
                bean.setStatus(-1);

                logList.add(bean);

            }
            this.syncLog(logList);
        }
    }

    private void syncLog(List<ESVersionLogBean> logList) {
        if (!CollectionUtils.isEmpty(logList)) {
            esVersionService.insertLogList(logList);
        }
    }

    private void syncVersion(List<ESVersionBean> list) {
        if (!CollectionUtils.isEmpty(list)) {
            esVersionService.replaceList(list);
        }
    }
}
