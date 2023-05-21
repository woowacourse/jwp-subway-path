package subway.ui;

import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.exception.IllegalDistanceException;
import subway.exception.IllegalFareException;
import subway.exception.IllegalLineException;
import subway.exception.IllegalSectionException;
import subway.exception.IllegalStationException;
import subway.exception.LineNotFoundException;
import subway.exception.StationNotFoundException;
import subway.ui.dto.exception.ExceptionResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleRuntimeException(RuntimeException e) {
        log.info(e.getMessage(), e);
        return ResponseEntity.internalServerError().body(new ExceptionResponse("서버에 오류가 발생했습니다."));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String exceptionMessage = e.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        return ResponseEntity.badRequest().body(new ExceptionResponse(exceptionMessage));
    }

    @ExceptionHandler({IllegalSectionException.class, IllegalDistanceException.class, IllegalStationException.class,
            IllegalLineException.class, IllegalFareException.class})
    public ResponseEntity<ExceptionResponse> handleIllegalException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({StationNotFoundException.class, LineNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(RuntimeException e) {
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ExceptionResponse> handleRequiredParamException(MissingServletRequestParameterException e) {
        String parameterName = e.getParameterName();
        String message = parameterName + "는 비어있을 수 없습니다.";
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<ExceptionResponse> handleRequiredPathVariableException(MissingPathVariableException e) {
        String pathVariableName = e.getVariableName();
        String message = pathVariableName + "는 비어있을 수 없습니다.";
        return ResponseEntity.badRequest().body(new ExceptionResponse(message));
    }
}
