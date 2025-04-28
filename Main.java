package org.example;
//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
// src/Main.java
public class Main {
    public static void main(String[] args) {
        ContinentDAO continentDAO = new ContinentDAO();
        CountryDAO countryDAO = new CountryDAO();

        // TEST: Inserează și caută
        continentDAO.create("Antarctica");
        continentDAO.findByName("Antarctica");

        countryDAO.create("Romania", "RO", 1); // continent_id = 1 (Europe)
        countryDAO.findByName("Romania");

        Database.closeConnection();
    }
}