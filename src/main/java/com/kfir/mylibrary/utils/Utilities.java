package com.kfir.mylibrary.utils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Utilities {

    public static LocalDate getDueDate(int timePeriod) {
        return LocalDate.now().plusDays(timePeriod);
    }

}
