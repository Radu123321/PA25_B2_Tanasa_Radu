package org.example.dao;

public class City {
    private int id;
    private String name;
    private boolean capital;
    private double latitude;
    private double longitude;
    private int countryId;

    public City(int id, String name, boolean capital, double latitude, double longitude, int countryId) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryId = countryId;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    public boolean isCapital() { return capital; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public int getCountryId() { return countryId; }

    @Override
    public String toString() {
        return name + (capital ? " (capitală)" : "") + " [" + latitude + ", " + longitude + "]";
    }
}
