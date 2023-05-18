package subway.domain.line;

import java.util.Optional;

public enum LineStatus {

    EXIST,
    INITIAL;

    LineStatus() {
    }

    public static LineStatus of(Optional<Line> findNullableLine) {
        if (findNullableLine.isEmpty()) {
            return INITIAL;
        }
        return EXIST;
    }
}
