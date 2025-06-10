package com;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.MessageFormat;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class Info {
    public static void execute(Locale locale, ResourceBundle messages) {
        System.out.println(MessageFormat.format(messages.getString("info"), locale.toString()));
        System.out.println("Country: " + locale.getDisplayCountry(locale) + " (" + locale.getDisplayCountry() + ")");
        System.out.println("Language: " + locale.getDisplayLanguage(locale) + " (" + locale.getDisplayLanguage() + ")");
        System.out.println("Currency: " + Currency.getInstance(locale).getCurrencyCode()
                + " (" + Currency.getInstance(locale).getDisplayName(locale) + ")");

        String[] weekdays = new DateFormatSymbols(locale).getWeekdays();
        System.out.print("Week Days: ");
        for (int i = 2; i <= 7; i++) {
            System.out.print(weekdays[i] + ", ");
        }
        System.out.println(weekdays[1]); // Sunday

        String[] months = new DateFormatSymbols(locale).getMonths();
        System.out.print("Months: ");
        for (int i = 0; i < months.length - 1; i++) {
            System.out.print(months[i] + ", ");
        }
        System.out.println(months[months.length - 1]);

        Date today = new Date();
        DateFormat dfDefault = DateFormat.getDateInstance(DateFormat.LONG, Locale.ENGLISH);
        DateFormat dfLocale = DateFormat.getDateInstance(DateFormat.LONG, locale);
        System.out.println("Today: " + dfDefault.format(today) + " (" + dfLocale.format(today) + ")");
    }
}
