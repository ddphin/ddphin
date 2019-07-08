package com.ddphin.ddphin.synchronizer.bean;

import lombok.Data;

/**
 * ClassName: ESVersionLogBean
 * Function:  ESVersionLogBean
 * Date:      2019/6/21 上午10:00
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Data
public class ESVersionLogBean extends ESVersionBean {
    private Long logId;
    private String context;
    private String message;
    private Integer status;
}
