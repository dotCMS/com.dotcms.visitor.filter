package com.dotcms.osgi.util;

import com.dotcms.util.GeoIp2CityDbUtil;
import com.dotcms.visitor.business.VisitorAPIImpl;
import com.dotcms.visitor.domain.Visitor;
import com.dotcms.visitor.domain.Visitor.AccruedTag;

import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.filters.Constants;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.portlets.personas.model.IPersona;
import com.dotmarketing.util.WebKeys;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Splitter;

import eu.bitwalker.useragentutils.UserAgent;

public class VisitorLogger {
    
    static GeoIp2CityDbUtil geo = GeoIp2CityDbUtil.getInstance();
    static ObjectMapper mapper;
    private ObjectMapper mapper() {
        if(mapper==null) {
            ObjectMapper newMapper = new ObjectMapper();
            newMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper=newMapper;
        }
        return mapper;
    }

    final String UNK="unk";

    public void log(final HttpServletRequest request, final HttpServletResponse response) throws JsonProcessingException {


        if (!shouldLog(request, response)) {
            return;
        }

        final Map<String, Object> map = new LinkedHashMap<>();
        // file or page
        final Object asset = (request.getAttribute("idInode") != null) ?  request.getAttribute("idInode")
                : (Identifier) request.getAttribute(Constants.CMS_FILTER_IDENTITY);
        
        final String assetId = (asset instanceof Identifier) ? ((Identifier)asset).getId() : 
            (asset !=null && asset instanceof String) ?
                    (String) asset :UNK;
        
        

        final Optional<String> content = Optional.ofNullable((String) request.getAttribute(WebKeys.WIKI_CONTENTLET));

        final Language lang = WebAPILocator.getLanguageWebAPI().getLanguage(request);
        final Visitor visitor = new VisitorAPIImpl().getVisitor(request).get();
        final GeolocationProvider geo = new GeoIp2Geolocation(visitor);
        final IPersona persona = visitor.getPersona();
        final String dmid = (visitor.getDmid()==null) ? UNK: visitor.getDmid().toString();
        final String device = visitor.getDevice();

        final List<AccruedTag> tags = visitor.getTags();
        final Map<String, String> params = (request.getQueryString()!=null) ?  Splitter.on('&').trimResults().withKeyValueSeparator("=").split(request.getQueryString()) : Collections.emptyMap();

        final UserAgent agent = visitor.getUserAgent();
        final String sessionId = request.getSession().getId();
        



        Map<String, String> cookies = Arrays.asList(request.getCookies()).stream().collect(Collectors.toMap(from ->from.getName(),from -> from.getValue()));
        map.put("response", response.getStatus());
        map.put("sessionId", sessionId);
        map.put("ts", System.currentTimeMillis());
        map.put("ip", request.getRemoteHost());
        map.put("request", request.getRequestURI());
        map.put("query", request.getParameterMap());
        map.put("referer", request.getHeader("referer"));
        map.put("host", request.getHeader("host"));
        map.put("assetId", assetId);
        map.put("contentId", content.orElse(UNK));
        map.put("device", device);
        map.put("agent", agent);
        map.put("userAgent", request.getHeader("user-agent"));
        map.put("cookies", cookies);
        map.put("persona", (persona != null) ? persona.getKeyTag() : UNK);
        map.put("city", geo.getCity());
        map.put("country", geo.getCountryCode());
        map.put("lang", lang.toString());
        map.put("dmid", dmid);
        map.put("latLong", geo.getLatLong());
        map.put("tags", tags);
        map.put("params", params);
        map.put("pagesViewed", visitor.getNumberPagesViewed());


        doLog(mapper().writeValueAsString(map));
    }

    private void doLog(Object message) {
        Logger.info(message);
    }


    private boolean shouldLog(HttpServletRequest request, HttpServletResponse response) {
        return 500 != response.getStatus() && new VisitorAPIImpl().getVisitor(request, false).isPresent();
    }


}
