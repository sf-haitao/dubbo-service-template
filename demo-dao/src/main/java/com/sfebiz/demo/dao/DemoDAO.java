package com.sfebiz.demo.dao;

import com.sfebiz.demo.dto.DemoDTO;

/**
 * Created by rendong on 14-9-27.
 */
public class DemoDAO {
    public DemoDTO queryEntity(int id){
        DemoDTO dto = new DemoDTO();
        dto.setP1(id);
        dto.setP2(123);
        dto.setP3("xxx");

        return dto;
    }
}
