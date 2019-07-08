package com.ddphin.ddphin.synchronizer.service;

import com.ddphin.ddphin.synchronizer.bean.ESVersionBean;
import com.ddphin.ddphin.synchronizer.bean.ESVersionLogBean;
import com.ddphin.ddphin.synchronizer.model.ESVersion;

import java.util.List;


/**
 * ClassName: ESVersionService
 * Function:  ESVersionService
 * Date:      2019/6/17 下午2:59
 * Author     DaintyDolphin
 * Version    V1.0
 */

public interface ESVersionService {
    ESVersion query(ESVersionBean t);
    Integer replaceList(List<ESVersionBean> t);
    Integer insertLogList(List<ESVersionLogBean> t);
}
