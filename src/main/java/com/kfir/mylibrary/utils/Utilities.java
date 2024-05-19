package com.kfir.mylibrary.utils;

import java.time.LocalDate;

public class Utilities {

    /**
     * Method for calculating the overdue date of a book according to param
     *
     * @param timePeriod (in days) for calculating the overdue date of a book
     * @return new LocalDate after adding the time period
     */
    public static LocalDate getDueDate(int timePeriod) {
        return LocalDate.now().plusDays(timePeriod);
    }

}
