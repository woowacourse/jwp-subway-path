package subway.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import subway.validation.annotation.EndsWith;

public class EndsWithValidator implements ConstraintValidator<EndsWith, String> {
    private String suffix;

    @Override
    public void initialize(EndsWith constraintAnnotation) {
        suffix = constraintAnnotation.suffix();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value != null && value.endsWith(suffix);
    }
}
