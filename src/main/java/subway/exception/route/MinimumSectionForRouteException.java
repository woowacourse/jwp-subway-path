package subway.exception.route;

import subway.exception.SubwayGlobalException;

public class MinimumSectionForRouteException extends SubwayGlobalException {
    public MinimumSectionForRouteException(final String message) {
        super(message);
    }
}
