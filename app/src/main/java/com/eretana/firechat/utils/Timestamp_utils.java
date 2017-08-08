package com.eretana.firechat.utils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Edgar on 26/6/2017.
 */

public final class Timestamp_utils {

    public static long current_time(){
        Timestamp time = new Timestamp(System.currentTimeMillis());
        return time.getTime();
    }

    public static String elapsed_time(long miliseconds){

        long seconds = miliseconds / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        //Segundos
        if(seconds < 60){
            return seconds + "s";
        }else if(seconds > 60 && seconds < 3600){
            return minutes + "m";
        }else if(seconds > 3600 && seconds < 86400){
            return hours + "h";
        }else if (seconds > 86400){
            return days + "d";
        }

        return "NaN";
    }

    public static String current_date(){
        Calendar calendar = Calendar.getInstance();
        String day = "" + calendar.get(Calendar.DAY_OF_MONTH);
        String month = "" + (calendar.get(Calendar.MONTH) + 1);
        String year = "" + calendar.get(Calendar.YEAR);

        return day + "/" + month + "/" + year;
    }




}
