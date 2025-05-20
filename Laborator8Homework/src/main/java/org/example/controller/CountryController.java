package org.example.controller;

import org.example.model.Country;
import org.example.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

///  Marcheaza clasa ca REST Controller, deci metodele vor raspunde cu JSON/XML în HTTP.

@RestController
///Seteaza ruta de baza pentru toate metodele din acest controller.
@RequestMapping("/api")
public class CountryController {

    private final CountryService countryService;
    ///Constructor default care inițializează manual CountryService.
    public CountryController() {
        this.countryService = new CountryService();
    }
    /// raspunde la GET/API/COUNTRIES
    /// returneaza lista de tari
    /// este apelata de browser/Postman si trimite un json
    @GetMapping("/countries")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }  @GetMapping("/cities")
    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }
}
