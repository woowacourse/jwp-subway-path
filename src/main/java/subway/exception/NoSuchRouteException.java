package subway.exception;

import subway.domain.Station;

public class NoSuchRouteException extends ApiNoSuchResourceException {

    public NoSuchRouteException() {
        super("이동할 수 없는 경로입니다.");
    }

    public NoSuchRouteException(final Station from, final Station to) {
        super("존재하지 않는 경로입니다." +
                "from : " + from +
                "to : " + to);
    }
}
