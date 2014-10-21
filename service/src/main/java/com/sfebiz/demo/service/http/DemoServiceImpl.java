package com.sfebiz.demo.service.http;

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
    @Override
    public DemoEntity sayHello(String name) {
        DemoEntity result = new DemoEntity();
        DemoDTO demoDTO = demoMapper.queryEntity(name);
        evaluater.evaluate(result, demoDTO);
        return result;
    }
    @Override
    public DemoEntity tryError(String in) {
        throw new RuntimeException("try error!");
    }
}
