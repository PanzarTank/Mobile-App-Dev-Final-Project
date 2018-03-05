package com.example.shelterconnect.util;

import java.text.DecimalFormat;

/**
 * Created by daniel on 3/4/18.
 */

public class Functions {

    private static DecimalFormat df = new DecimalFormat("#.##");

    public static String convertToFormat(double value){

        return df.format(value);
    }

}
