package com.ddphin.ddphin.collector.entity;

import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.List;
import java.util.Map;

/**
 * Configuration esSyncProperties for PageHelper.
 *
 * @author liuzh
 */
@Data
public class ESSyncProperties {
    public static final String COLLECTOR_PREFIX = "elasticsearch.sync";
    private Map<SqlCommandType, Point> point;
    private Map<String, String> input;

    private Map<String, ESSyncItemOutputItem> output;
    private List<String> api;

    public enum Point { before, after}

    public Boolean validate() {
        return true;
    }
}
