package com.ddphin.ddphin.collector.entity;

import lombok.Data;

/**
 * ClassName: ESSyncItemOutputItemAssociation
 * Function:  ESSyncItemOutputItemAssociation
 * Date:      2019/7/2 下午12:12
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESSyncItemOutputItemAssociation {
    private ESSyncItemOutputItemBelongs belongs;
    private ESSyncItemOutputItemReload onMissing;
}
