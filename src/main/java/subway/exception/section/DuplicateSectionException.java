package subway.exception.section;

import subway.exception.SubwayGlobalException;

public class DuplicateSectionException extends SubwayGlobalException {
    public DuplicateSectionException(final String message) {
        super(message);
    }
}
