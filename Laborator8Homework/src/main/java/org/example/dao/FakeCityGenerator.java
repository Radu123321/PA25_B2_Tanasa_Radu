package org.example.dao;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class FakeCityGenerator {
    public List<City> generateFakeCities(int count, int countryId) {
        List<City> cities = new ArrayList<>();
        CityDAO dao = new CityDAO();

        for (int i = 1; i <= count; i++) {
            String name = "FakeCity" + i;
            double lat = ThreadLocalRandom.current().nextDouble(-90, 90);
            double lon = ThreadLocalRandom.current().nextDouble(-180, 180);
            boolean capital = false;
            City city = new City(i, name, capital, lat, lon, countryId);
            dao.insert(city);
            cities.add(city);
        }

        return cities;
    }
}
