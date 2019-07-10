package com.ddphin.ddphin.transmitor.impl;

import com.ddphin.ddphin.synchronizer.requester.ESRequester;
import com.ddphin.ddphin.transmitor.RequestBodyTransmitor;

import java.io.IOException;

/**
 * ClassName: DefaultBulkRequestBodyTransmitor
 * Function:  DefaultBulkRequestBodyTransmitor
 * Date:      2019/7/6 下午6:14
 * Author     DaintyDolphin
 * Version    V1.0
 */

public class DefaultBulkRequestBodyTransmitor implements RequestBodyTransmitor {
    private ESRequester esRequester;

    public DefaultBulkRequestBodyTransmitor(ESRequester esRequester) {
        this.esRequester = esRequester;
    }
    @Override
    public void transmit(String requestBody) throws IOException {
        esRequester.bulkRequest(requestBody);
    }
}
