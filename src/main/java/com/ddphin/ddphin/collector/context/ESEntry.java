package com.ddphin.ddphin.collector.context;

import lombok.Data;
import org.apache.ibatis.mapping.SqlCommandType;

import java.util.HashMap;

/**
 * ClassName: ESNestedEntry
 * Function:  ESNestedEntry
 * Date:      2019/7/2 下午12:12
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESEntry extends HashMap<String, Object> {
    private SqlCommandType operation;
    private Object key;
    private Object withOfBelongs;
    private Object withOfOnMissing;
    private Object withOfReload;
    public ESEntry(SqlCommandType op) {
        super();
        this.operation = op;
    }
}
