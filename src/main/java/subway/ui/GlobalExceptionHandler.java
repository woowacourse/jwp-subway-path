package subway.ui;

import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import subway.domain.exception.SubwayResponsibleException;
import subway.dto.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class.getName());

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(SubwayResponsibleException.class)
    public ErrorResponse handleException(SubwayResponsibleException exception) {
        return new ErrorResponse(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleException(Exception exception) {
        logger.error(exception.getMessage());
        logger.error(Arrays.toString(exception.getStackTrace()));
        return new ErrorResponse("internal server error");
    }
}
