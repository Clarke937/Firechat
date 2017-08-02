package com.eretana.firechat.utils;

import java.util.Random;

/**
 * Created by Edgar on 20/7/2017.
 */

public class MyRandoms {

    public static int getRandom(int max){
        Random rand = new Random();
        return rand.nextInt(max);
    }

}
