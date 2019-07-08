package com.ddphin.ddphin.collector.interceptor;

/**
 * ClassName: CollectorInterceptor
 * Function:  CollectorInterceptor
 * Date:      2019/7/2 下午8:39
 * Author     DaintyDolphin
 * Version    V1.0
 */

import com.ddphin.ddphin.collector.collector.Collector;
import com.ddphin.ddphin.collector.configuration.CollectorProperties;
import com.ddphin.ddphin.common.util.ServiceLocale;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.defaults.DefaultSqlSession;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class,method = "update",args = {MappedStatement.class, Object.class})
})
public class CollectorInterceptor implements Interceptor {
    private CollectorProperties properties = new CollectorProperties();

    public Object intercept(Invocation invocation) throws Throwable {
        String output = null;
        String point = null;
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Executor executor = (Executor) invocation.getTarget();
        if (null != properties && null != properties.getInput() && null != properties.getOutput() && 1 < args.length) {
            String inputKey = ms.getId().substring(0, ms.getId().lastIndexOf("."));
            output = this.properties.getInput().get(inputKey);
            point = this.properties.getPoint().get(ms.getSqlCommandType().name());
        }
        if (null != output && "BEFORE".equals(point)) {
            this.process(args[1], output, executor, ms);
        }
        Object o = invocation.proceed();

        if (null != output && "AFTER".equals(point)) {
            this.process(args[1], output, executor, ms);
        }
        return o;
    }

    private void process(Object object, String ouput, Executor executor, MappedStatement ms) throws SQLException {
        if (object instanceof DefaultSqlSession.StrictMap) {
            for (Object o : (Collection)((DefaultSqlSession.StrictMap) object).get("collection")) {
                this.getCollector().collect(o, ouput, executor, ms);
            }
        }
        else {
            this.getCollector().collect(object, ouput, executor, ms);
        }
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
        if (null != properties && !properties.isEmpty()) {
            this.properties = (CollectorProperties) properties.values().iterator().next();
            this.properties.getInput().forEach((key, value) -> this.properties.getInputR().put(value, key));
        }
    }

    private Collector getCollector() {
        return (Collector) ServiceLocale.findService(Collector.class);
    }
}
