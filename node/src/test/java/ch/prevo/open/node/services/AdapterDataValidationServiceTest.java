package ch.prevo.open.node.services;

import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.data.api.Address;
import ch.prevo.open.node.NodeApplication;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import java.time.LocalDate;
import java.util.Set;

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
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(validCommencement);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testCorrectTerminationIsValid() {
        // given
        EmploymentTermination validTermination = new EmploymentTermination(validEmploymentInfo);

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(validTermination);

        // then
        assertThat(violations).isEmpty();
    }

    @Test
    public void testValidationOfRetirementFundUID() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setRetirementFundUid("756.1234.5678.98");

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(inValidTermination);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfDate() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setDate(null);

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(inValidTermination);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCompletelyWrongOASI() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setOasiNumber("AHD.1234.1234.89");

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(inValidTermination);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfOASIChecksum() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setOasiNumber("AHD.1234.1234.89");

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(inValidTermination);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfNoCapitalTransferInformation() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutName() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setName(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutIban() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setIban(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setAddress(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutCityInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setCity(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutPostalCodeInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setPostalCode(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithInvalidPostalCodeInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setPostalCode("CH-3000");

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutStreetInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setStreet(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testEmptyCommencementIsInvalid() {
        // given
        EmploymentCommencement emptyCommencement = new EmploymentCommencement();

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(emptyCommencement);

        // then
        assertThat(violations).isNotEmpty();
    }

    @Test
    public void testEmptyTerminationIsInvalid() {
        // given
        EmploymentTermination emptyTermination = new EmploymentTermination();

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(emptyTermination);

        // then
        assertThat(violations).isNotEmpty();
    }
}
