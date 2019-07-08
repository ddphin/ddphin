package com.ddphin.ddphin.collector.context;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: CollectorContext
 * Function:  CollectorContext
 * Date:      2019/7/3 下午5:44
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class CollectorContext {
    private transient Map<String, Map<Object, ESEntry>> association = new HashMap<>();
    private transient Map<String, Map<Object, Object>> data = new HashMap<>();
    private ESNestedCollection value = new ESNestedCollection();
}
