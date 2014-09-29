package com.sfebiz.demo.service;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.sfebiz.demo.api.DemoService;
import com.sfebiz.demo.dao.dto.DemoDTO;
import com.sfebiz.demo.dao.mapper.DemoMapper;
import com.sfebiz.demo.entity.DemoEntity;
import com.sfebiz.demo.entity.DemoReturnCode;
import net.pocrd.define.Evaluater;
import net.pocrd.dubboext.DubboExtProperty;
import net.pocrd.util.EvaluaterProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class DemoServiceImpl implements DemoService {
    private static final Logger    logger    = LoggerFactory.getLogger(DemoServiceImpl.class);
    private static final Evaluater evaluater = EvaluaterProvider.getEvaluater(DemoEntity.class, DemoDTO.class);

    @Autowired
    private DemoMapper demoMapper;
    //TODO AOP logger all uncatch Exception
    @Override
    public DemoEntity sayHello(String name) {
        DemoEntity result = null;
        try {
            result = new DemoEntity();
            evaluater.evaluate(demoMapper.queryEntity(name), result);
        } catch (Throwable throwable) {
            logger.error("sayHello failed!", throwable);
            DubboExtProperty.setErrorCode(DemoReturnCode.DEMO_UNKNOW_ERROR);
        }
        return result;
    }
}
