package subway.exception;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {


    private final Logger log = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAll(final Exception e) {
        log.error("Ex : ", e);
        return ResponseEntity.internalServerError()
                .body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final ApplicationException e) {
        return ResponseEntity.status(e.status())
                .body(new ExceptionResponse(e.getMessage()));
    }

//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<ExceptionResponse> handleBadRequest(final IllegalArgumentException e) {
//        return ResponseEntity.status(BAD_REQUEST)
//                .body(new ExceptionResponse(e.getMessage()));
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        String exceptionMessage = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(joining(System.lineSeparator()));
        return ResponseEntity.status(UNPROCESSABLE_ENTITY)
                .body(new ExceptionResponse(exceptionMessage));
    }

    public static class ExceptionResponse {
        private final String message;

        public ExceptionResponse(final String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
