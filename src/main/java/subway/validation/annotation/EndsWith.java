package subway.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;
import subway.validation.EndsWithValidator;

@Documented
@Constraint(validatedBy = {EndsWithValidator.class})
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EndsWith {
    String message() default "'{suffix}' 문자열로 끝나야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    String suffix();
}
