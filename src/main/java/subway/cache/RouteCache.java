package subway.cache;

import subway.domain.subway.Lines;
import subway.domain.subway.Route;

public class RouteCache {

    private static final Route route;

    static {
        route = Route.createDefault();
    }

    public static void update(final Lines lines) {
        route.update(lines);
    }

    public static Route getRoute() {
        return route;
    }
}
