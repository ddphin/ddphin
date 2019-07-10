package com.ddphin.ddphin.collector.requestbody;

import com.ddphin.ddphin.collector.entity.ESSyncItemOutputItem;

import java.util.Map;

/**
 * ClassName: RequestBodyBuilder
 * Function:  RequestBodyBuilder
 * Date:      2019/7/3 下午4:19
 * Author     DaintyDolphin
 * Version    V1.0
 */
public interface RequestBodyBuilder {
    String build();
    void setOutputMap(Map<String, ESSyncItemOutputItem> outputMap);
}
