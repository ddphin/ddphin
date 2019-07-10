package com.ddphin.ddphin.interceptor;

import com.ddphin.ddphin.transmitor.RequestBodyTransmitor;
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
    private RequestBodyTransmitor requestBodyTransmitor;

    public SynchronizerInterceptor(RequestBodyBuilder requestBodyBuilder, RequestBodyTransmitor bulkRequestBodyTransmitor) {
        this.requestBodyTransmitor = bulkRequestBodyTransmitor;
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
            String body = requestBodyBuilder.build();
            requestBodyTransmitor.transmit(body);
        }
        ContextHolder.remove();
    }
}
