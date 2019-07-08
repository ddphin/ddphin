package com.ddphin.ddphin.collector.context;

/**
 * ClassName: ContextHolder
 * Function:  ContextHolder
 * Date:      2019/7/5 下午4:03
 * Author     DaintyDolphin
 * Version    V1.0
 */

public class ContextHolder {
    // TransmittableThreadLocal
    private static final ThreadLocal<CollectorContext> CONTEXT = ThreadLocal.withInitial(
            () -> new CollectorContext());

    public static CollectorContext get() {
        return CONTEXT.get();
    }
    public static void remove() {
        CONTEXT.remove();
    }
}
