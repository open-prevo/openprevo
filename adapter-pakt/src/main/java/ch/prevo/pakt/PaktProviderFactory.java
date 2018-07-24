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
package ch.prevo.pakt;

import org.springframework.context.ApplicationContext;

import ch.prevo.open.node.data.provider.EmploymentCommencementProvider;
import ch.prevo.open.node.data.provider.EmploymentTerminationProvider;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.ProviderFactory;

public class PaktProviderFactory implements ProviderFactory {

    private final EmploymentCommencementProvider employmentCommencementProvider;
    private final EmploymentTerminationProvider employmentTerminationProvider;
    private final MatchNotificationListener matchNotificationListener;


    public PaktProviderFactory() {
        final ApplicationContext context = PaktApplicationContext.INSTANCE.getContext();
        this.employmentCommencementProvider = context.getBean(EmploymentCommencementProvider.class);
        this.employmentTerminationProvider = context.getBean(EmploymentTerminationProvider.class);
        this.matchNotificationListener = context.getBean(MatchNotificationListener.class);
    }

    @Override
    public EmploymentCommencementProvider getEmploymentCommencementProvider() {
        return employmentCommencementProvider;
    }

    @Override
    public EmploymentTerminationProvider getEmploymentTerminationProvider() {
        return employmentTerminationProvider;
    }

    @Override
    public MatchNotificationListener getMatchNotificationListener() {
        return matchNotificationListener;
    }
}