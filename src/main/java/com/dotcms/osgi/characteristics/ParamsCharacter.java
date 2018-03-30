package com.dotcms.osgi.characteristics;

import com.dotcms.osgi.util.BundleConfigProperties;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParamsCharacter extends AbstractCharacter {


    private static final Set<String> WHITELISTED_PARAMS =
            new HashSet<>(Arrays.asList(BundleConfigProperties.getProperty("WHITELISTED_PARAMS", "").toLowerCase().split(",")));

    public ParamsCharacter(AbstractCharacter incomingCharacter) {
        super(incomingCharacter);


        final Map<String, String> params = new HashMap<>();
        final String queryString = (request.getQueryString() != null) ? request.getQueryString().replaceAll("&", " ") : null;
        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
            String nextParamName = (String) e.nextElement().toLowerCase();
            if (WHITELISTED_PARAMS.contains(nextParamName)) {
                params.put("p." + nextParamName.toLowerCase(), request.getParameter(nextParamName));
            }
        }
        getMap().putAll(params);
        getMap().put("queryString", queryString);

    }

}
