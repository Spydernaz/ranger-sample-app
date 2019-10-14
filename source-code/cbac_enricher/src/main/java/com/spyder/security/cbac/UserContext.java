
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

package com.spyder.security.cbac;

import org.apache.ranger.plugin.contextenricher.*;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ranger.plugin.policyengine.RangerAccessRequest;
import org.apache.ranger.plugin.service.RangerBasePlugin;
import org.apache.ranger.plugin.service.RangerAuthContext;
import java.util.Map;
import java.util.Properties;

/**
 *  It This is how it would be used in service definition:
 {
    ... service def
    ...
    "contextEnrichers": [
		{
		 "itemId": 1, "name": "project-provider",
		 "enricher": "org.apache.ranger.plugin.contextenricher.UserContext",
		 "enricherOptions": { "contextName" : "PROJECT", "dataFile":"/etc/ranger/data/userProject.txt"}
		}
 	...
 }

 contextName: is used to specify the name under which the enricher would push value into context.
           For purposes of this example the default value of this parameter, if unspecified is PROJECT.  This default
           can be seen specified in <code>init()</code>.
 dataFile: is the file which contains the lookup data that this particular enricher would use to
           ascertain which value to insert into the context.  For purposes of this example the default value of
           this parameter, if unspecified is /etc/ranger/data/userProject.txt.  This default can be seen specified
           in <code>init()</code>.  Format of lookup data is in the form of standard java properties list.

 @see <a href="http://docs.oracle.com/javase/6/docs/api/java/util/Properties.html#load(java.io.Reader)">Java Properties List</a>
 */
public class UserContext extends RangerAbstractContextEnricher {
	private static final Log LOG = LogFactory.getLog(UserContext.class);

	private String     contextName    = "PROJECT";
	private Properties userProjectMap = null;
	
	@Override
	public void init() {
		LOG.info("==> UserContext.init(" + enricherDef + ")");
		
		super.init();
		
		contextName = getOption("contextName", "PROJECT");
		LOG.info("==> UserContext.contextName ======> (" + contextName + ")");

		


       
        Map<String, RangerBasePlugin> servicePluginMap = RangerBasePlugin.getServicePluginMap();
        RangerBasePlugin plugin = servicePluginMap != null ? servicePluginMap.get(getServiceName()) : null;
        if (plugin != null) {
			RangerAuthContext currentAuthContext = plugin.getCurrentRangerAuthContext();
            if (currentAuthContext != null) {
                currentAuthContext.addOrReplaceRequestContextEnricher(this, null);
            }
        }

	}

     @Override
     public boolean preCleanup() {
		LOG.debug("==> UserContext.preCleanup(" + enricherDef + ")");
        Map<String, RangerBasePlugin> servicePluginMap = RangerBasePlugin.getServicePluginMap();
        RangerBasePlugin plugin = servicePluginMap != null ? servicePluginMap.get(getServiceName()) : null;
        if (plugin != null) {
            RangerAuthContext currentAuthContext = plugin.getCurrentRangerAuthContext();
            if (currentAuthContext != null) {
                Map<RangerContextEnricher, Object> contextEnrichers = currentAuthContext.getRequestContextEnrichers();
                if (contextEnrichers != null) {
                    contextEnrichers.remove(this);
                }
            }
        }
		LOG.debug("<== UserContext.preCleanup(" + enricherDef + ")");

        return true;
    }

	@Override
	public void enrich(RangerAccessRequest request) {
		LOG.info("==> UserContext.enrich(" + request + ")");

		String dataFile = getOption("dataFile", "/ranger-sample-app/source-code/api/context.txt");
		userProjectMap = readProperties(dataFile);
		
		if(request != null && userProjectMap != null) {
			Map<String, Object> context = request.getContext();
			String              project = userProjectMap.getProperty(request.getUser());
	
			if(context != null && !StringUtils.isEmpty(project)) {
				request.getContext().put(request.getUser(), project);
                LOG.info("UserContext.enrich(): added " + request.getUser() + " with value " + project );
			} else {
			
				LOG.warn("UserContext.enrich(): skipping due to unavailable context or project. context=" + context + "; project=" + project);
				
			}
		}

		// request.getContext().put("PROJECT", "nate");
		// LOG.info("==> UserContext.request.getContext(" + request.getContext() + ")");


		// LOG.info("<== UserContext.enrich(" + request + ")");
		
	}
}
