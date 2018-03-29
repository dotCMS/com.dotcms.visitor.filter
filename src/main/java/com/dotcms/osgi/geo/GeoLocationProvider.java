package com.dotcms.osgi.geo;

import com.dotcms.visitor.domain.Visitor;

public interface GeoLocationProvider {

    GeolocationInfo getGeoInfo(Visitor visitor);

}
