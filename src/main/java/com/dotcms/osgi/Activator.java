/*
 * Licensed to the Apache Software Foundation (ASF) under one or more contributor license
 * agreements. See the NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The ASF licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.dotcms.osgi;

import org.osgi.framework.BundleContext;

import com.dotcms.osgi.servlet.FilterRegistration;
import com.dotcms.osgi.servlet.VisitorFilter;
import com.dotcms.osgi.util.FilterOrder;
import com.dotcms.osgi.util.TomcatServletFilterUtil;
import com.dotmarketing.osgi.GenericBundleActivator;

public final class Activator extends GenericBundleActivator {

  final static String FILTER_NAME = "VisitorLoggerFilter";


  private FilterRegistration filterReg = new FilterRegistration(new VisitorFilter(), "/");

  @Override
  public void start(BundleContext context) throws Exception {


    // putting this filter last becuase the CMSFilter does not interact with the back end
    // urls
    new TomcatServletFilterUtil().addFilter(FILTER_NAME, new VisitorFilter(), FilterOrder.FIRST, "*");


    // Initializing services...
    // initializeServices( context );

    // Registering the ViewTool service
    // registerViewToolService( context, new VisitorMetricInfo() );
    /*
     * final FilterWebInterceptorProvider filterWebInterceptorProvider =
     * FilterWebInterceptorProvider.getInstance(Config.CONTEXT); final WebInterceptorDelegate delegate =
     * filterWebInterceptorProvider.getDelegate(AutoLoginFilter.class);
     * 
     * delegate.addFirst(filterReg);
     */

  }

  @Override
  public void stop(BundleContext context) throws Exception {

    new TomcatServletFilterUtil().removeFilter(FILTER_NAME);
    // unregisterViewToolServices();
    /*
     * final FilterWebInterceptorProvider filterWebInterceptorProvider =
     * FilterWebInterceptorProvider.getInstance(Config.CONTEXT); final WebInterceptorDelegate delegate =
     * filterWebInterceptorProvider.getDelegate(AutoLoginFilter.class); if (null != delegate) {
     * delegate.remove(filterReg.getName(), true); }
     */



  }
}
