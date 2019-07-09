package com.ddphin.ddphin.synchronizer.requester.impl;

import com.ddphin.ddphin.synchronizer.requester.ESRequester;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;

/**
 * ClassName: DefaultESRequester
 * Function:  DefaultESRequester
 * Date:      2019/6/22 上午10:46
 * Author     DaintyDolphin
 * Version    V1.0
 */
public class DefaultESRequester implements ESRequester {
    private BulkProcessor esBulkProcessor;

    public DefaultESRequester(BulkProcessor esBulkProcessor) {
        this.esBulkProcessor = esBulkProcessor;
    }
    @Override
    public void bulkRequest(String requestBody) throws IOException {
        BulkRequest request = new BulkRequest();
        request.add(requestBody.getBytes(), 0, requestBody.getBytes().length, XContentType.JSON);
        request.requests().forEach(o -> esBulkProcessor.add(o));
    }
}
