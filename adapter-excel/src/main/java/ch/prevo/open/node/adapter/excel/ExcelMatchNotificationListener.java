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
package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.FullCommencementNotification;
import ch.prevo.open.data.api.FullTerminationNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.open.node.data.provider.error.NotificationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;

public class ExcelMatchNotificationListener implements MatchNotificationListener {

    private static final Logger LOG = LoggerFactory.getLogger(ExcelMatchNotificationListener.class);

    private static final String FILE_PROPERTY = "node.adapter.excel.out.file";
    private static final String FALLBACK_FILE = "retirement-fund-out-data";
    private static final String FILE_NAME_FORMAT = "%1$s_%2$tY-%2$tm-%2$td_%2$tH-%2$tM-%2$tS.%2$tL.xlsx";

    @Override
    public void handleTerminationMatch(FullTerminationNotification notification) throws NotificationException {
        final String filename = getFilename();

        try (final TerminationNotificationWriter writer = new TerminationNotificationWriter(filename)) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification + ") to Excel-file", e);
            throw new NotificationException(e);
        }
    }

    @Override
    public void handleCommencementMatch(FullCommencementNotification notification) throws NotificationException {
        final String filename = getFilename();

        try (final CommencementNotificationWriter writer = new CommencementNotificationWriter(filename)) {
            writer.append(notification);
        } catch (IOException e) {
            LOG.error("Exception while trying to write notification (" + notification + ") to Excel-file", e);
            throw new NotificationException(e);
        }
    }

    private static String getFilename() {
        return String.format(FILE_NAME_FORMAT, System.getProperty(FILE_PROPERTY, FALLBACK_FILE), LocalDateTime.now());
    }
}
