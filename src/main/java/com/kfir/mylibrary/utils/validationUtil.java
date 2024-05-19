package com.kfir.mylibrary.utils;

import com.kfir.mylibrary.dto.BookDTO;

public class validationUtil {

    public static boolean isBookValidForSave(BookDTO bookDTO) {
        return bookDTO != null &&
                notBlankOrEmpty(bookDTO.getTitle()) &&
                notBlankOrEmpty(bookDTO.getAuthor()) &&
                notBlankOrEmpty(bookDTO.getGenre());
    }

    private static boolean notBlankOrEmpty(String strToCheck) {
        return strToCheck != null && !strToCheck.isBlank() && !strToCheck.isEmpty();
    }
}
