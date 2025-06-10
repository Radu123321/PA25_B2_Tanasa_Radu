package org.example;



import java.lang.reflect.Method;
/// Modifier – pentru a verifica dacă o metodă este public, static, etc.
import java.lang.reflect.Modifier;

public class Main {

    public static void main(String[] args) {
        /// numele complet al clasei pe care vrei sa il incarci
        String className = "org.example.ExampleClass";

        try {
            /*
                Incarca in memorie clasa org.example.ExampleClass folosind numele complet.
                 clazz devine o referința la clasa incarcata
             */
            Class<?> clazz = Class.forName(className);
            /// numele complet al clasei incarcate
            System.out.println("Clasa: " + clazz.getName());
            /// obtine toate metodele din clasa incarcata
            Method[] methods = clazz.getDeclaredMethods();

            System.out.println("\n--- Metode disponibile ---");
            /// Afișeaza modificatorii metodei (public static), tipul returnat și numele metodei.
            for (Method method : methods) {
                System.out.println(
                        Modifier.toString(method.getModifiers()) + " " +
                                method.getReturnType().getSimpleName() + " " +
                                method.getName()
                );
            }

            System.out.println("\n--- Execut metode cu @Test ---");
            /*
            (verific daca)
                    Metoda are adnotarea @Test
                    Este static
                    Nu are parametri
                    ->execut metoda
                    ->apelez metoda fara parametri(null pentru ca e static)
             */
            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class)
                        && Modifier.isStatic(method.getModifiers())
                        && method.getParameterCount() == 0) {
                    System.out.println("Execut: " + method.getName());
                    method.invoke(null);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
