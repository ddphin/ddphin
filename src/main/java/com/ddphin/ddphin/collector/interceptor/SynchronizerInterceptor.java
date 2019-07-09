package com.ddphin.ddphin.collector.interceptor;

import com.ddphin.ddphin.transmitor.BulkRequestBodyTransmitor;
import com.ddphin.ddphin.collector.context.ContextHolder;
import com.ddphin.ddphin.collector.requestbody.RequestBodyBuilder;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ClassName: SynchronizerInterceptor
 * Function:  SynchronizerInterceptor
 * Date:      2019/7/4 上午11:25
 * Author     DaintyDolphin
 * Version    V1.0
 */

public class SynchronizerInterceptor implements HandlerInterceptor {
    private RequestBodyBuilder requestBodyBuilder;
    private BulkRequestBodyTransmitor bulkRequestBodyTransmitor;

    public SynchronizerInterceptor(RequestBodyBuilder requestBodyBuilder, BulkRequestBodyTransmitor bulkRequestBodyTransmitor) {
        this.bulkRequestBodyTransmitor = bulkRequestBodyTransmitor;
        this.requestBodyBuilder = requestBodyBuilder;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ContextHolder.remove();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws IOException {
        if (null == ex) {
            String body = requestBodyBuilder.buildBulkRequestBody();
            bulkRequestBodyTransmitor.transmit(body);
        }
        ContextHolder.remove();
    }
}
