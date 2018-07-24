package ch.prevo.open.node.services;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.encrypted.model.Address;
import ch.prevo.open.encrypted.model.CapitalTransferInformation;
import ch.prevo.open.node.NodeApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import java.time.LocalDate;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeApplication.class})
public class AdapterDataValidationServiceTest {

    @Inject
    private AdapterDataValidationService adapterDataValidationService;

    private EmploymentInfo validEmploymentInfo;
    private Address validAddress;
    private CapitalTransferInformation validCapitalTransferInformation;

    @Before
    public final void setUp() {
        validEmploymentInfo = new EmploymentInfo("CHE-109.537.519", "", "756.1234.5678.97", "", LocalDate.now());
        validAddress = new Address("Baslerstrasse 1", "4000", "Basel");
        validCapitalTransferInformation = new CapitalTransferInformation("BKB", "Bank", validAddress, "CH52 0483 5012 3456 7100 0");
    }

    @Test
    public void testCorrectCommencementIsValid() {
        // given
        EmploymentCommencement validCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(validCommencement);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    public void testCorrectTerminationIsValid() {
        // given
        EmploymentTermination validTermination = new EmploymentTermination(validEmploymentInfo);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentTermination(validTermination);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    public void testValidationOfRetirementFundUID() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setRetirementFundUid("756.1234.5678.98");

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentTermination(inValidTermination);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfDate() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setDate(null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentTermination(inValidTermination);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCompletelyWrongOASI() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setOasiNumber("AHD.1234.1234.89");

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentTermination(inValidTermination);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfOASIChecksum() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setOasiNumber("AHD.1234.1234.89");

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentTermination(inValidTermination);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfNoCapitalTransferInformation() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutName() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setName(null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutIban() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setIban(null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setAddress(null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutCityInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setCity(null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutPostalCodeInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setPostalCode(null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithInvalidPostalCodeInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setPostalCode("CH-3000");

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutStreetInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setStreet(null);

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(inValidCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testEmptyCommencementIsInvalid() {
        // given
        EmploymentCommencement emptyCommencement = new EmploymentCommencement();

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentCommencement(emptyCommencement);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    public void testEmptyTerminationIsInvalid() {
        // given
        EmploymentTermination emptyTermination = new EmploymentTermination();

        // when
        boolean isValid = adapterDataValidationService.isValidEmploymentTermination(emptyTermination);

        // then
        assertThat(isValid).isFalse();
    }
}
