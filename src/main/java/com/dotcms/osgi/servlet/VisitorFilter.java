
package com.dotcms.osgi.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dotcms.osgi.util.VisitorLogger;

public class VisitorFilter implements Filter {
  

    VisitorLogger logger=new VisitorLogger();
    public void init(FilterConfig config) throws ServletException {
        System.out.println("visitor logger started:");


    }

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {
      
      
      
        chain.doFilter(req, res);
      
      
        final HttpServletRequest request = (HttpServletRequest) req;

        final HttpServletResponse response = (HttpServletResponse) res;

        
        
        
        
        
        logger.log(request, response);

    }

    public void destroy() {
        System.out.println("visitor logger stopped");
    }







}
