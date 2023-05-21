package fixtures;

import subway.domain.path.Path;
import subway.domain.section.Section;
import subway.dto.PathRequest;
import subway.dto.PathResponse;

import java.util.List;

import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;

public class PathFixtures {

    public static final List<Section> ALL_SECTIONS = List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역, SECTION_건대역_TO_성수역);
    public static final int DISTANCE_강변역_TO_성수역 = DISTANCE_강변역_TO_건대역 + DISTANCE_건대역_TO_성수역;
    public static final int FARE_강변역_TO_성수역 = 1550;
    public static final Path PATH_강변역_TO_성수역 = new Path(List.of(STATION_강변역_NAME, STATION_건대역_NAME, STATION_성수역_NAME), DISTANCE_강변역_TO_성수역);
    public static final PathRequest REQUEST_PATH_강변역_TO_성수역 = new PathRequest(STATION_강변역_NAME, STATION_성수역_NAME);
    public static final PathResponse RESPONSE_PATH_강변역_TO_성수역 = new PathResponse(List.of(STATION_강변역_NAME, STATION_건대역_NAME, STATION_성수역_NAME), DISTANCE_강변역_TO_성수역, FARE_강변역_TO_성수역);
}
