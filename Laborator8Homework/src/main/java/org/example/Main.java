package org.example;

import org.example.dao.City;
import org.example.dao.CityDAO;
import org.example.dao.SisterhoodDAO;
import org.example.dao.FakeCityGenerator;
import org.example.importer.CityImporter;
import org.example.dao.TarjanSCC;

import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        CityDAO cityDAO = new CityDAO();
        CityImporter importer = new CityImporter();
        SisterhoodDAO sisterhoodDAO = new SisterhoodDAO();
        FakeCityGenerator generator = new FakeCityGenerator();

        // 1. Importă orașele reale din CSV
        importer.importFromCSV("src/main/resources/cities.csv");

        List<City> fakeCities = generator.generateFakeCities(1000, 9999);

        fakeCities.forEach(cityDAO::insert);

        List<City> allCities = cityDAO.getAll();
        allCities.forEach(System.out::println);

        // 4. Calculează distanțele între toate orașele
        if (allCities.size() >= 2) {
            System.out.println("\n--- Distanțe între orașe ---");
            for (int i = 0; i < allCities.size(); i++) {
                for (int j = i + 1; j < allCities.size(); j++) {
                    City c1 = allCities.get(i);
                    City c2 = allCities.get(j);
                    double dist = CityDAO.calculateDistance(c1, c2);
                    System.out.printf("Distanța dintre %s și %s este %.2f km\n",
                            c1.getName(), c2.getName(), dist);
                }
            }
        } else {
            System.out.println("Ai nevoie de cel puțin două orașe în baza de date.");
        }

        Set<SisterhoodDAO.Pair> connections = sisterhoodDAO.generateRandomSisterhoods(allCities, 0.001);
        connections.forEach(pair -> sisterhoodDAO.insert(pair.getCity1Id(), pair.getCity2Id()));
        System.out.printf("Număr de relații de înfrățire create: %d\n", connections.size());
        TarjanSCC tarjan = new TarjanSCC(allCities, connections);
        List<List<Integer>> components = tarjan.run();

        System.out.println("\n--- Componente 2-conexe ---");
        int idx = 1;
        for (List<Integer> comp : components) {
            System.out.printf("Componenta %d: %s\n", idx++, comp);
        }
        tarjan.drawMap(connections);
    }
}
