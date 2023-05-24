package subway.application.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;
import static subway.helper.SubwayPathFixture.stationsFixture;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.dao.InMemorySectionDao;
import subway.dao.StationDao;
import subway.domain.path.Path;
import subway.domain.section.Distance;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.exception.path.IllegalPathException;

class PathServiceTest {
    PathService pathService;

    StationDao stationDao = mock(StationDao.class);

    /*
     *   [A역]-7-[B역]-8-[C역]
     *     |             |
     *     5             1
     *     |             |
     *   [D역]-2-[F역]-3-[G역]
     *     |             |
     *     8             1
     *     |             |
     *   [E역]-4-[H역]-2-[I역]
     *
     *   [X역]-1-[Z역]
     *
     *   1호선 = A, D, E, H, I
     *   2호선 = A, B, C, G, I
     *   3호선 = D, F, G
     *   4호선 = X, Z
     *
     *   Case1. A -> I = A D F G I (distance: 11)
     *   Case2. D -> C = D F G C (distance: 6)
     *   Case3. C -> E = C G I H E (distance: 8)
     *   Case4. X -> Z = X Z (distance: 1)
     */
    @BeforeEach
    void setUp() {
        InMemorySectionDao sectionDao = new InMemorySectionDao();
        pathService = new PathService(stationDao, sectionDao);

        sectionDao.insert(1L, new Section(new Station("A역"), new Station("D역"), new Distance(5)));
        sectionDao.insert(1L, new Section(new Station("D역"), new Station("E역"), new Distance(8)));
        sectionDao.insert(1L, new Section(new Station("E역"), new Station("H역"), new Distance(4)));
        sectionDao.insert(1L, new Section(new Station("H역"), new Station("I역"), new Distance(2)));

        sectionDao.insert(2L, new Section(new Station("A역"), new Station("B역"), new Distance(7)));
        sectionDao.insert(2L, new Section(new Station("B역"), new Station("C역"), new Distance(8)));
        sectionDao.insert(2L, new Section(new Station("C역"), new Station("G역"), new Distance(1)));
        sectionDao.insert(2L, new Section(new Station("G역"), new Station("I역"), new Distance(1)));

        sectionDao.insert(3L, new Section(new Station("D역"), new Station("F역"), new Distance(2)));
        sectionDao.insert(3L, new Section(new Station("F역"), new Station("G역"), new Distance(3)));

        sectionDao.insert(4L, new Section(new Station("X역"), new Station("Z역"), new Distance(1)));
    }

    @MethodSource("pathCase")
    @ParameterizedTest
    @DisplayName("경로가 정확하게 조회되어야 한다.")
    void findPath_success(String originStation, String destinationStation, List<String> stations,
                          int distance, Set<Long> lineIds) {
        // given
        given(stationDao.findAll())
                .willReturn(stationsFixture());

        // when
        Path path = pathService.findPath(originStation, destinationStation);

        // then
        assertThat(path.getStations())
                .isEqualTo(stations);
        assertThat(path.getTotalDistance())
                .isEqualTo(distance);
        assertThat(path.getPassingLineIds())
                .isEqualTo(lineIds);
    }

    static Stream<Arguments> pathCase() {
        return Stream.of(
                Arguments.of("A역", "I역", List.of("A역", "D역", "F역", "G역", "I역"), 11,
                        Set.of(1L, 2L, 3L)),
                Arguments.of("D역", "C역", List.of("D역", "F역", "G역", "C역"), 6, Set.of(2L, 3L)),
                Arguments.of("C역", "E역", List.of("C역", "G역", "I역", "H역", "E역"), 8, Set.of(1L, 2L)),
                Arguments.of("X역", "Z역", List.of("X역", "Z역"), 1, Set.of(4L))
        );
    }

    @Test
    @DisplayName("구간에 없는 역을 조회하면 예외가 발생해야 한다.")
    void findPath_stationNotOnSection() {
        // given
        String originStationName = "A역";
        String destinationStationName = "없는역";

        // expect
        assertThatThrownBy(() -> pathService.findPath(originStationName, destinationStationName))
                .isInstanceOf(IllegalPathException.class)
                .hasMessage("해당 역이 구간에 존재하지 않습니다.");
    }

    @Test
    @DisplayName("없는 역을 조회하면 예외가 발생해야 한다.")
    void findPath_invalidStation() {
        // given
        String originStationName = "A역";
        String destinationStationName = "?역";

        // expect
        assertThatThrownBy(() -> pathService.findPath(originStationName, destinationStationName))
                .isInstanceOf(IllegalPathException.class)
                .hasMessage("해당 역이 구간에 존재하지 않습니다.");
    }

    @Test
    @DisplayName("경로를 찾을 수 없으면 예외가 발생해야 한다.")
    void findPath_invalidPath() {
        // given
        String originStationName = "A역";
        String destinationStationName = "Z역";
        given(stationDao.findAll())
                .willReturn(stationsFixture());

        // expect
        assertThatThrownBy(() -> pathService.findPath(originStationName, destinationStationName))
                .isInstanceOf(IllegalPathException.class)
                .hasMessage("해당 경로를 찾을 수 없습니다.");
    }
}
