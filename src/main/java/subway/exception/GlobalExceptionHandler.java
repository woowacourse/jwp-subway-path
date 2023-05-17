package subway.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(IllegalAddSectionException.class)
    public ResponseEntity<String> handleIllegalAddSectionException(final IllegalAddSectionException e) {
        return ResponseEntity.badRequest().body(IllegalAddSectionException.MESSAGE);
    }

    @ExceptionHandler(IllegalRemoveSectionException.class)
    public ResponseEntity<String> handleIllegalRemoveSectionException(final IllegalRemoveSectionException e) {
        return ResponseEntity.badRequest().body(IllegalRemoveSectionException.MESSAGE);
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<String> handleEmptyResultDataAccessException(final EmptyResultDataAccessException e) {
        return ResponseEntity.badRequest().body("존재하지 않는 데이터입니다. 확인 후 다시 요청해주세요.");
    }
}
