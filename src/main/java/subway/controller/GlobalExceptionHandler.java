package subway.controller;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.controller.dto.response.ExceptionResponse;
import subway.exception.DuplicatedLineNameException;
import subway.exception.DuplicatedSectionException;
import subway.exception.DuplicatedStationNameException;
import subway.exception.InvalidDirectionException;
import subway.exception.InvalidDistanceException;
import subway.exception.LineNotFoundException;
import subway.exception.LineOrStationNotFoundException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotFoundException;

import java.util.Objects;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DuplicatedStationNameException.class, DuplicatedLineNameException.class, DuplicatedSectionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleDuplicated(IllegalArgumentException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler({StationNotFoundException.class, LineNotFoundException.class,
            LineOrStationNotFoundException.class, SectionNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleNotFound(IllegalArgumentException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidDistanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidDistance(InvalidDistanceException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(InvalidDirectionException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleInvalidDirectionException(InvalidDirectionException e) {
        return new ExceptionResponse(e.getMessage());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        try {
            final String errorMessage = Objects.requireNonNull(e.getRootCause()).getMessage();
            return new ExceptionResponse(errorMessage);
        } catch (NullPointerException ex) {
            return handleRuntimeException();
        }
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        final String errorMessage = e.getFieldErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(" "));
        return new ExceptionResponse(errorMessage);
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionResponse handleRuntimeException() {
        return new ExceptionResponse("서버 내부 오류입니다.");
    }
}
