/* Copyright (c) 2001 - 2011 TOPP - www.openplans.org. All rights reserved.
 * This code is licensed under the GPL 2.0 license, available at the root
 * application directory.
 */

package org.geoserver.security.filter;

import org.geoserver.config.util.XStreamPersister;
import org.geoserver.security.config.SecurityNamedServiceConfig;

/**
 * Security provider for a security interceptor fitler
 * 
 * @author mcr
 */
public class GeoServerSecurityInterceptorProvider extends AbstractFilterProvider {

    @Override
    public void configure(XStreamPersister xp) {
        super.configure(xp);
        xp.getXStream().alias("securityInterceptor", GeoServerSecurityInterceptorFilter.class);
    }

    @Override
    public Class<? extends GeoServerSecurityFilter> getFilterClass() {
        return GeoServerSecurityInterceptorFilter.class;
    }

    @Override
    public GeoServerSecurityFilter createFilter(SecurityNamedServiceConfig config) {
        return new GeoServerSecurityInterceptorFilter();
    }

}
