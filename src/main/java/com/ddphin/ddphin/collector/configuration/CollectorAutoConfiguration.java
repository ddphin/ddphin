package com.ddphin.ddphin.collector.configuration;
/**
 * ClassName: CollectorAutoConfiguration
 * Function:  CollectorAutoConfiguration
 * Date:      2019/7/2 下午8:39
 * Author     DaintyDolphin
 * Version    V1.0
 */

import com.ddphin.ddphin.collector.interceptor.CollectorInterceptor;
import com.ddphin.ddphin.collector.mybatis.SQLExecutor;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Properties;

@Configuration
@AutoConfigureAfter(MybatisAutoConfiguration.class)
public class CollectorAutoConfiguration {
    @Autowired
    private List<SqlSessionFactory> sqlSessionFactoryList;

    @Bean
    @ConfigurationProperties(prefix=CollectorProperties.COLLECTOR_PREFIX)
    public CollectorProperties properties() {
        return new CollectorProperties();
    }

    @Bean
    public SQLExecutor sqlExecutor(SqlSession sqlSession) {
        return new SQLExecutor(sqlSession);
    }

    @PostConstruct
    public void addCollector() {
        CollectorProperties collectorProperties = this.properties();
        if (collectorProperties.validate()) {
            CollectorInterceptor interceptor = new CollectorInterceptor();
            Properties properties = new Properties();
            properties.put(CollectorProperties.COLLECTOR_PREFIX, collectorProperties);
            interceptor.setProperties(properties);
            for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
                sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
            }
        }
    }
}