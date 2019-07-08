package com.ddphin.ddphin.collector.collector;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;

import java.sql.SQLException;

/**
 * ClassName: Collector
 * Function:  Collector
 * Date:      2019/7/3 下午4:19
 * Author     DaintyDolphin
 * Version    V1.0
 */
public interface Collector {
    void collect(Object object, String output, Executor executor, MappedStatement ms) throws SQLException;
}
