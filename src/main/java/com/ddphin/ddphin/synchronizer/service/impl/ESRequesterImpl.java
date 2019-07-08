package com.ddphin.ddphin.synchronizer.service.impl;

import com.ddphin.ddphin.synchronizer.service.ESRequester;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * ClassName: ESRequesterImpl
 * Function:  ESRequesterImpl
 * Date:      2019/6/22 上午10:46
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Service
public class ESRequesterImpl implements ESRequester {
    @Autowired
    private BulkProcessor esBulkProcessor;

    @Override
    public void bulkRequest(String requestBody) throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(requestBody.getBytes(), 0, requestBody.getBytes().length, XContentType.JSON);
        request.requests().forEach(o -> esBulkProcessor.add(o));
    }
}
