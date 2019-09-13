package com.spyder.security.services;

import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import org.apache.ranger.plugin.client.BaseClient;
import org.apache.ranger.plugin.service.RangerBaseService;
import org.apache.ranger.plugin.service.ResourceLookupContext;

/**
 * Hello world!
 *
 */
public class RangerServiceHTTP extends RangerBaseService {
    
    private static final Log LOG = LogFactory.getLog(RangerServiceHTTP.class);
    static {
        BasicConfigurator.configure();
    }


    @Override
    public Map<String, Object> validateConfig() throws Exception {
        if (LOG.isDebugEnabled()) {
                LOG.debug("==> RangerServiceHTTP.validateConfig()");
        }

        Map<String, Object> ret = new HashMap<>();

        BaseClient.generateResponseDataMap(true, "ConnectionTest Successful", "ConnectionTest Successful", null, null, ret);

        if (LOG.isDebugEnabled()) {
                LOG.debug("<== RangerServiceAtlas.validateConfig(): " + ret );
        }

        return ret;
    }

    @Override
    public List<String> lookupResource(ResourceLookupContext context)throws Exception {
        if (LOG.isDebugEnabled()) {
                LOG.debug("==> RangerServiceHTTP.lookupResource(" + context + ")");
        }

        // TODO: Get the list of routes from somewhere... maybe a /discovery route?
        List<String> ret = Arrays.asList("/", "/hello");

        if (LOG.isDebugEnabled()) {
                LOG.debug("<== RangerServiceAtlas.lookupResource("+ context + "): " + ret);
        }

        return ret;
    }


}
