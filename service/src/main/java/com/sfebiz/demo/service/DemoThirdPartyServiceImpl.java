package com.sfebiz.demo.service;

import com.sfebiz.demo.api.DemoThirdPartyService;
import net.pocrd.entity.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by guankaiqiang521 on 2014/10/22.
 */
public class DemoThirdPartyServiceImpl implements DemoThirdPartyService {
    private static final Logger logger = LoggerFactory.getLogger(DemoThirdPartyServiceImpl.class);
    @Override
    public int testThirdParty(int id) throws ServiceException{
        logger.info("id:{}", id);
        return id;
    }
}
