package subway.config;

import org.springframework.dao.DataAccessException;

public class TestDataAccessException extends DataAccessException {
    public TestDataAccessException(String msg) {
        super(msg);
    }

    public TestDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
