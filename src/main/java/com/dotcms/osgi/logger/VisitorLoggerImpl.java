package com.dotcms.osgi.logger;

import com.dotcms.enterprise.cluster.ClusterFactory;
import com.dotcms.osgi.geo.GeoProvider;
import com.dotcms.osgi.servlet.VisitorFilter;
import com.dotcms.osgi.util.BundleConfigProperties;
import com.dotcms.uuid.shorty.ShortyIdAPI;
import com.dotcms.visitor.business.VisitorAPIImpl;
import com.dotcms.visitor.domain.Visitor;
import com.dotcms.visitor.domain.Visitor.AccruedTag;

import com.dotmarketing.beans.Identifier;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.business.web.WebAPILocator;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.filters.Constants;
import com.dotmarketing.portlets.languagesmanager.model.Language;
import com.dotmarketing.portlets.personas.model.IPersona;
import com.dotmarketing.util.WebKeys;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import eu.bitwalker.useragentutils.UserAgent;

public class VisitorLoggerImpl implements VisitorLogger {


    private static final String CLUSTER_ID = ClusterFactory.getClusterId();
    private static ObjectMapper mapper;
    private final String GDPR_CONSENT_PROPERTY = "GDPR_CONSENT";
    private final String GDPR_CONSENT_DEFAULT_PROPERTY = "GDPR_CONSENT_DEFAULT_PROPERTY";
    private final boolean GDPR_CONSENT;
    private final String UNK = null;

    private final Set<String> WHITELISTED_HEADERS;
    private final Set<String> WHITELISTED_COOKIES;
    private final Set<String> WHITELISTED_PARAMS;
    private final ShortyIdAPI shorty=APILocator.getShortyAPI();
    public VisitorLoggerImpl() {
        this.GDPR_CONSENT = BundleConfigProperties.getBooleanProperty(GDPR_CONSENT_DEFAULT_PROPERTY, false);


        
        WHITELISTED_HEADERS =
                new HashSet<>(Arrays.asList(BundleConfigProperties.getProperty("WHITELISTED_HEADERS", "").toLowerCase().split(",")));
        WHITELISTED_COOKIES =
                new HashSet<>(Arrays.asList(BundleConfigProperties.getProperty("WHITELISTED_COOKIES", "").toLowerCase().split(",")));
        WHITELISTED_PARAMS =
                new HashSet<>(Arrays.asList(BundleConfigProperties.getProperty("WHITELISTED_PARAMS", "").toLowerCase().split(",")));

    }



    private final ThreadLocal<Map<String, Object>> myMap = new ThreadLocal<Map<String, Object>>() {
        @Override
        protected Map<String, Object> initialValue() {
            return new LinkedHashMap<>();
        }
    };



    private ObjectMapper mapper() {
        if (mapper == null) {
            ObjectMapper newMapper = new ObjectMapper();
            newMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            mapper = newMapper;
        }
        return mapper;
    }



    @Override
    public void log(final HttpServletRequest request, final HttpServletResponse response) throws JsonProcessingException {

        if (!shouldLog(request, response)) {
            return;
        }
        // clear after every request
        try {
            logInternal(request, response);
        } finally {
            myMap.get().clear();
        }
    }

    private void logInternal(final HttpServletRequest request, final HttpServletResponse response) {


        final Map<String, Object> map = myMap.get();
        final Object asset = (request.getAttribute("idInode") != null) ? request.getAttribute("idInode")
                : (Identifier) request.getAttribute(Constants.CMS_FILTER_IDENTITY);

        final String assetId = (asset instanceof Identifier) ? ((Identifier) asset).getId()
                : (asset != null && asset instanceof String) ? (String) asset : UNK;

          

        final Optional<String> content = Optional.ofNullable((String) request.getAttribute(WebKeys.WIKI_CONTENTLET));

        final Language lang = WebAPILocator.getLanguageWebAPI().getLanguage(request);
        final Visitor visitor = new VisitorAPIImpl().getVisitor(request).get();
        final Map<String, String> geoInfo = new GeoProvider().getGeoInfo(visitor);
        final IPersona persona = visitor.getPersona();
        final String dmid = (visitor.getDmid() == null) ? UNK : visitor.getDmid().toString();
        final String device = visitor.getDevice();

        final List<AccruedTag> tags = visitor.getTags();
        final Map<String, String> params = new HashMap<>();
        for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
            String nextParamName = (String) e.nextElement();
            params.put(nextParamName.toLowerCase(), request.getParameter(nextParamName));
        }
        
        final Map<String, String> cookies = (request.getCookies() != null) ? Arrays.asList(request.getCookies()).stream().collect(
                Collectors.toMap(from -> from.getName().toLowerCase(), from -> from.getValue())) : new HashMap<>();



        final Map<String, String> headers = new HashMap<>();
        for (Enumeration<String> e = request.getHeaderNames(); e.hasMoreElements();) {
            String nextHeaderName = (String) e.nextElement();
            headers.put(nextHeaderName.toLowerCase(), request.getHeader(nextHeaderName));
        }


        cookies.keySet().retainAll(WHITELISTED_COOKIES);
        headers.keySet().retainAll(WHITELISTED_HEADERS);
        params.keySet().retainAll(WHITELISTED_PARAMS);


        final UserAgent agent = visitor.getUserAgent();
        final String sessionId = request.getSession().getId();  
        final String remoteIp = visitor.getIpAddress().getHostAddress();


        
        map.put("response", response.getStatus());
        map.put("clusterId", shorty.shortify(CLUSTER_ID));
        map.put("sessionId", shorty.shortify(sessionId));
        map.put("ts", System.currentTimeMillis());
        map.put("ipHash", DigestUtils.sha1Hex(remoteIp));
        map.put("mimeType", response.getContentType());
        map.put("vanityUrl", request.getAttribute(VisitorFilter.VANITY_URL_ATTRIBUTE));
        map.put("request", request.getRequestURI());
        map.put("query", request.getParameterMap());
        map.put("referer", request.getHeader("referer"));
        map.put("host", request.getHeader("host"));
        map.put("assetId", assetId);
        map.put("contentId", content.orElse(UNK));
        map.put("device", device);
        map.put("agent", agent);
        map.put("userAgent", request.getHeader("user-agent"));
        map.put("persona", (persona != null) ? persona.getKeyTag() : UNK);
        map.put("geo", geoInfo);
        map.put("lang", lang.toString());
        map.put("dmid", dmid);
        map.put("weightedTags", tags);
        map.put("params", params);
        map.put("pagesViewed", visitor.getNumberPagesViewed());
        if (hasGdprConsent(request)) {
            map.put("ip", remoteIp);
            map.put("cookies", cookies);
        }

        try {
            doLog(mapper().writeValueAsString(map));
        } catch (JsonProcessingException e) {
            throw new DotRuntimeException(e);
        }
    }

    private void doLog(Object message) {
        org.pmw.tinylog.Logger.info(message);
    }


    private boolean shouldLog(HttpServletRequest request, HttpServletResponse response) {
        return 500 != response.getStatus() && new VisitorAPIImpl().getVisitor(request, false).isPresent();
    }

    private boolean hasGdprConsent(final HttpServletRequest request) {

        return (GDPR_CONSENT
                && (request.getAttribute(GDPR_CONSENT_PROPERTY) == null || (boolean) request.getAttribute(GDPR_CONSENT_PROPERTY))
                && request.getSession().getAttribute(GDPR_CONSENT_PROPERTY) == null
                || (boolean) request.getSession().getAttribute(GDPR_CONSENT_PROPERTY));



    }



}
