package com.kakaopay.assignment;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sangwon on 20. 12. 22..
 */

public class RandomTest {
    @Test
    public void createNumberTest() {
        long amount = 100;
        long peopleNumber = 1;
        long rest = amount - peopleNumber;
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < peopleNumber; i++) {
            if(i == peopleNumber-1){
                result.add(rest + 1);
                System.out.println(rest + 1);
            }else{
                double randomAmount = Math.floor(Math.random() * amount) % rest;
                rest -= randomAmount;
                result.add(Double.valueOf(randomAmount).longValue() + 1);
                System.out.println(randomAmount + 1);
            }
        }

        System.out.println(result.stream().reduce((a,b)->a+b).get());

    }

}
