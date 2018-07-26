package ch.prevo.open.encrypted.model;

/**
 * Bundle of encryption key and inital vector.
 */
public class SymmetricKeyBundle {

    private byte[] key;
    private byte[] iv;

    SymmetricKeyBundle() {}

    public SymmetricKeyBundle(byte[] key, byte[] iv) {
        this.key = key;
        this.iv = iv;
    }

    public byte[] getKey() {
        return key;
    }

    public byte[] getIv() {
        return iv;
    }
}
