package com.ddphin.ddphin.synchronizer.service.impl;

import com.ddphin.ddphin.synchronizer.mapper.ESVersionMapper;
import com.ddphin.ddphin.synchronizer.service.ESVersionService;
import com.ddphin.ddphin.synchronizer.bean.ESVersionBean;
import com.ddphin.ddphin.synchronizer.bean.ESVersionLogBean;
import com.ddphin.ddphin.synchronizer.model.ESVersion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ClassName: ESVersionServiceImpl
 * Function:  ESVersionServiceImpl
 * Date:      2019/6/29 上午11:49
 * Author     DaintyDolphin
 * Version    V1.0
 */
@Service
public class ESVersionServiceImpl implements ESVersionService {
    @Autowired
    private ESVersionMapper mapper;
    @Override
    public ESVersion query(ESVersionBean t) {
        return mapper.query(t);
    }

    @Override
    public Integer replaceList(List<ESVersionBean> t) {
        return mapper.replaceList(t);
    }

    @Override
    public Integer insertLogList(List<ESVersionLogBean> t) {
        return mapper.insertLogList(t);
    }
}
