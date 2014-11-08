package com.sfebiz.demo.service.http;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.sfebiz.demo.api.DemoService;
import com.sfebiz.demo.api.DemoThirdPartyService;
import com.sfebiz.demo.dao.dto.DemoDTO;
import com.sfebiz.demo.dao.mapper.DemoMapper;
import com.sfebiz.demo.entity.DemoEntity;
import net.pocrd.define.Evaluater;
import net.pocrd.dubboext.DubboExtProperty;
import net.pocrd.entity.ServiceException;
import net.pocrd.util.EvaluaterProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class DemoServiceImpl implements DemoService {
    private static final Logger    logger    = LoggerFactory.getLogger(DemoServiceImpl.class);
    private static final Evaluater evaluater = EvaluaterProvider.getEvaluater(DemoEntity.class, DemoDTO.class);
    @Autowired
    private DemoMapper            demoMapper;
    @Autowired
    private DemoThirdPartyService demoThirdPartyService;

    @Override
    public DemoEntity sayHello(String name) throws ServiceException {
        DemoEntity result = new DemoEntity();
        DemoDTO demoDTO = demoMapper.queryEntity(name);
        demoDTO.setId(demoThirdPartyService.testThirdParty(demoDTO.getId()));
        evaluater.evaluate(result, demoDTO);
        return result;
    }

    @Override
    public DemoEntity tryError(String in) throws ServiceException {
        throw new RuntimeException("try error!");
    }

    @Override
    public String testRegistedDevice() throws ServiceException {
        return "test";
    }

    @Override
    public String testUserLogin(long deviceId, long userId) throws ServiceException {
        System.out.println("deviceId:" + deviceId + ", userId:" + userId);
        return "deviceId:" + deviceId + ", userId:" + userId;
    }

    @Override
    public String getResByThirdPartyId(String something) {
        System.out.println("something:" + something);
        return something;
    }

    @Override
    public String testRedirect() {
        DubboExtProperty.setRedirectUrl("http://www.sfht.com");
        return null;
    }
}
