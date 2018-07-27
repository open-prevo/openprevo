package ch.prevo.open.node.services;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.Java6Assertions.assertThat;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ch.prevo.open.data.api.Address;
import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import ch.prevo.open.node.NodeApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {NodeApplication.class})
public class AdapterDataValidationServiceTest {

    private static final String NOT_NULL_VALIDATION_MSG = "must not be null";

    @Inject
    private AdapterDataValidationService adapterDataValidationService;

    private EmploymentInfo validEmploymentInfo;
    private Address validAddress;
    private CapitalTransferInformation validCapitalTransferInformation;

	private Locale defaultLocale;

    @Before
    public final void setUp() {
    	defaultLocale = Locale.getDefault();
    	Locale.setDefault(Locale.ENGLISH);
        validEmploymentInfo = new EmploymentInfo("CHE-109.537.519", "", "756.1234.5678.97", "", LocalDate.now());
        validAddress = new Address("Baslerstrasse 1", "4000", "Basel");
        validCapitalTransferInformation = new CapitalTransferInformation("BKB", "Bank", validAddress, "CH52 0483 5012 3456 7100 0");
    }
    
    @After
    public final void tearDown() {
    	Locale.setDefault(defaultLocale);
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
        assertThat(violations).extracting("propertyPath", "message")
                            .containsExactly(tuple(
                        PathImpl.createPathFromString("employmentInfo.retirementFundUid"),
                                "must match \"CHE-([0-9]{3}\\.){2}[0-9]{3}\""));
    }

    @Test
    public void testValidationOfDate() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setDate(null);

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(inValidTermination);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("employmentInfo.date"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testValidationOfCompletelyWrongOASI() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setOasiNumber("AHD.1234.1234.89");

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(inValidTermination);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("employmentInfo.oasiNumber"), "Invalid OASI number"));
    }

    @Test
    public void testValidationOfOASIChecksum() {
        // given
        EmploymentTermination inValidTermination = new EmploymentTermination(validEmploymentInfo);
        inValidTermination.getEmploymentInfo().setOasiNumber("7568824253347");

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(inValidTermination);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("employmentInfo.oasiNumber"), "Invalid OASI number"));
    }

    @Test
    public void testValidationOfNoCapitalTransferInformation() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).isNotEmpty();
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutName() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setName(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo.name"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutIban() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setIban(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo.iban"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().setAddress(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo.address"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutCityInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setCity(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo.address.city"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutPostalCodeInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setPostalCode(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo.address.postalCode"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithInvalidPostalCodeInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setPostalCode("CH-3000");

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo.address.postalCode"), "must match \"[0-9]{4}\""));
    }

    @Test
    public void testValidationOfCapitalTransferInformationWithoutStreetInAddress() {
        // given
        EmploymentCommencement inValidCommencement = new EmploymentCommencement(validEmploymentInfo, validCapitalTransferInformation);
        inValidCommencement.getCapitalTransferInfo().getAddress().setStreet(null);

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(inValidCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("capitalTransferInfo.address.street"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testEmptyCommencementIsInvalid() {
        // given
        EmploymentCommencement emptyCommencement = new EmploymentCommencement();

        // when
        final Set<ConstraintViolation<EmploymentCommencement>> violations = adapterDataValidationService.getEmploymentCommencementViolations(emptyCommencement);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactlyInAnyOrder(
                        tuple(PathImpl.createPathFromString("employmentInfo"), NOT_NULL_VALIDATION_MSG),
                        tuple(PathImpl.createPathFromString("capitalTransferInfo"), NOT_NULL_VALIDATION_MSG));
    }

    @Test
    public void testEmptyTerminationIsInvalid() {
        // given
        EmploymentTermination emptyTermination = new EmploymentTermination();

        // when
        final Set<ConstraintViolation<EmploymentTermination>> violations = adapterDataValidationService.getEmploymentTerminationViolations(emptyTermination);

        // then
        assertThat(violations).extracting("propertyPath", "message")
                .containsExactly(tuple(PathImpl.createPathFromString("employmentInfo"), NOT_NULL_VALIDATION_MSG));
    }
}
