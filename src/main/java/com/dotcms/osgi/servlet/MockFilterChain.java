package com.dotcms.osgi.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.dotcms.filters.interceptor.Result;

public class MockFilterChain implements javax.servlet.FilterChain {

	
	

	private Result passthrough=Result.SKIP_NO_CHAIN;
	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1) throws IOException, ServletException {
		passthrough=Result.NEXT;
	}
	
	public Result result(){
		return passthrough;
	}

}
