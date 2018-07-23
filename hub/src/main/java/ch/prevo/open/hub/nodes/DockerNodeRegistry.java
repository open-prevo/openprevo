/*******************************************************************************
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
 ******************************************************************************/
package ch.prevo.open.hub.nodes;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * All node instances currently known by the hub, currently only a mock implementation.
 */
@Service
@Profile("docker")
class DockerNodeRegistry implements NodeRegistry {

    static final NodeConfiguration BALOISE_NODE =
            new NodeConfiguration("http://node_baloise:8080",
                    "CHE-109.740.084-Baloise-Sammelstiftung", "CHE-109.740.084-Baloise-Sammelstiftung 2");
    static final NodeConfiguration HELVETIA_NODE =
            new NodeConfiguration("http://node_helvetia:8080",
                    "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung", "CHE-109.537.488-Helvetia-Prisma-Sammelstiftung 2");

    @Override
    public List<NodeConfiguration> getCurrentNodes() {
        return Arrays.asList(BALOISE_NODE, HELVETIA_NODE);
    }
}
