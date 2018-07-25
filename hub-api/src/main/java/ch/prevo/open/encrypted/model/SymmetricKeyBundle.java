package ch.prevo.open.encrypted.model;

/**
 * Bundle of encryption key and inital vector.
 */
public class SymmetricKeyBundle {

    private String key;
    private String iv;

    SymmetricKeyBundle() {}

    public SymmetricKeyBundle(String key, String iv) {
        this.key = key;
        this.iv = iv;
    }

    public String getKey() {
        return key;
    }

    public String getIv() {
        return iv;
    }
}
