package com.kfir.mylibrary.utils;

import com.kfir.mylibrary.dto.BookDTO;

public class validationUtil {

    /**
     * Method for checking if bookDTO is valid
     *
     * @param bookDTO to be checked
     * @return true if valid and false otherwise
     */
    public static boolean isBookValidForSave(BookDTO bookDTO) {
        return bookDTO != null &&
                notBlankOrEmpty(bookDTO.getTitle()) &&
                notBlankOrEmpty(bookDTO.getAuthor()) &&
                notBlankOrEmpty(bookDTO.getGenre());
    }

    /**
     * Method to check if string is valid (not null, empty or blank)
     *
     * @param strToCheck to be checked
     * @return true if valid and false otherwise
     */
    private static boolean notBlankOrEmpty(String strToCheck) {
        return strToCheck != null && !strToCheck.isBlank() && !strToCheck.isEmpty();
    }
}
