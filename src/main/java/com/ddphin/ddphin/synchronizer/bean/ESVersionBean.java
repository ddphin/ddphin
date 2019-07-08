package com.ddphin.ddphin.synchronizer.bean;

import lombok.Data;

/**
 * ClassName: ESVersionBean
 * Function:  ESVersionBean
 * Date:      2019/6/21 上午10:00
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESVersionBean {
    private Long logId;
    private String id;
    private String index;
    private String context;
    private Long seqNo;
    private Long primaryTerm;
}
