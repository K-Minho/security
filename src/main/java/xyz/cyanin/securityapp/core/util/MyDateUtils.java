package xyz.cyanin.securityapp.core.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MyDateUtils {
    
    public static String toString(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
}
