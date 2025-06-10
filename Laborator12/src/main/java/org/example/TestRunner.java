package org.example;

import java.io.*;

///pentru introspecția claselor si apelarea metodelor

import java.lang.reflect.*;
///pentru manipulare URL-uri si clase pentru incarcarea fișierelor .class sau .jar.
import java.net.*;
import java.util.*;
import java.util.jar.*;

public class TestRunner {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.out.println("Usage: java TestRunner <class file | folder | jar>");
            return;
        }

        File input = new File(args[0]);
        List<Class<?>> testClasses = new ArrayList<>();

        ///  Identifica ce tip de input este: folder, .jar sau fișier .class
        /// Apoi incarca clasele din acel input

        if (input.isDirectory()) {
            findClassesInDirectory(input, input.getAbsolutePath(), testClasses);
        } else if (input.getName().endsWith(".jar")) {
            findClassesInJar(input, testClasses);
        } else if (input.getName().endsWith(".class")) {
            Class<?> cls = loadClassFromFile(input);
            if (cls != null) testClasses.add(cls);
        }

        int total = 0, passed = 0, failed = 0;

        for (Class<?> cls : testClasses) {
            ///  Ignoră clasele care nu sunt publice sau nu au adnotarea @Test
            if (!Modifier.isPublic(cls.getModifiers())) continue;
            ///if (!cls.isAnnotationPresent(Test.class)) continue;
            System.out.println("\nTesting class: " + cls.getName());
            System.out.println("Prototype for class: " + cls.getName());
            printClassPrototype(cls);
            System.out.println();

            /// Itereaza toate metodele din clasa

            for (Method method : cls.getDeclaredMethods()) {
                ///  Dacă metoda este adnotata cu @Test, o numaram ca test
                if (method.isAnnotationPresent(Test.class)) {
                    total++;
                    try {
                        Object instance = null;
                        if (!Modifier.isStatic(method.getModifiers())) {
                            Constructor<?> cons = cls.getDeclaredConstructor();
                            cons.setAccessible(true);
                            instance = cons.newInstance();
                        }
                        /**
                         *  Genereaza argumente mock(fictive), apeleaza metoda si marcheaza testul ca trecut.
                         */
                        Object[] argsMock = mockArguments(method);
                        method.setAccessible(true);
                        method.invoke(instance, argsMock);
                        System.out.println("Test passed: " + method.getName());
                        passed++;
                    } catch (Exception e) {
                        System.out.println("Test failed: " + method.getName());
                        failed++;
                    }
                }
            }
        }

        System.out.println("\nTotal tests: " + total);
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);
    }
    ///  Creeaza argumente fictive pentru metoda data
    /// Obtine tipurile parametrilor metodei
    /// Genereaza valori standard pentru int si String altfel, seteaza null.
    private static Object[] mockArguments(Method method) {
        Class<?>[] types = method.getParameterTypes();
        Object[] args = new Object[types.length];
        for (int i = 0; i < types.length; i++) {
            if (types[i] == int.class) args[i] = 0;
            else if (types[i] == String.class) args[i] = "test";
            else args[i] = null;
        }
        return args;
    }

    /// Recursiv, cauta fișiere .class intr-un folder și le transforma în clase folosind Class.forName.
    private static void findClassesInDirectory(File dir, String root, List<Class<?>> result) {
        File[] files = dir.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (file.isDirectory()) {
                findClassesInDirectory(file, root, result);
            } else if (file.getName().endsWith(".class")) {
                try {
                    String name = file.getAbsolutePath().substring(root.length() + 1)
                            .replace(File.separatorChar, '.').replaceAll("\\.class$", "");
                    result.add(Class.forName(name));
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
        }
    }

    /// Caută clase intr-un fișier .jar și le incarca cu un URLClassLoader.

    private static void findClassesInJar(File jarFile, List<Class<?>> result) throws IOException {
        JarFile jar = new JarFile(jarFile);
        Enumeration<JarEntry> entries = jar.entries();
        URLClassLoader loader = new URLClassLoader(new URL[]{ jarFile.toURI().toURL() });
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            if (entry.getName().endsWith(".class")) {
                String className = entry.getName().replace('/', '.').replaceAll("\\.class$", "");
                try {
                    result.add(loader.loadClass(className));
                } catch (ClassNotFoundException e) {
                    // ignore
                }
            }
        }
    }

    /// Incarca o singura clasa dintr un fisier .class izolat
    private static Class<?> loadClassFromFile(File classFile) {
        try {
            String className = classFile.getName().replace(".class", "");
            URL url = classFile.getParentFile().toURI().toURL();
            URLClassLoader loader = new URLClassLoader(new URL[]{url});
            return loader.loadClass(className);
        } catch (Exception e) {
            return null;
        }
    }

    private static void printClassPrototype(Class<?> cls) {
        for (Method method : cls.getDeclaredMethods()) {
            StringBuilder sb = new StringBuilder();
            int modifiers = method.getModifiers();
            sb.append(Modifier.toString(modifiers)).append(" ");
            sb.append(method.getReturnType().getSimpleName()).append(" ");
            sb.append(method.getName()).append("(");
            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                sb.append(params[i].getSimpleName());
                if (i < params.length - 1) sb.append(", ");
            }
            sb.append(");");
            System.out.println(sb.toString());
        }
    }
}
