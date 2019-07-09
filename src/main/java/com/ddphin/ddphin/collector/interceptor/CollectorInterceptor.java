package com.ddphin.ddphin.collector.interceptor;

/**
 * ClassName: CollectorInterceptor
 * Function:  CollectorInterceptor
 * Date:      2019/7/2 下午8:39
 * Author     DaintyDolphin
 * Version    V1.0
 */

import com.ddphin.ddphin.collector.collector.Collector;
import com.ddphin.ddphin.collector.entity.ESSyncProperties;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class,method = "update",args = {MappedStatement.class, Object.class})
})
public class CollectorInterceptor implements Interceptor {
    private Map<SqlCommandType, ESSyncProperties.Point> point;
    private Map<String, String> input;
    private Collector collector;

    public CollectorInterceptor(ESSyncProperties properties, Collector collector) {
        this.point =properties.getPoint();
        this.input = properties.getInput();
        this.collector = collector;
    }

    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Executor executor = (Executor) invocation.getTarget();

        String inputKey = ms.getId().substring(0, ms.getId().lastIndexOf("."));
        ESSyncProperties.Point point = this.point.get(ms.getSqlCommandType());
        if (ESSyncProperties.Point.before.equals(point)) {
            this.process(args[1], this.input.get(inputKey), executor, ms);
        }
        Object o = invocation.proceed();

        if (ESSyncProperties.Point.after.equals(point)) {
            this.process(args[1], this.input.get(inputKey), executor, ms);
        }
        return o;
    }

    private void process(Object object, String ouput, Executor executor, MappedStatement ms) throws SQLException {
        if (object instanceof DefaultSqlSession.StrictMap) {
            for (Object o : (Collection) ((DefaultSqlSession.StrictMap) object).get("collection")) {
                collector.collect(o, ouput, executor, ms);
            }
        } else {
            collector.collect(object, ouput, executor, ms);
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {}
}
