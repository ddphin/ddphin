package com.ddphin.ddphin.synchronizer.mapper;

import com.ddphin.ddphin.synchronizer.bean.ESVersionBean;
import com.ddphin.ddphin.synchronizer.bean.ESVersionLogBean;
import com.ddphin.ddphin.synchronizer.model.ESVersion;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * ClassName: ESVersionMapper
 * Function:  ESVersionMapper
 * Date:      2019/6/17 下午2:59
 * Author     DaintyDolphin
 * Version    V1.0
 */

@Mapper
public interface ESVersionMapper {
    ESVersion query(ESVersionBean t);

    Integer replaceList(List<ESVersionBean> t);

    Integer insertLogList(List<ESVersionLogBean> t);
}
