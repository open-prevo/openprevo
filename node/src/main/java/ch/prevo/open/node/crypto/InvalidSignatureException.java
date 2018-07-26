package ch.prevo.open.node.crypto;

/**
 * Used if an invalid signature for decryption is provided.
 */
public class InvalidSignatureException extends RuntimeException {

    public InvalidSignatureException(String message) {
        super(message);
    }
}
