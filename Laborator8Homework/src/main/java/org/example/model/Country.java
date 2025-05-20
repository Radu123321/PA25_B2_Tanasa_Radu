package org.example.model;

public class Country {
    private int id;
    private String name;
    private String code;
    private int continentId;

    public Country() {}

    public Country(int id, String name, String code, int continentId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.continentId = continentId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public int getContinentId() { return continentId; }
    public void setContinentId(int continentId) { this.continentId = continentId; }
}
