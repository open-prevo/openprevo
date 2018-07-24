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
package ch.prevo.open.hub;

import ch.prevo.open.hub.match.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

@Component
@Profile("!test")
public class HubScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HubScheduler.class);

    @Inject
    private HubService hubService;

    @Scheduled(initialDelay = 10000, fixedDelay = 120000)
    public void run() {
        LOGGER.info("Start matching task");
        List<Match> matches = hubService.matchAndNotify();
        LOGGER.info("Matching task ended, found {} matches", matches.size());
    }
}
