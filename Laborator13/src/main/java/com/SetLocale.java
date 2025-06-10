package com;

import java.util.Locale;
import java.util.ResourceBundle;
import java.text.MessageFormat;

public class SetLocale {
    public static Locale execute(String languageTag, ResourceBundle messages) {
        /// creeaza locatia pe baza unui cod primit de la utilizator
        Locale locale = Locale.forLanguageTag(languageTag);

        /// adaug o locatie noua in functie de limba
        if (locale.getCountry().isEmpty()) {
            if (locale.getLanguage().equals("ro")) {
                locale = new Locale("ro", "RO");
            } else if (locale.getLanguage().equals("en")) {
                locale = new Locale("en", "US");
            } else if (locale.getLanguage().equals("fr")) {
                locale = new Locale("fr", "FR");
            }
        }

        System.out.println(MessageFormat.format(messages.getString("locale.set"), locale.toString()));
        return locale;
    }
}
