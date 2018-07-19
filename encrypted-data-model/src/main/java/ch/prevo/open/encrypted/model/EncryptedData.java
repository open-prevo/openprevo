package ch.prevo.open.encrypted.model;

import java.io.Serializable;
import java.util.Objects;

public class EncryptedData implements Serializable {

    private String encryptedDataBase64;
    private String encryptedSymmetricKeyBase64;
    private String ivBase64;

    public EncryptedData() {
    }

    public EncryptedData(String encryptedDataBase64, String encryptedSymmetricKeyBase64, String ivBase64) {
        this.encryptedDataBase64 = encryptedDataBase64;
        this.encryptedSymmetricKeyBase64 = encryptedSymmetricKeyBase64;
        this.ivBase64 = ivBase64;
    }

    public String getEncryptedDataBase64() {
        return encryptedDataBase64;
    }

    public String getEncryptedSymmetricKeyBase64() {
        return encryptedSymmetricKeyBase64;
    }

    public String getIvBase64() {
        return ivBase64;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EncryptedData that = (EncryptedData) o;
        return Objects.equals(encryptedDataBase64, that.encryptedDataBase64) &&
                Objects.equals(encryptedSymmetricKeyBase64, that.encryptedSymmetricKeyBase64);
    }

    @Override
    public int hashCode() {
        return Objects.hash(encryptedDataBase64, encryptedSymmetricKeyBase64);
    }

    @Override
    public String toString() {
        return "EncryptedData{" +
                "encryptedDataBase64='" + encryptedDataBase64 + '\'' +
                ", encryptedSymmetricKeyBase64='" + encryptedSymmetricKeyBase64 + '\'' +
                '}';
    }
}
