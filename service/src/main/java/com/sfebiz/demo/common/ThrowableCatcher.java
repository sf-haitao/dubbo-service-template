package com.sfebiz.demo.common;

import com.alibaba.fastjson.JSON;
import com.sfebiz.demo.entity.DemoReturnCode;
import net.pocrd.dubboext.DubboExtProperty;
import net.pocrd.entity.ServiceException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by guankaiqiang521 on 2014/10/20.
 */
@Aspect
public class ThrowableCatcher {
    private Logger logger = LoggerFactory.getLogger(ThrowableCatcher.class);
    @Around(value = "within(com.sfebiz.demo.service.http.*)")//此处声明需要作用的http api包名
    public Object doThrowableCatch(ProceedingJoinPoint pjp) throws ServiceException {
        Object retVal = null;
        try {
            retVal = pjp.proceed();
        } catch (ServiceException se) {
            logger.error("error occured", se);
            throw se;
        } catch (Throwable throwable) {
            logger.error(
                    "execute failed, message: {\"method\":\"" + pjp.getSignature() + "\",\"arguments\":" + JSON.toJSONString(pjp.getArgs()) + "!",
                    throwable);
            //            DubboExtProperty.setErrorCode(DemoReturnCode.DEMO_UNKNOW_ERROR);//这个地方设置内部异常error code
            throw new ServiceException(DemoReturnCode.DEMO_UNKNOW_ERROR);
        }
        return retVal;
    }
}





