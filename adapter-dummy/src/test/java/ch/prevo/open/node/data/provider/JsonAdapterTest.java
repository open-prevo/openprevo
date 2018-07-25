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
package ch.prevo.open.node.data.provider;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.EmploymentCommencement;

public class JsonAdapterTest {

    private JsonAdapter jsonAdapter;

    @Before
    public void setUp() {
        this.jsonAdapter = new JsonAdapter("classpath:employment-commencement-test.json", "classpath:employment-termination-test.json");
    }

    @Test
    public void getEmploymentCommencementInformation() {
        List<EmploymentCommencement> employmentTerminationInformation = jsonAdapter.getEmploymentCommencements();
        assertThat(employmentTerminationInformation).hasSize(3);
    }

    @Test
    public void getEmploymentTerminationInformation() {
        List<EmploymentTermination> employmentTerminationInformation = jsonAdapter.getEmploymentTerminations();
        assertThat(employmentTerminationInformation).hasSize(3);
    }
}
