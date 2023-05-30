package subway.ui.dto.request;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ValidationTest {

    protected final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    protected final Validator validator = factory.getValidator();

}
