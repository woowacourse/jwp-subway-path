package subway.exception;

import org.springframework.dao.DataAccessException;

public class DatabaseException extends DataAccessException {
    public DatabaseException(String message) {
        super(message);
    }
}
