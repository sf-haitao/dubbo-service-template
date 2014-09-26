package com.sfebiz.demo.entity;

import net.pocrd.annotation.Description;

import java.io.Serializable;
import java.util.List;

@Description("ComplexTestEntity")
public class ComplexTestEntity implements Serializable {
    @Description("strValue")
    public String                 strValue;
    @Description("shortValue")
    public short                  shortValue;
    @Description("byteValue")
    public byte                   byteValue;
    @Description("doubleValue")
    public double                 doubleValue;
    @Description("floatValue")
    public float                  floatValue;
    @Description("boolValue")
    public boolean                boolValue;
    @Description("intValue")
    public int                    intValue;
    @Description("longValue")
    public long                   longValue;
    @Description("charValue")
    public char                   charValue;
    @Description("SimpleTestEntity List")
    public List<SimpleTestEntity> simpleTestEntityList;
    @Description("simpleTestEntity")
    public SimpleTestEntity       simpleTestEntity;
}
