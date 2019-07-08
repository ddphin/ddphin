package com.ddphin.ddphin.common.configuration;

import com.ddphin.ddphin.synchronizer.listener.EBulkProcessorListener;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ClassName: ESConfiguration
 * Function:  ESConfiguration
 * Date:      2019/6/21 下午2:30
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Configuration
public class ESConfiguration {
    @Bean
    @ConfigurationProperties(prefix="elasticsearch.repo")
    public ESProperties esproperties() {
        return new ESProperties();
    }


    @Bean
    public RestHighLevelClient esclient(ESProperties esproperties) {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(
                                esproperties.getHost(), esproperties.getPort(), esproperties.getScheme())));
        return client;
    }

    @Bean
    public BulkProcessor esBulkProcessor(RestHighLevelClient esclient) {
        return BulkProcessor.builder(
                (request, bulkListener) -> esclient.bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                new EBulkProcessorListener())
                .setBulkActions(1000)
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                .setConcurrentRequests(1)
                .setBackoffPolicy(BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();
    }
}
