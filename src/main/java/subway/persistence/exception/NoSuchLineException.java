package subway.persistence.exception;

import java.util.NoSuchElementException;

public class NoSuchLineException extends NoSuchElementException {

    public NoSuchLineException() {
        super("없는 호선입니다");
    }
}
