package com;

import java.util.Locale;
import java.util.ResourceBundle;

public class DisplayLocales {
    public static void execute(ResourceBundle messages) {
        System.out.println(messages.getString("locales"));
        for (Locale locale : Locale.getAvailableLocales()) {
            System.out.println(locale.toString() + " - " + locale.getDisplayName(locale));
        }
    }
}