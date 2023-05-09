package subway.fixture;

import subway.domain.Station;
import subway.dto.request.StationRequest;

@SuppressWarnings("NonAsciiCharacters")
public class StationFixture {

    public static class 역삼역 {

        public static final Station STATION = new Station(1L, "역삼역");
        public static final StationRequest REQUEST = new StationRequest("역삼역");

    }

    public static class 삼성역 {

        public static final Station STATION = new Station(2L, "삼성역");
        public static final StationRequest REQUEST = new StationRequest("삼성역");

    }

    public static class 잠실역 {

        public static final Station STATION = new Station(3L, "잠실역");
        public static final StationRequest REQUEST = new StationRequest("잠실역");

    }
}
