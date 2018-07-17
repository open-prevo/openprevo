package ch.prevo.open.node.services;

import ch.prevo.open.encrypted.services.Cryptography;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CryptographyTest {

    @Test
    public void hashFunctionShouldIgnoreDots() {
        // given
        final String oasiNumber1 = "756.3412.8844.97";
        final String oasiNumber2 = "7563412884497";

        // when
        final String hash1 = Cryptography.digestOasiNumber(oasiNumber1);
        final String hash2 = Cryptography.digestOasiNumber(oasiNumber2);

        // then
        assertThat(hash1).isEqualTo(hash2);
    }

}
