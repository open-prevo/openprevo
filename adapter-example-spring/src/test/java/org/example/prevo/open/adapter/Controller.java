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
package org.example.prevo.open.adapter;

import java.util.List;

import javax.inject.Inject;

import org.example.prevo.open.adapter.dao.EmploymentCommencementDAO;
import org.example.prevo.open.adapter.dao.EmploymentTerminationDAO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentTermination;

@RestController
public class Controller {

    private final EmploymentCommencementDAO employmentCommencementDAO;
    private final EmploymentTerminationDAO employmentTerminationDAO;

    @Inject
    public Controller(EmploymentCommencementDAO employmentCommencementDAO, EmploymentTerminationDAO employmentTerminationDAO) {
        this.employmentCommencementDAO = employmentCommencementDAO;
        this.employmentTerminationDAO = employmentTerminationDAO;
    }

    @RequestMapping("/commencement-of-employment")
    public ResponseEntity<List<EmploymentCommencement>> getAllEmploymentCommencementData() {
        return ResponseEntity.ok(employmentCommencementDAO.getEmploymentCommencements());
    }

    @RequestMapping("/termination-of-employment")
    public ResponseEntity<List<EmploymentTermination>> getAllEmploymentTerminationData() {
        return ResponseEntity.ok(employmentTerminationDAO.getEmploymentTerminations());
    }
}
