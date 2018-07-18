package ch.prevo.open.encrypted.model;

import ch.prevo.open.encrypted.services.EncryptedData;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.PublicKey;


class EncryptedCapitalTransferInfo extends EncryptedData<CapitalTransferInformation> {

    private static final String JSON_CHARSET = "UTF-8";

    protected EncryptedCapitalTransferInfo() {
        super();
    }

    public EncryptedCapitalTransferInfo(CapitalTransferInformation info, PublicKey publicEncodingKey) {
        super(info, publicEncodingKey);
    }

    @Override
    protected byte[] toByteArray(CapitalTransferInformation data) throws IOException {
        String json = new ObjectMapper().writeValueAsString(data);
        return json.getBytes(JSON_CHARSET);
    }

    @Override
    protected CapitalTransferInformation fromByteArray(byte[] data) throws IOException {
        return new ObjectMapper().readValue(data, CapitalTransferInformation.class);
    }


}

