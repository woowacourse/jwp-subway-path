package subway.helper;

import java.util.List;
import subway.dto.section.SectionResponse;
import subway.dto.station.StationResponse;

public class SubwayPathFixture {

    private SubwayPathFixture() {
    }

    public static List<SectionResponse> sectionResponsesFixture() {
        return List.of(
                new SectionResponse("서울역", "용산역", 10),
                new SectionResponse("용산역", "노량진역", 10),
                new SectionResponse("노량진역", "대방역", 10)
        );
    }

    public static List<StationResponse> stationResponsesFixture() {
        return List.of(
                new StationResponse("서울역"),
                new StationResponse("용산역"),
                new StationResponse("노량진역")
        );
    }
}
