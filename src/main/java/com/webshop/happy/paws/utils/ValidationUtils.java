package com.webshop.happy.paws.utils;

import java.util.Date;
import java.util.Calendar;

public class ValidationUtils {
    public static boolean isStringNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isLongNullOrEmpty(Long value){return value == null;}

    public static boolean isAgeInvalid(Date dateOfBirth) {
        if (dateOfBirth == null) {
            return true;
        }

        Calendar dob = Calendar.getInstance();
        dob.setTime(dateOfBirth);

        Calendar currentDate = Calendar.getInstance();

        int age = currentDate.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        // Adjust age for leap years
        if (currentDate.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age < 18 || age > 120;

    }
    public static boolean isSameDayAndMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        return cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }

}
