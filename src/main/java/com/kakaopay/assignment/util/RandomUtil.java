package com.kakaopay.assignment.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sangwon on 20. 12. 22..
 */
public class RandomUtil {
    public static List<Long> getRandomlyDividedAmountList(long amount, long peopleNumber) {
        long rest = amount - peopleNumber;
        List<Long> result = new ArrayList<>();
        for (int i = 0; i < peopleNumber; i++) {
            if (i == peopleNumber - 1) {
                result.add(rest + 1);
            } else {
                long randomAmount = Double.valueOf(Math.floor(Math.random() * amount) % rest).longValue();
                rest -= randomAmount;
                result.add(randomAmount + 1);
            }
        }
        return result;
    }

    public static String getRandomTokenKey(int n) {
        char[] randomChars = new char[n];
        for (int i = 0; i < randomChars.length; i++) {
            char randomChar;
            if(Math.floor(Math.random() * 10) % 2 == 1){
                //lower case
                randomChar = (char) ((int) (Math.random() * 26) + 65);
            }else{
                //upper case
                randomChar = (char) ((int) (Math.random() * 26) + 97);
            }

            randomChars[i] = randomChar;
        }
        return new String(randomChars);
    }

}
