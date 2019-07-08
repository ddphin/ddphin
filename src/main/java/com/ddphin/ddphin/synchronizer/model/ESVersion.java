package com.ddphin.ddphin.synchronizer.model;

import lombok.Data;

/**
 * ClassName: ESVersion
 * Function:  ESVersion
 * Date:      2019/6/21 上午10:00
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESVersion {
    private Long logId;
    private String id;
    private String index;
    private String context;
    private Long seqNo;
    private Long primaryTerm;
}
