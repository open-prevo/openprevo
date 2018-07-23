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
package org.example.prevo.open.adapter.dao;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import org.example.prevo.open.adapter.repository.EmploymentCommencementRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmploymentCommencementDAO implements EmploymentCommencementProvider {

    private final EmploymentCommencementRepository repository;
    private final ModelMapper mapper;

    @Inject
    public EmploymentCommencementDAO(EmploymentCommencementRepository repository) {
        this.repository = repository;
        this.mapper = new ModelMapper();
    }

    @Override
    public List<EmploymentCommencement> getEmploymentCommencements() {
        return repository.findAll().stream()
                .map(dto -> mapper.map(dto, EmploymentCommencement.class))
                .collect(Collectors.toList());
    }

}
