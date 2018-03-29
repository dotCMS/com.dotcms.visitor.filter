package com.dotcms.osgi.geo;

import java.io.Serializable;

public class GeolocationInfoValue implements Serializable, GeolocationInfo {
    private static final long serialVersionUID = 1L;
    private final String countryCode, city, continent, company;
    private final double latitude, longitude;

    public GeolocationInfoValue(final String ipAddress, final double latitude, final double longitude, final String countryCode,
            final String city, final String continent, final String company) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.countryCode = countryCode;
        this.city = city;
        this.continent = continent;
        this.company = company;
    }



    @Override
    public String getCompany() {
        // TODO Auto-generated method stub
        return company;
    }



    @Override
    public String getEmail() {
        // TODO Auto-generated method stub
        return null;
    }



    @Override
    public String getContinent() {
        return this.continent;
    }



    @Override
    public String getCountryCode() {
        return this.countryCode;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public String getLatLong() {
        return latitude + "," + longitude;
    }


}
