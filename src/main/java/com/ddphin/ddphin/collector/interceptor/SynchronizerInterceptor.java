package com.ddphin.ddphin.collector.interceptor;

import com.ddphin.ddphin.common.util.ServiceLocale;
import com.ddphin.ddphin.transmitor.BulkRequestBodyTransmitor;
import com.ddphin.ddphin.transmitor.CustomizedBulkRequestBodyTransmitor;
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
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        ContextHolder.remove();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws IOException {
        if (null == ex) {
            String body = this.getRequestBodyBuilder().buildBulkRequestBody();
            this.getBulkRequestBodyTransmitor().transmit(body);
        }
        ContextHolder.remove();
    }

    private BulkRequestBodyTransmitor getBulkRequestBodyTransmitor() {
        BulkRequestBodyTransmitor transmitor =
                (CustomizedBulkRequestBodyTransmitor) ServiceLocale.findService(CustomizedBulkRequestBodyTransmitor.class);
        if (null == transmitor) {
            transmitor = (BulkRequestBodyTransmitor) ServiceLocale.findService(BulkRequestBodyTransmitor.class);
        }
        return transmitor;
    }
    private RequestBodyBuilder getRequestBodyBuilder() {
        return (RequestBodyBuilder) ServiceLocale.findService(RequestBodyBuilder.class);
    }
}
