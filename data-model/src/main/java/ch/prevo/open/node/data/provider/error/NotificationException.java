package ch.prevo.open.node.data.provider.error;

/**
 * Exception that occurred during the notification process.
 * Indicates that the adapter could not properly handle the notification.
 *
 * This exception should only be thrown it the adapter wants to be notified again for the same match later.
 */
public class NotificationException extends Exception {

    public NotificationException(Exception e) {
        super(e);
    }
}
