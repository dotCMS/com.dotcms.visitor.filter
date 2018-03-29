package com.dotcms.osgi.geo;



import com.dotcms.visitor.domain.Visitor;

import com.dotmarketing.portlets.rules.conditionlet.Location;

import java.lang.reflect.Field;



public class GeoIp2GeolocationProvider implements GeoLocationProvider {


    private final static String VISITOR_PLUGIN_GEOLOCATION = "VISITOR_PLUGIN_GEOLOCATION";
    Field latitudeFld, longitudeFld;
    final GeoIp2CityDbUtil geo;

    private GeoIp2GeolocationProvider(final Visitor visitor) {
        this.geo = new GeoIp2CityDbUtil(visitor.getIpAddress());

    }

    public GeolocationInfo getGeoInfo(final Visitor visitor) {
        

        if (visitor.get(VISITOR_PLUGIN_GEOLOCATION) != null) {
            return (GeolocationInfo) visitor.get(VISITOR_PLUGIN_GEOLOCATION);
        }


        final String ip = visitor.getIpAddress().getHostAddress();
        final Location location = location(ip);
        final GeolocationInfoValue info = new GeolocationInfoValue(ip, latitude(location),
                longitude(), country(), city(), );

        visitor.put(VISITOR_PLUGIN_GEOLOCATION, info);
        return info;

    }



    private String city() {
        try {
            return geo.getCityName();
        } catch (Exception e) {
            return null;
        }
    }

    private double latitude(final Location location) {
        try {
            if (latitudeFld == null) {
                latitudeFld = Location.class.getDeclaredField("latitude");
                latitudeFld.setAccessible(true);
            }
            return (Double) latitudeFld.get(location);
        } catch (Exception e) {
            return 0d;
        }
    }

    private double longitude(final Location location) {
        try {
            if (longitudeFld == null) {
                longitudeFld = Location.class.getDeclaredField("longitude");
                longitudeFld.setAccessible(true);
            }
            return (Double) longitudeFld.get(location);
        } catch (Exception e) {
            return 0d;
        }
    }

    private String country() {
        try {
            return geo.getCountryIsoCode();
        } catch (Exception e) {
            return null;
        }
    }

    private String company() {
        try {
            return geo.getCompany();
        } catch (Exception e) {
            return null;
        }
    }

    private Location location(final String ipAddress) {
        try {
            return geo.getLocationByIp(ipAddress);
        } catch (Exception e) {
            return new Location(0, 0);
        }
    }

}
