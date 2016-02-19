package com.sfebiz.demo.mock;

import com.sfebiz.demo.api.DemoMockService;
import com.sfebiz.demo.entity.DemoEntity;
import net.pocrd.define.MockApiImplementation;
import net.pocrd.entity.ServiceException;

/**
 * Created by rendong on 15/8/5.
 */
public class MockServiceImpl implements MockApiImplementation<DemoMockService>, DemoMockService {

    @Override
    public DemoEntity testMockService(String name) throws ServiceException {
        DemoEntity entity = new DemoEntity();
        entity.id = 7654321;
        entity.name = "mock service test " + name;
        return entity;
    }

    @Override
    public void $setProxy(DemoMockService proxy) {

    }
}
