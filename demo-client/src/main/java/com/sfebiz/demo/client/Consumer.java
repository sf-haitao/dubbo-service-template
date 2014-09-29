package com.sfebiz.demo.client;

/**
 * 异步通讯过程中的消费者接口，用于接受异步获取的T类型数据并在主线程执行UI逻辑。
 * @author rendong
 *
 * @param <T>
 */
public interface Consumer<T> {
    void run(T p);
}
