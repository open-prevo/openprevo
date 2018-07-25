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
package ch.prevo.pakt.provider;

import javax.inject.Inject;

import ch.prevo.open.data.api.FullMatchForTerminationNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.FullMatchForCommencementNotification;
import ch.prevo.open.node.data.provider.MatchNotificationListener;
import ch.prevo.pakt.config.PaktEnvironment;
import ch.prevo.pakt.config.RetirementFundRegistry;
import ch.prevo.pakt.soap.SoapRequest;
import ch.prevo.pakt.soap.SubmitFZLVerwendungRequestPopulator;

@Service
public class PAKTMatchNotificationListenerImpl implements MatchNotificationListener {

	private static final Logger LOG = LoggerFactory.getLogger(PAKTMatchNotificationListenerImpl.class);

	private final PaktEnvironment config;
    private final RetirementFundRegistry retirementFundRegistry;
	private final SoapRequest soapRequest;

    @Inject
	public PAKTMatchNotificationListenerImpl(PaktEnvironment config,
                                             RetirementFundRegistry retirementFundRegistry,
                                             SoapRequest soapRequest) {

	    this.config = config;
	    this.retirementFundRegistry = retirementFundRegistry;
	    this.soapRequest = soapRequest;
    }

	@Override
	public void handleMatchForCommencementNotification(FullMatchForCommencementNotification notification) {
		LOG.info("Handling FullMatchForCommencementNotification notification for OASI [{}]", notification.getEmploymentCommencement().getEmploymentInfo().getOasiNumber());
	}

	@Override
	public void handleMatchForTerminationNotification(FullMatchForTerminationNotification notification) {
		try {
			LOG.info("Submitting FullMatchForTerminationNotification message to PAKT for OASI [{}]", notification.getEmploymentTermination().getEmploymentInfo().getOasiNumber());
			soapRequest.callSubmitFZLVerwendung(config.getServiceBaseUrl(),
					new SubmitFZLVerwendungRequestPopulator(this.getCdMandant(), this.getUserId(),
							notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId(),
							retirementFundRegistry.getByUid(notification.getNewRetirementFundUid()).getIdPartner(),
							notification.getCommencementDate(),
							notification.getEmploymentTermination().getEmploymentInfo().getInternalPersonId()),
					(doc) -> {
						LOG.info("Message was successfuly submitted to PAKT for OASI [{}] and received number [{}]", notification.getEmploymentTermination().getEmploymentInfo().getOasiNumber(), doc.getElementsByTagName("nrMeld").item(0).getTextContent());
					});
		} catch (Exception e) {
			LOG.error("Error by submitting FullMatchForTerminationNotification message to PAKT", e);
			throw new RuntimeException(e);
		}
	}

	private String getUserId() {
		return config.getIdUser();
	}

	private short getCdMandant() {
		return config.getCdMandant();
	}
}