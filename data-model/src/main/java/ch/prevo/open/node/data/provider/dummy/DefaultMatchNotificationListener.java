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
package ch.prevo.open.node.data.provider.dummy;

import java.io.PrintWriter;

import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.error.NotificationException;

public class DefaultMatchNotificationListener implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultMatchNotificationListener.class);

    private final PrintWriter writer = new PrintWriter(System.out);
    private final NotificationWriter notificationWriter = new NotificationWriter();

    @Override
    public void handleMatchForCommencementNotification(FullMatchForCommencementNotification notification) throws NotificationException {
        try {
            notificationWriter.write(writer, notification);
        } catch (Exception e) {
            LOG.error("Could not handle termination match", e);
            throw new NotificationException(e);
        }
    }

    @Override
    public void handleMatchForTerminationNotification(FullMatchForTerminationNotification notification) throws NotificationException {
        try {
            notificationWriter.write(writer, notification);
        } catch (Exception e) {
            LOG.error("Could not handle commencement match", e);
            throw new NotificationException(e);
        }
    }
}
