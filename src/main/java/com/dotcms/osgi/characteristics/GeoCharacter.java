package com.dotcms.osgi.characteristics;

import com.dotcms.osgi.geo.GeoIp2CityDbUtil;

import com.google.common.collect.ImmutableMap;

public class GeoCharacter extends AbstractCharacter {



    private final static String VISITOR_PLUGIN_GEOLOCATION = "VISITOR_PLUGIN_GEOLOCATION";


    public GeoCharacter(AbstractCharacter incomingCharacter) {
        super(incomingCharacter);

        ImmutableMap<String, String> m;
        if (visitor.get(VISITOR_PLUGIN_GEOLOCATION) != null && visitor.get(VISITOR_PLUGIN_GEOLOCATION) instanceof ImmutableMap) {
            m = (ImmutableMap<String, String>) visitor.get(VISITOR_PLUGIN_GEOLOCATION);
        } else {

            try {
                GeoIp2CityDbUtil geo = new GeoIp2CityDbUtil(visitor.getIpAddress());
                m = new ImmutableMap.Builder<String, String>().put("g.latLong", geo.getLatLong())
                    .put("g.countryCode", geo.getCountryIsoCode())
                    .put("g.cityName", geo.getCityName())
                    .put("g.continent", geo.getContinent())
                    .put("g.company", geo.getCompany())
                    .build();
            } catch (Exception e) {
                m = ImmutableMap.of("g.ip", "ukn");
            }

            visitor.put(VISITOR_PLUGIN_GEOLOCATION, m);
        }
        getMap().putAll(m);

    }

}
