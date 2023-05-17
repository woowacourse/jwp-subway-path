package subway.domain;

import java.util.List;
import subway.common.exception.SubwayIllegalArgumentException;

// TODO(질문): 기능(책임)이 없는 도메인도 도메인인가?
public class Route {

    private final List<Station> route;
    private final int distance;

    public Route(final List<Station> route, final int distance) {
        validateRoute(route);
        validateDistance(distance);
        this.route = route;
        this.distance = distance;
    }

    private void validateRoute(final List<Station> route) {
        if (route == null || route.size() < 2) {
            throw new SubwayIllegalArgumentException("경로에는 2개 이상의 역이 존재해야합니다.");
        }
    }

    private void validateDistance(final int distance) {
        if (distance <= 0) {
            throw new SubwayIllegalArgumentException("거리는 0보다 커야합니다.");
        }
    }

    public List<Station> getRoute() {
        return route;
    }

    public int getDistance() {
        return distance;
    }
}
