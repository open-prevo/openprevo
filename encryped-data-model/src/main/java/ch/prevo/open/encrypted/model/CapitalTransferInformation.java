package ch.prevo.open.encrypted.model;

/**
 * Encrypted information required for the transfer of the retirement capital
 * between the old and the new retirement fund.
 */
public class CapitalTransferInformation {

    private String encryptedPaymentInformation;

    public String getEncryptedPaymentInformation() {
        return encryptedPaymentInformation;
    }

    public void setEncryptedPaymentInformation(String encryptedPaymentInformation) {
        this.encryptedPaymentInformation = encryptedPaymentInformation;
    }
}
