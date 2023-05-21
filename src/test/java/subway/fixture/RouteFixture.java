package subway.fixture;

import java.util.List;
import subway.domain.Route;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.역삼역;

public class RouteFixture {

    public static class 역삼_삼성_10 {

        public static final Route ROUTE = new Route(List.of(역삼역.STATION, 삼성역.STATION), 10);
    }
}
