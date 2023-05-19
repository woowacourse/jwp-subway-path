package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.section.domain.exception.InvalidDistanceException;
import subway.line.domain.station.application.StationRepository;
import subway.line.infrastructure.LineDao;
import subway.line.domain.section.infrastructure.SectionDao;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.section.domain.Distance;
import subway.line.Line;
import subway.line.domain.station.Station;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class SectionServiceTest {
    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationRepository stationRepository;

    private Line lineOne;
    private Station stationS;
    private Station stationJ;
    private Station stationO;

    @BeforeEach
    void setUp() {
        lineOne = lineDao.insert("1호선", "blue");
        stationS = stationRepository.insert(new Station("송탄"));
        stationJ = stationRepository.insert(new Station("진위"));
        stationO = stationRepository.insert(new Station("오산"));
        sectionService.insert(lineOne.getId(), "송탄", "진위", Distance.of(6), true);
    }

    @Test
    @DisplayName("노선에 역이 하나도 등록되지 않은 상황에서 최초 등록시 두 역이 동시 등록됩니다.")
    void initialize() {
        assertThat(sectionDao.countStations(lineOne))
                .as("두 개의 역이 동시에 등록되어 있습니다.")
                .isEqualTo(2);
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 상행방향")
    void distanceChange() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(sectionService.findDistanceBetween(stationS, stationJ, lineOne))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(Distance.of(6));

        // when
        sectionService.insert(lineOne.getId(), stationO.getName(), stationJ.getName(), Distance.of(2), true);

        // then
        assertThat(sectionService.findDistanceBetween(stationS, stationO, lineOne))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(Distance.of(4));
        assertThat(sectionService.findDistanceBetween(stationO, stationJ, lineOne))
                .as("오산과 진위 사이의 거리는 2km.")
                .isEqualTo(Distance.of(2));
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 하행방향")
    void distanceChange2() {
        // A-B-C 노선에서 A 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(sectionService.findDistanceBetween(stationS, stationJ, lineOne))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(Distance.of(6));

        // when
        sectionService.insert(lineOne.getId(), stationO.getName(), stationS.getName(), Distance.of(2), false);

        // then
        assertThat(sectionService.findDistanceBetween(stationS, stationO, lineOne))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(Distance.of(2));
        assertThat(sectionService.findDistanceBetween(stationO, stationJ, lineOne))
                .as("오산과 진위 사이의 거리는 2km.")
                .isEqualTo(Distance.of(4));
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리는 양의 정수라는 비즈니스 규칙을 지켜야 합니다.")
    void distanceRule() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C역의 거리가 3km인 경우 B-D 거리는 3km보다 적어야 합니다.
        // B-C가 3km인데 B-D거리가 3km면 D-C거리는 0km가 되어야 하는데 거리는 양의 정수여야 하기 때문에 이 경우 등록이 불가능 해야합니다.
        // when
        assertThatThrownBy(() -> sectionService.insert(lineOne.getId(), stationO.getName(), stationJ.getName(), Distance.of(6), true))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessage("거리 정보는 양의 정수로 제한합니다.");
    }

    @Test
    @DisplayName("노선에 역이 등록될 때 거리 정보도 함께 포함됩니다. 그래서 이웃한 역의 거리정보를 조회할 수 있습니다.")
    void distanceSaving() {
        // when & then
        assertThat(sectionService.findDistanceBetween(stationS, stationJ, lineOne)).isEqualTo(Distance.of(6));
    }

    @Test
    @DisplayName("거리를 조회할 두 역을 거꾸로 제공해도 거리를 계산할 수 있습니다.")
    void distanceSavingReverse() {
        // when & then
        assertThat(sectionService.findDistanceBetween(stationJ, stationS, lineOne)).isEqualTo(Distance.of(6));
    }

    @Test
    @DisplayName("하나의 역은 여러 노선에 등록될 수 있습니다.")
    void multipleSubwayMap() {
        // given
        Line lineTwo = lineDao.insert("2호선", "yellow");

        // when
        assertThatCode(() -> sectionService.insert(lineTwo.getId(), stationS.getName(), stationO.getName(), Distance.of(6), false))
                .as("송탄역을 1호선과 2호선 위에 동시에 올릴 수 있습니다.")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("역을 처음 초기화하면 노선 정보에 상행 종점 역 정보가 저장되어 있다.")
    void head() {
        final var headStation = lineDao.findById(lineOne.getId()).getHead();
        assertThat(headStation).isEqualTo(stationS);
    }

    @Test
    @DisplayName("역의 상행 종점 역 정보가 바뀌면, 노선 정보에 상행 종점 역 정보도 변경된다.")
    void changeHead() {
        assertThat(lineDao.findById(lineOne.getId()).getHead())
                .as("본래는 송탄이 상행 최종역이지만")
                .isEqualTo(stationS);
        sectionService.insert(lineOne.getId(), "오산", "송탄", Distance.of(4), true);
        assertThat(lineDao.findById(lineOne.getId()).getHead())
                .as("오산을 송탄 앞에 배치한 이후로는 오산이 상행 최종역이다.")
                .isEqualTo(stationO);
    }


    @Test
    @DisplayName("특정 노선에 등록된 역을 상행부터 순서대로 조회합니다.")
    void findAllOrderByUp() {
        sectionService.insert(lineOne.getId(), stationO.getName(), stationS.getName(), Distance.of(6), true);

        assertThat(sectionService.findAllStationsOrderByUp(lineDao.findById(lineOne.getId())))
                .containsExactly(stationO, stationS, stationJ);
    }
}