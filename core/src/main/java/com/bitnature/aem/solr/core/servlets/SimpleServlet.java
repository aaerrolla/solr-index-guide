/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.bitnature.aem.solr.core.servlets;

import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.apache.solr.common.SolrInputDocument;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Collection;

/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = { Servlet.class })
@SlingServletResourceTypes(
        resourceTypes="aem-solr/components/page",
        methods=HttpConstants.METHOD_GET,
        extensions="txt")
@ServiceDescription("Simple Demo Servlet")
public class SimpleServlet extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(SimpleServlet.class);

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
        updateIndex(resource);
        resp.setContentType("text/plain");
        resp.getWriter().write("Title = " + resource.getValueMap().get(JcrConstants.JCR_TITLE));
    }

    private void updateIndex(Resource resource) {

        SolrInputDocument doc = resource.adaptTo(SolrInputDocument.class);
        if (doc != null) {
            Collection<String> fieldNames = doc.getFieldNames();
            if (fieldNames != null) {
                LOG.debug("updateIndex : Fields in Doc {}", fieldNames);
            } else {
                LOG.debug("updateIndex : doc.getFieldName returned null");
            }
        } else {
            LOG.debug("updateIndex : doc is null");
        }
    }
}
