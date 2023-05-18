package fixtures.path;

import static fixtures.path.PathGeneralSectionFixtures.INITIAL_GENERAL_SECTION_LINE3_D_TO_E;
import static fixtures.path.PathGeneralSectionFixtures.INITIAL_GENERAL_SECTION_LINE7_A_TO_D;
import static fixtures.path.PathSectionsFixtures.INITIAL_SHORTEST_PATH;
import static fixtures.path.PathStationFixtures.*;

import java.util.List;

import subway.dto.PathResponse;
import subway.dto.PathSectionDto;

public class PathSectionDtoFixtures {

    public static class INITIAL_PATH_SECTION_DTOS {

        public static final List<PathSectionDto> DTOS = List.of(
                new PathSectionDto(INITIAL_STATION_LINE2_A.FIND_DTO, INITIAL_STATION_LINE7_A.FIND_DTO, true, 0),
                new PathSectionDto(INITIAL_STATION_LINE7_A.FIND_DTO, INITIAL_STATION_LINE7_D.FIND_DTO, false, INITIAL_GENERAL_SECTION_LINE7_A_TO_D.DISTANCE),
                new PathSectionDto(INITIAL_STATION_LINE7_D.FIND_DTO, INITIAL_STATION_LINE3_D.FIND_DTO, true, 0),
                new PathSectionDto(INITIAL_STATION_LINE3_D.FIND_DTO, INITIAL_STATION_LINE3_E.FIND_DTO, false, INITIAL_GENERAL_SECTION_LINE3_D_TO_E.DISTANCE)
        );
    }

    public static class INITIAL_PATH_RESPONSE {

        public static final int FARE = 1450;

        public static final PathResponse RESPONSE = new PathResponse(
                INITIAL_PATH_SECTION_DTOS.DTOS,
                INITIAL_SHORTEST_PATH.TOTAL_DISTANCE,
                FARE
        );
    }
}
