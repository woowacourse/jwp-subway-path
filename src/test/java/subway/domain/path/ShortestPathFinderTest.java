package subway.domain.path;

import static fixtures.path.PathSectionsFixtures.INITIAL_SHORTEST_PATH;
import static fixtures.path.PathStationFixtures.INITIAL_STATION_LINE2_A;
import static fixtures.path.PathStationFixtures.INITIAL_STATION_LINE3_E;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import fixtures.path.PathGeneralSectionFixtures;
import fixtures.path.PathStationFixtures;
import fixtures.path.PathTransferSectionFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.general.GeneralSection;
import subway.domain.section.transfer.TransferSection;
import subway.domain.station.Station;

public class ShortestPathFinderTest {

    // TODO: 최단 거리가 같을 때 케이스도 추가하기
    @Test
    @DisplayName("최단 경로를 계산하여 최단 경로가 담긴 PathSections를 반환한다.")
    void findShortestPath() {
        // given
        Station startStation = INITIAL_STATION_LINE2_A.FIND_STATION;
        Station endStation = INITIAL_STATION_LINE3_E.FIND_STATION;

        List<Station> allPathInitialStation = PathStationFixtures.ALL_INITIAL_STATION;
        List<GeneralSection> allPathInitialGeneralSection = PathGeneralSectionFixtures.ALL_INITIAL_GENERAL_SECTION;
        List<TransferSection> allPathInitialTransferSection = PathTransferSectionFixtures.ALL_INITIAL_TRANSFER_SECTION;

        PathSections expectedPathSections = INITIAL_SHORTEST_PATH.PATH_SECTIONS;

        // when
        ShortestPathFinder shortestPathFinder =
                new ShortestPathFinder(allPathInitialStation, allPathInitialGeneralSection, allPathInitialTransferSection);
        PathSections findPathSections = shortestPathFinder.findShortestPathSections(startStation, endStation);

        // then
        assertThat(findPathSections).usingRecursiveComparison().isEqualTo(expectedPathSections);
    }

}
