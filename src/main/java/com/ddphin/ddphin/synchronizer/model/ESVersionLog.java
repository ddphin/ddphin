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
public class ESVersionLog {
    private String index;
    private String id;
    private Long seqNo;
    private Long primaryTerm;

    private Long lid;
    private Long logId;
    private String context;
    private String message;
    private Integer status;
}
