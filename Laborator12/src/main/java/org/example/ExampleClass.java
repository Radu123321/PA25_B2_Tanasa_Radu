package org.example;

public class ExampleClass {

    @Test
    ///  Metoda statica fără argumente, adnotata cu @Test, deci va fi executata de analizator.
    public static void test1() {
        System.out.println("Test 1 executat");
    }

    @Test
    public static void test2() {
        System.out.println("Test 2 executat");
    }

    public static void noTest() {
        System.out.println("Aceasta metoda nu are adnotarea @Test");
    }

    /// Metoda ne-statica, deci nu va fi apelata de analizator (nu indeplinește condiția).
    public void instanceMethod() {
        System.out.println("Metoda non-statica (nu va fi executata)");
    }
}
