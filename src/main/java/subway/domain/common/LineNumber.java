package subway.domain.common;

import subway.exception.LineNumberUnderMinimumNumber;

import java.util.Objects;

public class LineNumber {

    private static final int MINIMUM_NUMBER = 0;

    private final Long lineNumber;

    public LineNumber(final Long lineNumber) {
        validateLineNumber(lineNumber);
        this.lineNumber = lineNumber;
    }

    private void validateLineNumber(final Long lineNumber) {
        if (lineNumber < MINIMUM_NUMBER) {
            throw new LineNumberUnderMinimumNumber();
        }
    }

    public Long getLineNumber() {
        return lineNumber;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof LineNumber)) return false;
        LineNumber that = (LineNumber) o;
        return Objects.equals(lineNumber, that.lineNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineNumber);
    }
}
