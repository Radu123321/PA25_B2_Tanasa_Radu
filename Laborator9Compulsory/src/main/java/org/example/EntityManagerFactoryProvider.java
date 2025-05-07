package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class EntityManagerFactoryProvider {

    // Numele unității de persistență, folosit pentru a configura EntityManagerFactory
    private static final String PERSISTENCE_UNIT_NAME = "WorldPU";
    private static EntityManagerFactory emf;

    // Constructor privat pentru a preveni instanțierea clasei
    private EntityManagerFactoryProvider() {
        // constructor privat pentru singleton
    }

    // Metodă statică pentru obținerea instanței EntityManagerFactory
    public static EntityManagerFactory getInstance() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        }
        return emf;
    }

    // Metodă statică pentru a închide EntityManagerFactory
    public static void close() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
