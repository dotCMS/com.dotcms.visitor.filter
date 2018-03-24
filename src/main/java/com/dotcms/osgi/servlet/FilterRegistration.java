package com.dotcms.osgi.servlet;

import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dotcms.filters.interceptor.Result;
import com.dotcms.filters.interceptor.WebInterceptor;
import com.dotcms.repackage.org.apache.commons.collections.iterators.IteratorEnumeration;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.util.Config;

public class FilterRegistration implements WebInterceptor {

  private static final long serialVersionUID = 1L;
  final Filter filter;
	final String[] paths;
	final Map<String, String> params;
	final FilterConfig config;

	public FilterRegistration(Filter filter, String... paths) {
		this(filter, Collections.emptyMap(), paths);
	}

	public FilterRegistration(Filter filter, Map<String, String> params, String... paths) {
		this.filter = filter;
		this.paths = paths;
		this.params = params;

		this.config = new FilterConfig() {

			@Override
			public ServletContext getServletContext() {
				return Config.CONTEXT;
			}

			@Override
			public Enumeration<String> getInitParameterNames() {
				return new IteratorEnumeration(params.keySet().iterator());

			}

			@Override
			public String getInitParameter(String arg0) {
				return params.get(arg0);
			}

			@Override
			public String getFilterName() {
				return filter.getClass().getName();
			}
		};

	}

	final @Override public void init() {

		try {
			filter.init(config);
		} catch (ServletException e) {

			e.printStackTrace();
		}
	}

	@Override
	public Result intercept(HttpServletRequest req, HttpServletResponse res) throws IOException {
		try {
			MockFilterChain mockChain = new MockFilterChain();
			filter.doFilter(req, res, mockChain);
			return mockChain.result();
		} catch (ServletException e) {
			throw new DotRuntimeException(e);
		}

	}

	@Override
	public void destroy() {
		filter.destroy();
	}
	@Override
	public String[] getFilters() {
		return paths;
	}
}