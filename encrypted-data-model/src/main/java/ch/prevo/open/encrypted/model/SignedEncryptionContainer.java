package ch.prevo.open.encrypted.model;

/**
 * Container to hold the data to encrypt and its signature.
 */
public class SignedEncryptionContainer<T> {

    private T data;
    private String signatureBase64;

    SignedEncryptionContainer() {}

    public SignedEncryptionContainer(T data, String signatureBase64) {
        this.data = data;
        this.signatureBase64 = signatureBase64;
    }

    public T getData() {
        return data;
    }

    public String getSignatureBase64() {
        return signatureBase64;
    }
}
