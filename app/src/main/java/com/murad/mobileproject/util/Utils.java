package com.murad.mobileproject.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static String dateFormatter(Date date){
       SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLLL yyyy HH:mm", new Locale("tr"));

       return simpleDateFormat.format(date);

    }
}
