package com.ddphin.ddphin.transmitor.impl;

import com.ddphin.ddphin.common.util.ServiceLocale;
import com.ddphin.ddphin.synchronizer.service.ESRequester;
import com.ddphin.ddphin.transmitor.BulkRequestBodyTransmitor;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * ClassName: DefaultBulkRequestBodyTransmitor
 * Function:  DefaultBulkRequestBodyTransmitor
 * Date:      2019/7/6 下午6:14
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Service
public class DefaultBulkRequestBodyTransmitor implements BulkRequestBodyTransmitor {
    @Override
    public void transmit(String bulkRequestBody) throws IOException {
        ESRequester esRequester = (ESRequester) ServiceLocale.findService(ESRequester.class);
        esRequester.bulkRequest(bulkRequestBody);
    }
}
