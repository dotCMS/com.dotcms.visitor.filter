package com.dotcms.osgi.viewtool;

import org.apache.velocity.tools.view.context.ViewContext;
import org.apache.velocity.tools.view.servlet.ServletToolInfo;

/**
 * Visitor Metric Tool
 * @author will
 *
 */
public class VisitorMetricInfo extends ServletToolInfo {

    

    @Override
    public String getKey () {
        return "VisitorMetricTool";
    }

    @Override
    public String getScope () {
        return ViewContext.RESPONSE;
    }

    @Override
    public String getClassname () {
        return VisitorMetricTool.class.getName();
    }

    @Override
    public Object getInstance ( Object initData ) {

        VisitorMetricTool viewTool = new VisitorMetricTool();
        viewTool.init( initData );

        setScope( ViewContext.RESPONSE );

        return viewTool;
    }

}