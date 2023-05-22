package subway.line.domain.navigation.application.exception;

import subway.common.exception.ServerErrorException;

public class NavigationNotFoundException extends ServerErrorException {
    private static final String MESSAGE = "네비게이션 정보가 등록되지 않았습니다.";

    public NavigationNotFoundException() {
        super(MESSAGE);
    }

    public NavigationNotFoundException(Throwable cause) {
        super(MESSAGE, cause);
    }

    protected NavigationNotFoundException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(MESSAGE, cause, enableSuppression, writableStackTrace);
    }
}
