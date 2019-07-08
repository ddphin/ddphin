package com.ddphin.ddphin.collector.entity;

import lombok.Data;

import java.util.Map;

/**
 * ClassName: ESSyncItemOutputItem
 * Function:  ESSyncItemOutputItem
 * Date:      2019/7/2 下午12:12
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESSyncItemOutputItem {
    private String key;

    private Map<String, String> map;

    private ESSyncItemOutputItemAssociation association;

    private ESSyncItemOutputItemReload reload;

    private Map<String, ESSyncItemOutputItemHas> has;
}
