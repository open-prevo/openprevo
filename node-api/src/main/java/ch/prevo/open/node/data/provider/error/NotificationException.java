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
package ch.prevo.open.node.data.provider.error;

/**
 * Exception that occurred during the notification process.
 * Indicates that the adapter could not properly handle the notification.
 *
 * This exception should only be thrown it the adapter wants to be notified again for the same match later.
 */
public class NotificationException extends Exception {
    private static final long serialVersionUID = 2968936416684363225L;

    public NotificationException(String message) {
        super(message);
    }

    public NotificationException(Exception e) {
        super(e);
    }
    
    public NotificationException(String message, Throwable cause) {
        super(message, cause);
    }
}
