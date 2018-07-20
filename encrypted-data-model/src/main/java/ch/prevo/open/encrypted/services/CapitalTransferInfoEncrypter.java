package ch.prevo.open.encrypted.services;

import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class CapitalTransferInfoEncrypter extends DataEncrypter<CapitalTransferInformation> {

    private static final String JSON_CHARSET = "UTF-8";

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
