package ch.prevo.open.encrypted.services;

/**
 * Used if an invalid signature for decryption is provided.
 */
public class InvalidSignatureException extends RuntimeException {

    public InvalidSignatureException(String message) {
        super(message);
    }
}
