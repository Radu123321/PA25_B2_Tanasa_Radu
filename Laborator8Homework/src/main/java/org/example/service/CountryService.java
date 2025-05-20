package org.example.service;

import org.example.dao.CountryDAO;
import org.example.model.Country;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private final CountryDAO countryDAO;

    public CountryService() {
        this.countryDAO = new CountryDAO();
    }

    public List<Country> getAllCountries() {
        return countryDAO.findAllCountries();
    }
}
