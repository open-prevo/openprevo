package ch.prevo.open.data.api.validation;

import java.util.stream.IntStream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Validator for OASI numbers according to the following explanation:
 *
 * @see <a href="https://www.admin.ch/opc/de/classified-compilation/20071554/index.html#app1ahref1">https://www.admin.ch</a>
 */
class OASINumberValidator implements ConstraintValidator<OASI, String> {

    @Override
    public void initialize(OASI constraintAnnotation) {
    }

    @Override
    public boolean isValid(String oasiNumber, ConstraintValidatorContext context) {

        if (oasiNumber != null) {
            final String normalizedOasiNumber = oasiNumber.trim().replace(".", "");
            final int checksum = getIntValue(normalizedOasiNumber, normalizedOasiNumber.length() - 1);

            // verify the normalized version is 13 numbers (not allowed to contain any character)
            if (normalizedOasiNumber.length() == 13 && normalizedOasiNumber.matches("[0-9]+")) {
                int sum = IntStream.range(0, normalizedOasiNumber.length() - 1)
                        .map(index -> getMultiplier(index) * getIntValue(normalizedOasiNumber, index))
                        .sum();

                return (Math.ceil(sum / 10.0) * 10) - sum == checksum;
            }
        }
        return false;
    }

    private int getMultiplier(int index) {
        return index % 2 == 0 ? 1 : 3;
    }

    private int getIntValue(String normalizedOasiNumber, int index) {
        return Character.getNumericValue(normalizedOasiNumber.charAt(index));
    }
}
