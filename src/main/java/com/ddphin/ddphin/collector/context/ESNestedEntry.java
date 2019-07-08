package com.ddphin.ddphin.collector.context;

import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: ESNestedEntry
 * Function:  ESNestedEntry
 * Date:      2019/7/2 下午12:12
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESNestedEntry extends  HashMap<String, Object>{
    private transient SqlCommandType __operation;
    private transient Map<String, SqlCommandType> __remove_operation;
    private transient String __name;
    public ESNestedEntry(String name, SqlCommandType op) {
        this.__operation = op;
        this.__name = name;
        this.__remove_operation = new HashMap<>();
    }
}
