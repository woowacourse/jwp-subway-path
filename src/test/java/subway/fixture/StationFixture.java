package subway.fixture;

import subway.dto.request.StationRequest;
import subway.entity.StationEntity;

@SuppressWarnings("NonAsciiCharacters")
public class StationFixture {

    public static class 역삼역 {

        public static final StationEntity ENTITY = new StationEntity(1L, "역삼역");
        public static final StationRequest REQUEST = new StationRequest("역삼역");

    }

    public static class 삼성역 {

        public static final StationEntity ENTITY = new StationEntity(2L, "삼성역");
        public static final StationRequest REQUEST = new StationRequest("삼성역");

    }

    public static class 잠실역 {

        public static final StationEntity STATION_ENTITY = new StationEntity(3L, "잠실역");
        public static final StationRequest REQUEST = new StationRequest("잠실역");

    }
}
