/*============================================================================*
 * Copyright (c) 2018 - Prevo-System AG and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the Eclipse
 * Public License, v. 2.0 are satisfied: GNU General Public License, version 3
 * with the GNU Classpath Exception which is
 * available at https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-3.0 WITH Classpath-exception-2.0
 *
 * Contributors:
 *     Prevo-System AG - initial API and implementation
 *===========================================================================*/
package ch.prevo.open.node.adapter.excel;

import ch.prevo.open.data.api.Address;
import ch.prevo.open.data.api.CapitalTransferInformation;
import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentInfo;
import ch.prevo.open.data.api.EmploymentTermination;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static ch.prevo.open.node.adapter.excel.ExcelAssertions.assertRowComments;

public class ExcelProviderErrorNotificationTest {

    private static final String MUST_NOT_BE_NULL = "must not be null";
    private static final String PLZ_ERROR = "must match \"[0-9]{4}\"";
    private static final String RETIREMENT_FUND_ERROR = "must match \"CHE-([0-9]{3}\\.){2}[0-9]{3}\"";
    private static final String INVALID_OASI_NUMBER = "Invalid OASI number";

    private ExcelProvider excelProvider;
    private Validator validator;
    private static final String BASE_FILE_NAME = "src/test/resources/retirement-fund-error-test-data";
    private Locale defaultLocale;

    @Before
    public void init() {
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.ENGLISH);

        System.setProperty(ExcelProvider.FILE_PROPERTY, BASE_FILE_NAME + ".xlsx");
        excelProvider = new ExcelProvider();
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @After
    public void tearDown() {
        Locale.setDefault(defaultLocale);
    }

    @Test
    public void notifyCommencementErrorsTest() throws IOException, InvalidFormatException {
        // given
        EmploymentCommencement employmentCommencement = getInvalidEmploymentCommencement();
        Map<EmploymentCommencement, Set<ConstraintViolation<EmploymentCommencement>>> validationErrorMap = validate(employmentCommencement);

        // when
        excelProvider.notifyCommencementErrors(validationErrorMap);

        // then
        String filename = BASE_FILE_NAME + "-error.xlsx";
        new File(filename).deleteOnExit();
        assertRowComments(filename, ExcelConstants.COMMENCEMENTS_LABEL, 3, INVALID_OASI_NUMBER, MUST_NOT_BE_NULL,
                RETIREMENT_FUND_ERROR, PLZ_ERROR, MUST_NOT_BE_NULL);

    }

    @Test
    public void notifyTerminationErrorsTest() throws IOException, InvalidFormatException {
        // given
        EmploymentTermination employmentTermination = getInvalidEmploymentTermination();
        Map<EmploymentTermination, Set<ConstraintViolation<EmploymentTermination>>> validationErrorMap = validate(employmentTermination);

        // when
        excelProvider.notifyTerminationErrors(validationErrorMap);

        // then
        String filename = BASE_FILE_NAME + "-error.xlsx";
        new File(filename).deleteOnExit();
        assertRowComments(filename, ExcelConstants.TERMINATION_LABEL, 3, INVALID_OASI_NUMBER, MUST_NOT_BE_NULL,
                RETIREMENT_FUND_ERROR);

    }

    private EmploymentCommencement getInvalidEmploymentCommencement() {
        EmploymentInfo employmentInfo = new EmploymentInfo("CH-109.740.078", "our-ref-56", "7569678192441", null, null);
        Address add = new Address("Str/Postfach", "PLZ", "Ort");
        CapitalTransferInformation transferInformation = new CapitalTransferInformation("Bâloise-Sammelstiftung für die ausserobligatorische berufliche Vorsorge", "Zusatzname", add, null);
        return new EmploymentCommencement(employmentInfo, transferInformation);
    }

    private EmploymentTermination getInvalidEmploymentTermination() {
        EmploymentInfo employmentInfo = new EmploymentInfo("CHE-123.123.12", null, "1341234328313", null, null);
        return new EmploymentTermination(employmentInfo);
    }

    private <T> Map<T, Set<ConstraintViolation<T>>> validate(T objectToValidate) {
        Set<ConstraintViolation<T>> constraints = validator.validate(objectToValidate);

        Map<T, Set<ConstraintViolation<T>>> errors = new HashMap<>();
        errors.put(objectToValidate, constraints);
        return errors;
    }
}
