package com.dotcms.osgi.geo;



import java.util.Map;

import com.dotcms.visitor.domain.Visitor;
import com.google.common.collect.ImmutableMap;



public class GeoProvider implements GeoLocationProvider {



  private final static String VISITOR_PLUGIN_GEOLOCATION = "VISITOR_PLUGIN_GEOLOCATION";


  public Map<String,String> getGeoInfo(final Visitor visitor) {


    if (visitor.get(VISITOR_PLUGIN_GEOLOCATION) != null && visitor.get(VISITOR_PLUGIN_GEOLOCATION) instanceof Map) {
     // return (Map<String, String>) visitor.get(VISITOR_PLUGIN_GEOLOCATION);
    }

    final String ip = visitor.getIpAddress().getHostAddress();
    GeoIp2CityDbUtil geo = new GeoIp2CityDbUtil(visitor.getIpAddress());
    ImmutableMap<String, String> info;
    try {
      info = new ImmutableMap.Builder<String, String>().put("ip", ip).put("latLong", geo.getLatLong())
          .put("countryCode", geo.getCountryIsoCode()).put("cityName", geo.getCityName()).put("continent", geo.getContinent())
          .put("company", geo.getCompany()).build();
    } catch (Exception e) {
      info = ImmutableMap.of();
    }

    visitor.put(VISITOR_PLUGIN_GEOLOCATION, info);
    return info;

  }



}
