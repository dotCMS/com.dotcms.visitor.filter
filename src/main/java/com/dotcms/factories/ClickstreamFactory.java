package com.dotcms.factories;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pmw.tinylog.Logger;

import com.dotmarketing.beans.Clickstream;
import com.dotmarketing.beans.Clickstream404;
import com.dotmarketing.beans.Host;
import com.dotmarketing.business.DotStateException;
import com.dotmarketing.exception.DotDataException;
import com.dotcms.osgi.util.VisitorLogger;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.common.collect.Lists;

/**
 * Provides an entry point for interacting with {@link Clickstream} objects. Each
 * {@link Clickstream} object represents a visit from a given user to a site, and allow to generate
 * site statistics regarding the most-visited pages, site visits, etc. that can be seen in the
 * Dashboard portlet.
 * 
 * @author root
 * @version 1.0
 * @since Mar 22, 2012
 *
 */
public class ClickstreamFactory {


  {
    System.err.println("ClickstreamFactory Orverride");
    
    
  }

  static VisitorLogger logger = new VisitorLogger();
  public static final String CLICKSTREAM_SESSION_ATTR_KEY = "clickstream";

  /**
   * Adds a new request to the stream of clicks. The HttpServletRequest is converted to a
   * ClickstreamRequest object and added to the clickstream.
   *
   * @param request - The servlet request to be added to the clickstream.
   * @throws DotDataException An error occurred when interacting with the database.
   */
  public static Clickstream addRequest(final HttpServletRequest request, final HttpServletResponse response, Host host)
      throws DotDataException {
    Logger.info("adding request");

    try {
      logger.log(request, response);
    } catch (JsonProcessingException e) {
      Logger.info(e);
    }


    return null;
  }

  /**
   * This method forces a clickstream save
   *
   * @param stream
   */
  public static void flushClickStream(Clickstream stream) {

    Logger.info("flushClickStream()");
  }

  public static void save(Clickstream clickstream) {
    Logger.info("saveClickstream()");
  }

  public static Clickstream getClickstream(String clickstreamId) {
    Logger.info("getClickstream()");
    return new Clickstream();

  }


  public static List<Clickstream> getClickstreamsByCookieId(String cookieId) {
    return Lists.newArrayList();

  }

  /**
   * This method sets the user for the current clickstream on login.
   *
   * @param userId the
   * @param request
   */

  public static void setClickStreamUser(String userId, HttpServletRequest request) {
    Logger.info("setClickStreamUser()");

  }

  public static void add404Request(HttpServletRequest request, HttpServletResponse response, Host host)
      throws DotStateException, DotDataException {
    Logger.info("add404Request()");


  }

  public static void save404(Clickstream404 clickstream404) {
    Logger.info("save404()");
  }

}
