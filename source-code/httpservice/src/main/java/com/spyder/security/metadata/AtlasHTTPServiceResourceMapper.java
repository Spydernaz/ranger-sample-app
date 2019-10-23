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

package com.spyder.security.metadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.BasicConfigurator;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.fs.Path;
import org.apache.ranger.plugin.model.RangerPolicy;
import org.apache.ranger.plugin.model.RangerPolicy.RangerPolicyResource;
import org.apache.ranger.plugin.model.RangerServiceResource;
import org.apache.ranger.tagsync.source.atlasrest.RangerAtlasEntity;
import org.apache.ranger.tagsync.source.atlas.AtlasResourceMapper;

public class AtlasHTTPServiceResourceMapper extends AtlasResourceMapper {
	public static final String ENTITY_TYPE_HTTP_PATH = "http_path2";
	public static final String RANGER_TYPE_HTTP_PATH = "path";
	// public static final String TAGSYNC_ATLAS_NAME_SERVICE_IDENTIFIER = ".nameservice.";
	// public static final String ENTITY_TYPE_HDFS_CLUSTER_AND_NAME_SERVICE_SEPARATOR = "_";

	public static final String ENTITY_ATTRIBUTE_PATH = "path";
	public static final String ENTITY_ATTRIBUTE_CLUSTER_NAME = "clusterName";
	public static final String ENTITY_ATTRIBUTE_NAME_SERVICE_ID = "nameServiceId";

	public static final String[] SUPPORTED_ENTITY_TYPES = { ENTITY_TYPE_HTTP_PATH };

	private static final Log LOG = LogFactory.getLog(AtlasHTTPServiceResourceMapper.class);
    static {
        BasicConfigurator.configure();
    }

	public AtlasHTTPServiceResourceMapper() {
		super("httpservice", SUPPORTED_ENTITY_TYPES);
	}

	@Override
	public RangerServiceResource buildResource(final RangerAtlasEntity entity) throws Exception {

		LOG.info("==> AtlasHTTPService.buildResource(" + entity + ")");

		String qualifiedName = (String)entity.getAttributes().get(AtlasResourceMapper.ENTITY_ATTRIBUTE_QUALIFIED_NAME);
		String nameServiceId = (String)entity.getAttributes().get(ENTITY_ATTRIBUTE_NAME_SERVICE_ID);

		String path          = null;
		String clusterName   = null;

		if (StringUtils.isNotEmpty(qualifiedName)) {
			path = getResourceNameFromQualifiedName(qualifiedName);
			clusterName = getClusterNameFromQualifiedName(qualifiedName);
		}

		if (StringUtils.isEmpty(path)) {
			path = (String) entity.getAttributes().get(ENTITY_ATTRIBUTE_PATH);
		}
		if (StringUtils.isEmpty(path)) {
			LOG.warn("==> AtlasHTTPService.buildResource.pathIsEmpty(" + ENTITY_ATTRIBUTE_QUALIFIED_NAME + ENTITY_ATTRIBUTE_PATH + ")");
			throwExceptionWithMessage("path not found in attribute '" + ENTITY_ATTRIBUTE_QUALIFIED_NAME + "' or '" + ENTITY_ATTRIBUTE_PATH +  "'");
		}

		if (StringUtils.isEmpty(clusterName)) {
			clusterName = (String) entity.getAttributes().get(ENTITY_ATTRIBUTE_CLUSTER_NAME);
		}
		if (StringUtils.isEmpty(clusterName)) {
			clusterName = defaultClusterName;
		}
		if (StringUtils.isEmpty(clusterName)) {
			LOG.warn("==> AtlasHTTPService.buildResource.clusterIsEmpty(" + ENTITY_ATTRIBUTE_QUALIFIED_NAME + ENTITY_ATTRIBUTE_PATH + ")");
			throwExceptionWithMessage("clusterName not found in attribute '" + ENTITY_ATTRIBUTE_QUALIFIED_NAME + "' or '" + ENTITY_ATTRIBUTE_CLUSTER_NAME +  "'");
		}

		String  entityGuid  = entity.getGuid();
		String  serviceName = nameServiceId;
		Boolean isExcludes  = Boolean.FALSE;
		Boolean isRecursive = Boolean.TRUE;

		Path pathObj = new Path( path );

		Map<String, RangerPolicyResource> elements = new HashMap<String, RangerPolicy.RangerPolicyResource>();
		elements.put(RANGER_TYPE_HTTP_PATH, new RangerPolicyResource(pathObj.toUri().getPath(), isExcludes, isRecursive));
		
		
		LOG.warn("==> AtlasHTTPService.buildResource.RangerServiceResourceOutput(" + entityGuid + ", " + serviceName + ", " + elements + ")");

		RangerServiceResource ret = new RangerServiceResource(entityGuid, serviceName, elements);

		return ret;
	}
}
