package org.example.importer;

import com.opencsv.CSVReader;
import org.example.dao.City;
import org.example.dao.CityDAO;
import org.example.dao.CountryDAO;

import java.io.FileReader;
import java.util.concurrent.atomic.AtomicInteger;

public class CityImporter {
    public void importFromCSV(String path) {
        CityDAO cityDAO = new CityDAO();
        CountryDAO countryDAO = new CountryDAO();
        AtomicInteger idCounter = new AtomicInteger(1);

        try (CSVReader reader = new CSVReader(new FileReader(path))) {
            String[] line;
            reader.readNext(); // skip header

            while ((line = reader.readNext()) != null) {
                String city = line[0];
                String country = line[1];
                boolean isCapital = "primary".equalsIgnoreCase(line[2]);
                double lat = Double.parseDouble(line[3]);
                double lon = Double.parseDouble(line[4]);
                int countryId = countryDAO.findIdByName(country);
                if (countryId == -1) {
                    System.out.println("Țara '" + country + "' nu a fost găsită. Orașul '" + city + "' va fi ignorat.");
                    continue;
                }

                // Inserăm orașul
                cityDAO.insert(new City(
                        idCounter.getAndIncrement(), city, isCapital, lat, lon, countryId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
