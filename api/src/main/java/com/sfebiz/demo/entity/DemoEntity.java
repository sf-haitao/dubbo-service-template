package com.sfebiz.demo.entity;

import net.pocrd.annotation.Description;

import java.io.Serializable;

/**
 * Created by guankaiqiang521 on 2014/9/29.
 */
@Description("demo entity")
public class DemoEntity implements Serializable {
    @Description("id")
    public int    id;
    @Description("name")
    public String name;
}
