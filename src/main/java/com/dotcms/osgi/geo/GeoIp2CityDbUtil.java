package com.dotcms.osgi.geo;

import com.dotcms.osgi.util.BundleConfigProperties;
import com.dotcms.repackage.com.maxmind.geoip2.DatabaseReader;
import com.dotcms.repackage.com.maxmind.geoip2.exception.GeoIp2Exception;
import com.dotcms.repackage.com.maxmind.geoip2.model.CityResponse;
import com.dotcms.repackage.com.maxmind.geoip2.model.DomainResponse;
import com.dotcms.repackage.com.maxmind.geoip2.record.City;
import com.dotcms.repackage.com.maxmind.geoip2.record.Continent;
import com.dotcms.repackage.com.maxmind.geoip2.record.Country;
import com.dotcms.repackage.com.maxmind.geoip2.record.Subdivision;

import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.portlets.rules.conditionlet.Location;
import com.dotmarketing.util.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class GeoIp2CityDbUtil {


    static DatabaseReader reader;
    final String GEO_LIGHT_DB_PAT = "GEO_LIGHT_DB_PATH";
    final InetAddress inetAddress;



    public GeoIp2CityDbUtil(final InetAddress ipAddress) {
        this.inetAddress = ipAddress;
        if (reader == null) {
            try (final InputStream in =
                    GeoIp2CityDbUtil.class.getResourceAsStream(BundleConfigProperties.getProperty("GEO_LIGHT_DB_PATH"))) {

                reader = connectToDatabase(in);
            } catch (Exception e) {
                throw new DotRuntimeException(e);
            }
        }


    }

    public GeoIp2CityDbUtil(final String ipAddress) throws UnknownHostException {
        this(InetAddress.getByName(ipAddress));


    }

    /**
     * Establishes the connection with the IP database. If a previous connection has already been
     * created, it will be closed. Such a scenario would mean that the database file has been
     * updated, so the database reader must be re-built to load the new information.
     * 
     * @param database - The {@link File} reference to the database file.
     * @throws DotRuntimeException If the connection to the GeoIP2 database file cannot be
     *         established.
     */
    private DatabaseReader connectToDatabase(InputStream database) {
        try {

            return new DatabaseReader.Builder(database).build();
        } catch (IOException e) {
            Logger.error(GeoIp2CityDbUtil.class, "Connection to the GeoIP2 database could not be established.");
            throw new DotRuntimeException("Connection to the GeoIP2 database could not be established.", e);
        }
    }


    /**
     * Returns the ISO code of the state, province or region (referred to as "subdivision") the
     * specified IP address belongs to. The ISO code is a one or two-character representation
     * (depending on the country) of the name of the subdivision.
     * 
     * @param ipAddress - The IP address to get information from.
     * @return The ISO code representing the state, province, or region.
     * @throws UnknownHostException If the IP address of a host could not be determined.
     * @throws IOException If the connection to the GeoIP2 service could not be established, or the
     *         result object could not be created.
     * @throws GeoIp2Exception If the IP address is not present in the service database.
     */
    public String getSubdivisionIsoCode() throws UnknownHostException, IOException, GeoIp2Exception {

        CityResponse city = reader.city(inetAddress);
        Subdivision subdivision = city.getMostSpecificSubdivision();
        return subdivision.getIsoCode();
    }

    /**
     * Returns the ISO code of the country the specified IP address belongs to. The ISO code is a
     * two-character representation of the name of the country.
     * 
     * @param ipAddress - The IP address to get information from.
     * @return The ISO code representing the country.
     * @throws UnknownHostException If the IP address of a host could not be determined.
     * @throws IOException If the connection to the GeoIP2 service could not be established, or the
     *         result object could not be created.
     * @throws GeoIp2Exception If the IP address is not present in the service database.
     */
    public String getCountryIsoCode() throws UnknownHostException, IOException, GeoIp2Exception {

        CityResponse city = reader.city(inetAddress);
        Country country = city.getCountry();
        return country.getIsoCode();
    }
    
    public String getCompany() throws UnknownHostException, IOException, GeoIp2Exception {

        DomainResponse res = reader.domain(inetAddress);
        return res.getDomain();

    }
    /**
     * returns an instance of {@code Location} from ip address
     * 
     * @param ipAddress the ip address to represent
     * @return the location
     * @throws IOException if the connection to the GeoIP2 service could not be established, or the
     *         result object could not be created.
     * @throws GeoIp2Exception if the IP address is not present in the service database.
     */
    public Location getLocationByIp(String ipAddress) throws IOException, GeoIp2Exception {

        CityResponse city = reader.city(inetAddress);
        com.dotcms.repackage.com.maxmind.geoip2.record.Location location = city.getLocation();
        return new Location(location.getLatitude(), location.getLongitude());
    }

    /**
     * Returns the name of the city the specified IP address belongs to.
     * 
     * @param ipAddress - The IP address to get information from.
     * @return The city name.
     * @throws UnknownHostException If the IP address of a host could not be determined.
     * @throws IOException If the connection to the GeoIP2 service could not be established, or the
     *         result object could not be created.
     * @throws GeoIp2Exception If the IP address is not present in the service database.
     */
    public String getCityName() throws UnknownHostException, IOException, GeoIp2Exception {
        CityResponse cityResponse = reader.city(inetAddress);
        City city = cityResponse.getCity();
        return city.getName();
    }

    /**
     * Returns the {@link TimeZone} associated with location, as specified by the
     * <a href="http://www.iana.org/time-zones">IANA Time Zone Database</a>. For example:
     * {@code "America/New_York"}.
     * 
     * @param ipAddress - The IP address to get information from.
     * @return The associated {@link TimeZone} object.
     * @throws UnknownHostException If the IP address of a host could not be determined.
     * @throws IOException If the connection to the GeoIP2 service could not be established, or the
     *         result object could not be created.
     * @throws GeoIp2Exception If the IP address is not present in the service database.
     */
    public TimeZone getTimeZone() throws UnknownHostException, IOException, GeoIp2Exception {

        CityResponse city = reader.city(inetAddress);
        String zone = city.getLocation().getTimeZone();
        return TimeZone.getTimeZone(zone);
    }

    public String getContinent() throws UnknownHostException, IOException, GeoIp2Exception {

        Continent con = reader.country(inetAddress).getContinent();
        return con.getName();

    }

    /**
     * Returns the {@link Date} when the client issued the request. This information is obtained by
     * adjusting the server's current date/time with the time zone the user is in.
     * 
     * @param ipAddress - The IP address to get information from.
     * @return The client's current {@link Date}.
     * @throws UnknownHostException If the IP address of a host could not be determined.
     * @throws IOException If the connection to the GeoIP2 service could not be established, or the
     *         result object could not be created.
     * @throws GeoIp2Exception If the IP address is not present in the service database.
     */
    public Calendar getDateTime() throws UnknownHostException, IOException, GeoIp2Exception {
        TimeZone timeZone = getTimeZone();
        Calendar calendar = Calendar.getInstance(timeZone);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        long clientDateTime = new GregorianCalendar(year, month, day, hour, minute, second).getTimeInMillis();
        calendar.setTimeInMillis(clientDateTime);
        return calendar;
    }

}
