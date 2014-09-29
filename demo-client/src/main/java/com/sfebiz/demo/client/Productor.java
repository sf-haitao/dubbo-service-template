package com.sfebiz.demo.client;

/**
 * 异步通讯过程中的生产者接口，用于在异步线程执行并产生类型为T的数据，请在实现时通过重载toString()函数输出调试信息以便于异步调试。
 * @author rendong
 *
 * @param <T>
 */
public interface Productor<T> {
    T run();
}
