package subway.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import subway.presentation.dto.response.BadResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleGlobalException(GlobalException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BadResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleDatabaseException(DatabaseException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new BadResponse(e.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleBeanValidationException(MethodArgumentNotValidException e) {
        String errorMessage = e.getMessage();
        BindingResult bindingResult = e.getBindingResult();

        if (bindingResult.hasErrors()) {
            errorMessage = bindingResult.getFieldError().getDefaultMessage();
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BadResponse(errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<BadResponse> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BadResponse("예기치 못한 오류가 발생하였습니다."));
    }
}
