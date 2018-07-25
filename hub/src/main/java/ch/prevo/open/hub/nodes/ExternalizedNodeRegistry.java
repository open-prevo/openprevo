/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 * 
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 * 
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.hub.nodes;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
@Profile("!docker")
class ExternalizedNodeRegistry implements NodeRegistry {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalizedNodeRegistry.class);

    private final ResourceLoader loader;
    private final ObjectMapper mapper;

    @Value("${open.prevo.hub.config.file}")
    private String configFile;

    private List<NodeConfiguration> nodes = Collections.emptyList();

    @Inject
    public ExternalizedNodeRegistry(ResourceLoader loader) {
        this.loader = loader;
        this.mapper = new ObjectMapper(new YAMLFactory());
    }

    @PostConstruct
    public void init() {
        try {
            final Resource resource = loader.getResource(configFile);
            nodes = mapper.readValue(resource.getInputStream(), new TypeReference<List<NodeConfiguration>>() {});
        } catch (IOException e) {
            LOGGER.warn("Unable to read bootstrap-data from " + configFile, e);
        }
    }

    @Override
    public List<NodeConfiguration> getCurrentNodes() {
        return nodes;
    }
}
