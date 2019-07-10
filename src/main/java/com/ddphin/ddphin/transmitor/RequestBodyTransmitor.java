package com.ddphin.ddphin.transmitor;

import java.io.IOException;

/**
 * ClassName: transmitor
 * Function:  TODO
 * Date:      2019/7/6 下午6:10
 * Author     DaintyDolphin
 * Version    V1.0
 */
public interface RequestBodyTransmitor {
    void transmit(String requestBody) throws IOException;
}
