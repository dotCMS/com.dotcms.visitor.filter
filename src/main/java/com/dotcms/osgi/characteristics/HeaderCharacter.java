package com.dotcms.osgi.characteristics;

import com.dotcms.osgi.util.BundleConfigProperties;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class HeaderCharacter extends AbstractCharacter {

    private final Set<String> WHITELISTED_HEADERS = new HashSet<>(
            Arrays.asList(BundleConfigProperties.getProperty("WHITELISTED_HEADERS", "").toLowerCase().split(",")));


    public HeaderCharacter(AbstractCharacter incomingCharacter) {
        super(incomingCharacter);


        for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
            String nextHeaderName = (String) e.nextElement().toLowerCase();
            if (WHITELISTED_HEADERS.contains(nextHeaderName)) {
                getMap().put("h." + nextHeaderName, request.getHeader(nextHeaderName));
            }
        }

    }

}
