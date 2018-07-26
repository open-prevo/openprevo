package ch.prevo.open.encrypted.model;

/**
 * Container to hold the data to encrypt and its signature.
 */
public class SignedEncryptionContainer<T> {

    private T data;
    private byte[] signature;

    SignedEncryptionContainer() {}

    public SignedEncryptionContainer(T data, byte[] signature) {
        this.data = data;
        this.signature = signature;
    }

    public T getData() {
        return data;
    }

    public byte[] getSignature() {
        return signature;
    }
}
