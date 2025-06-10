package app;


/// toate locatiile disponibile
import com.DisplayLocales;
import com.Info;
/// seteaza locatia curenta
import com.SetLocale;

import java.util.Locale;

/// ResourceBundle: incarcă fișiere .properties pentru a afișa texte traduse
import java.util.ResourceBundle;
import java.util.Scanner;

public class LocaleExplore {
    private static Locale currentLocale = Locale.getDefault();
    /**
     * Incarcă fișierul Messages.properties (și Messages_<lang>.properties dacă exista) folosind currentLocale.
     * Acesta conține textele afisate în aplicație, în limba corespunzatoare (prompt, invalid, etc.).
     */
    private static ResourceBundle messages = ResourceBundle.getBundle("Messages", currentLocale);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            /**
             * Actualizez messages in caz ca currentLocale s-a schimbat intre timp
             * utilizatorul a tastat set locale ro).
             */
            messages = ResourceBundle.getBundle("Messages", currentLocale);
            System.out.print(messages.getString("prompt") + " ");
            String command = scanner.nextLine().trim();

            if (command.equalsIgnoreCase("display locales")) {
                DisplayLocales.execute(messages);
            } else if (command.startsWith("set locale")) {
                String[] parts = command.split(" ");
                if (parts.length >= 3) {
                    String tag = parts[2];
                    currentLocale = SetLocale.execute(tag, messages);
                }
                /**
                 * Daca utilizatorul scrie info, se apelează Info.execute(...), care afișeaza informații despre:
                 * tara, limba
                 * moneda
                 * zilele săptamanii
                 * luni
                 * data curenta
                 */
            } else if (command.equalsIgnoreCase("info")) {
                Info.execute(currentLocale, messages);
            } else if (command.equalsIgnoreCase("exit")) {
                break;
            } else {
                System.out.println(messages.getString("invalid"));
            }
        }
    }
}
