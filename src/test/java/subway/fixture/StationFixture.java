package subway.fixture;

import subway.adapter.in.web.station.dto.CreateStationRequest;
import subway.adapter.out.persistence.entity.StationEntity;
import subway.application.port.in.station.dto.response.StationQueryResponse;
import subway.domain.Station;

@SuppressWarnings("NonAsciiCharacters")
public class StationFixture {

    public static class 역삼역 {

        private static final Long ID = 1L;
        private static final String NAME = "역삼역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final CreateStationRequest REQUEST = new CreateStationRequest(NAME);
        public static final StationQueryResponse RESPONSE = new StationQueryResponse(ID, NAME);
    }

    public static class 삼성역 {

        private static final Long ID = 2L;
        private static final String NAME = "삼성역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final CreateStationRequest REQUEST = new CreateStationRequest(NAME);
        public static final StationQueryResponse RESPONSE = new StationQueryResponse(ID, NAME);
    }

    public static class 잠실역 {

        private static final Long ID = 3L;
        private static final String NAME = "잠실역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final CreateStationRequest REQUEST = new CreateStationRequest(NAME);
        public static final StationQueryResponse RESPONSE = new StationQueryResponse(ID, NAME);
    }

    public static class 건대역 {

        private static final Long ID = 4L;
        private static final String NAME = "건대역";

        public static final Station STATION = new Station(ID, NAME);
        public static final StationEntity ENTITY = new StationEntity(ID, NAME);
        public static final CreateStationRequest REQUEST = new CreateStationRequest(NAME);
        public static final StationQueryResponse RESPONSE = new StationQueryResponse(ID, NAME);
    }
}
