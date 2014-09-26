package com.sfebiz.demo.entity;

import net.pocrd.annotation.Description;

import java.io.Serializable;

@Description("simple test entity")
public class SimpleTestEntity implements Serializable {
    @Description("string value")
    public String strValue;
    @Description("int array")
    public int[]  intArray;
}
