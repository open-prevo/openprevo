package ch.prevo.open.node.services;

import java.util.Set;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ch.prevo.open.data.api.EmploymentCommencement;
import ch.prevo.open.data.api.EmploymentTermination;

@Service
public class AdapterDataValidationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmploymentCommencementService.class);

    private final Validator validator;

    @Inject
    public AdapterDataValidationService(Validator validator) {
        this.validator = validator;
    }

    public boolean isValidEmploymentCommencement(EmploymentCommencement employmentCommencement) {
        Set<ConstraintViolation<EmploymentCommencement>> validate = validator.validate(employmentCommencement);

        if (!validate.isEmpty()) {
            LOGGER.info("Invalid commencement found in provided data, error: \n{}", validate);
        }

        return validate.isEmpty();
    }

    public boolean isValidEmploymentTermination(EmploymentTermination employmentTermination) {
        Set<ConstraintViolation<EmploymentTermination>> validate = validator.validate(employmentTermination);

        if (!validate.isEmpty()) {
            LOGGER.info("Invalid termination found in provided data, error: \n{}", validate);
        }

        return validate.isEmpty();
    }

}
