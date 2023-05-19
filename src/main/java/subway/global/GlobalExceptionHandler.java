package subway.global;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.line.exception.*;
import subway.route.exception.IdenticalStationsException;
import subway.route.exception.InvalidAgeException;
import subway.route.exception.RouteNotFoundException;
import subway.station.exception.NameLengthException;
import subway.station.exception.StationNotFoundException;

import javax.servlet.http.HttpServletRequest;

import static java.util.stream.Collectors.joining;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_MESSAGE_DELIMITER = ",";

    @ExceptionHandler({
            DuplicateStationInLineException.class,
            InvalidDistanceException.class,
            NameLengthException.class,
            DuplicateLineNameException.class,
            IdenticalStationsException.class,
            InvalidAdditionalFareException.class,
            InvalidAgeException.class
    })
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleBadRequest(RuntimeException exception, HttpServletRequest request) {
        return new ErrorDto(exception.getMessage(), request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exception, HttpServletRequest request) {
        final String errorMessage = exception.getFieldErrors()
                                             .stream()
                                             .map(FieldError::getDefaultMessage)
                                             .collect(joining(ERROR_MESSAGE_DELIMITER));

        return new ErrorDto(errorMessage, request.getRequestURI(), HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler({
            SectionNotFoundException.class,
            StationNotFoundException.class,
            LineNotFoundException.class,
            RouteNotFoundException.class
    })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDto handleNotFound(RuntimeException exception, HttpServletRequest request) {
        return new ErrorDto(exception.getMessage(), request.getRequestURI(), HttpStatus.NOT_FOUND.value());
    }
}
