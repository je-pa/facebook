package com.koreait.facebook.common;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class MySecurityUtilsTest {
    @Autowired
    private MySecurityUtils utils;

    @Test
    public void test1(){
        int len = 5;
        String val = utils.getRandomDigit(len);
        assertEquals(val.length(),len);

        String val2 = utils.getRandomDigit(len);
        assertEquals(val2.length(),len);

        System.out.println("val:"+val);
        System.out.println("val2:"+val2);
    }
}
