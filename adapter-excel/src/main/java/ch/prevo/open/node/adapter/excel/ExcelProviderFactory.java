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
package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;

public class ExcelProviderFactory implements ProviderFactory {

    final ExcelReader excelReader = new ExcelReader();
    final ExcelMatchNotificationListener listener = new ExcelMatchNotificationListener();

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return excelReader;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return excelReader;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return listener;
    }
}
