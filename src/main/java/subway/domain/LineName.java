package subway.domain;

import subway.exception.LineNameValidateLengthException;

public class LineName {

    private static final int MAX_LENGTH = 20;

    private final String value;

    public LineName(final String value) {
        validateName(value);
        this.value = value;
    }

    private void validateName(final String name) {
        if (name.length() > MAX_LENGTH) {
            throw new LineNameValidateLengthException();
        }
    }

    public String getValue() {
        return value;
    }
}
