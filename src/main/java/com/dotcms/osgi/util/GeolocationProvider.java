package com.dotcms.osgi.util;

public interface GeolocationProvider {

    String getCountryCode();

    String getCity();

    String getLatLong();

}
