package com.ddphin.ddphin.synchronizer.service;

import java.io.IOException;

/**
 * ClassName: ESRequester
 * Function:  ESRequester
 * Date:      2019/6/22 上午10:39
 * Author     DaintyDolphin
 * Version    V1.0
 */
public interface ESRequester {
    void bulkRequest(String requesBody) throws IOException;
}
