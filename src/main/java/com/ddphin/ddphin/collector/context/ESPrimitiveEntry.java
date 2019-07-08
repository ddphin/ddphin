package com.ddphin.ddphin.collector.context;

import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

/**
 * ClassName: ESNestedEntry
 * Function:  ESNestedEntry
 * Date:      2019/7/2 下午12:12
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESPrimitiveEntry {
    private transient SqlCommandType __operation;
    private transient String __name;
    private transient Object value;
    public ESPrimitiveEntry(String name, SqlCommandType op) {
        this.__operation = op;
        this.__name = name;
    }
}
