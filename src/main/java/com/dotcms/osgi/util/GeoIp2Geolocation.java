package com.dotcms.osgi.util;



import com.dotcms.util.GeoIp2CityDbUtil;
import com.dotcms.visitor.domain.Visitor;

import com.dotmarketing.portlets.rules.conditionlet.Location;

import java.io.Serializable;
import java.lang.reflect.Field;



public class GeoIp2Geolocation implements Serializable, GeolocationProvider {


    @Override
    public String getCountryCode() {
        return countryCode;
    }

    @Override
    public String getCity() {
        return city;
    }

    @Override
    public String getLatLong() {
        return latitude + "," + longitude;
    }

    static Field latitudeFld;
    static Field longitudeFld;

    private static final long serialVersionUID = 1L;
    private final String ipAddress;
    private static GeoIp2CityDbUtil geoIp2CityDbUtil;
    private final double latitude, longitude;
    private final Location location;
    private final String countryCode, city;
    private final static String unknown = "ukn";

    public GeoIp2Geolocation(Visitor visitor) {

        this.ipAddress = visitor.getIpAddress().getHostAddress();
        this.city = city();
        this.countryCode = country();
        this.location = location();
        this.latitude = latitude();
        this.longitude = longitude();

    }


    private GeoIp2CityDbUtil geo() {
        if ((geoIp2CityDbUtil == null)) {
            geoIp2CityDbUtil = GeoIp2CityDbUtil.getInstance();
        }
        return geoIp2CityDbUtil;
    }


    private String city() {
        try {
            return geo().getCityName(ipAddress);
        } catch (Exception e) {
            return unknown;
        }
    }

    private double latitude() {
        try {
            if (latitudeFld == null) {
                latitudeFld = Location.class.getDeclaredField("latitude");
                latitudeFld.setAccessible(true);
            }
            return (Double) latitudeFld.get(this.location);
        } catch (Exception e) {
            return 0d;
        }
    }

    private double longitude() {
        try {
            if (longitudeFld == null) {
                longitudeFld = Location.class.getDeclaredField("longitude");
                longitudeFld.setAccessible(true);
            }
            return (Double) longitudeFld.get(this.location);
        } catch (Exception e) {
            return 0d;
        }
    }

    private String country() {
        try {
            return geo().getCountryIsoCode(ipAddress);
        } catch (Exception e) {
            return unknown;
        }
    }

    private Location location() {
        try {
            return geo().getLocationByIp(ipAddress);
        } catch (Exception e) {
            return new Location(0, 0);
        }
    }

}
