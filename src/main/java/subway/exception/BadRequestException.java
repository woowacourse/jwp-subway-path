package subway.exception;

import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

public class BadRequestException extends MethodArgumentNotValidException {
    /**
     * Constructor for {@link MethodArgumentNotValidException}.
     *
     * @param parameter     the parameter that failed validation
     * @param bindingResult the results of the validation
     */
    public BadRequestException(final MethodParameter parameter, final BindingResult bindingResult) {
        super(parameter, bindingResult);
    }
}
