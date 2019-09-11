/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.spyder.security.sampleapp;

import org.apache.ranger.plugin.audit.RangerDefaultAuditHandler;
import org.apache.ranger.plugin.service.RangerBasePlugin;
import org.apache.ranger.plugin.policyengine.RangerAccessResourceImpl;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.policyengine.RangerAccessRequestImpl;
import org.apache.ranger.plugin.policyengine.RangerAccessResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import java.util.Set;

public class RangerWebAuthorizer implements IAuthorizer {

    private static final Log LOG = LogFactory.getLog(SampleApp.class);
    static {
        BasicConfigurator.configure();
    }

    private static volatile RangerBasePlugin plugin = null;

    public RangerWebAuthorizer() {

    }

    public void init() {
        if(plugin == null) {
            synchronized (RangerWebAuthorizer.class) {
                if(plugin == null) {
                    plugin = new RangerBasePlugin("httpservice", "httpservice");

                    plugin.setResultProcessor(new RangerDefaultAuditHandler());

                    plugin.init();
                }
            }
        }
    }

    @Override
    public boolean authorize(String route, String accessType, String user, Set<String> userGroups) {
        RangerAccessResourceImpl resource = new RangerAccessResourceImpl();
        resource.setValue("path", route); // "path" must be a value resource name in servicedef JSON

        RangerAccessRequest request = new RangerAccessRequestImpl(resource, accessType, user, userGroups);

        RangerAccessResult result = plugin.isAccessAllowed(request);

        return result != null && result.getIsAllowed();    
    }
}