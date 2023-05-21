package subway.fixture;

import subway.domain.Station;
import subway.dto.request.StationRequest;
import subway.dto.response.StationResponse;
import subway.entity.StationEntity;

@SuppressWarnings("NonAsciiCharacters")
public class StationFixture {

    public static class 역삼역 {

        private static final Long ID = 1L;
        private static final String NAME = "역삼역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final StationRequest REQUEST = new StationRequest(NAME);
        public static final StationResponse RESPONSE = new StationResponse(ID, NAME);
    }

    public static class 삼성역 {


        private static final Long ID = 2L;
        private static final String NAME = "삼성역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final StationRequest REQUEST = new StationRequest(NAME);
        public static final StationResponse RESPONSE = new StationResponse(ID, NAME);
    }

    public static class 잠실역 {

        private static final Long ID = 3L;
        private static final String NAME = "잠실역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final StationRequest REQUEST = new StationRequest(NAME);
        public static final StationResponse RESPONSE = new StationResponse(ID, NAME);
    }

    public static class 건대역 {

        private static final Long ID = 4L;
        private static final String NAME = "건대역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final StationRequest REQUEST = new StationRequest(NAME);
        public static final StationResponse RESPONSE = new StationResponse(ID, NAME);
    }

    public static class 강남역 {

        private static final Long ID = 5L;
        private static final String NAME = "강남역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final StationRequest REQUEST = new StationRequest(NAME);
        public static final StationResponse RESPONSE = new StationResponse(ID, NAME);
    }

    public static class 신논현역 {

        private static final Long ID = 6L;
        private static final String NAME = "신논현역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final StationRequest REQUEST = new StationRequest(NAME);
        public static final StationResponse RESPONSE = new StationResponse(ID, NAME);
    }

    public static class 종합운동장역 {

        private static final Long ID = 7L;
        private static final String NAME = "종합운동장역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final StationRequest REQUEST = new StationRequest(NAME);
        public static final StationResponse RESPONSE = new StationResponse(ID, NAME);
    }
}
