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
package ch.prevo.open.node.api;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.prevo.open.encrypted.model.InsurantInformation;
import ch.prevo.open.node.services.EmploymentCommencementService;

@RestController
class EmploymentCommencementController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmploymentCommencementController.class);

    private EmploymentCommencementService employmentCommencementService;

    @Inject
    public EmploymentCommencementController(EmploymentCommencementService employmentCommencementService) {
        this.employmentCommencementService = employmentCommencementService;
    }

    @RequestMapping("/commencement-of-employment")
    public ResponseEntity<SortedSet<InsurantInformation>> getAllEmploymentCommencementData() {
        try {
            Set<InsurantInformation> employmentCommencementData = this.employmentCommencementService.getAllEmploymentCommencementData();
            return ResponseEntity.ok(new TreeSet<>(employmentCommencementData));
        } catch (Exception e) {
            LOGGER.error("Could not load employment start data", e);
            return ResponseEntity.notFound().build();
        }
    }
}
