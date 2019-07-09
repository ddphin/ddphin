package com.ddphin.ddphin.collector.entity;

import lombok.Data;

import java.util.Map;

/**
 * ClassName: ESSyncItemOutputItemHas
 * Function:  ESSyncItemOutputItemHas
 * Date:      2019/7/2 下午12:12
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESSyncItemOutputItemHas {
    private String as;
    private WithType withType;
    private String uniqueBy;

    public enum WithType {
        primitive, array,nested
    }
}
