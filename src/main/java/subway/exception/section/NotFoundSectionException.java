package subway.exception.section;

import subway.exception.SubwayGlobalException;

public class NotFoundSectionException extends SubwayGlobalException {
    public NotFoundSectionException(final String message) {
        super(message);
    }
}
