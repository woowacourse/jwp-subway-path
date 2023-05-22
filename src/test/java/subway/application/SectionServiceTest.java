package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.UnRegisteredLine;
import subway.line.application.LineRepository;
import subway.line.application.LineService;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.exception.InvalidDistanceException;
import subway.line.domain.station.Station;
import subway.line.domain.station.application.StationRepository;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class SectionServiceTest {
    @Autowired
    private LineRepository lineRepository;

    @Autowired
    private StationRepository stationRepository;

    @Autowired
    private LineService lineService;

    private Line lineOne;
    private Station stationS;
    private Station stationJ;
    private Station stationO;

    @BeforeEach
    void setUp() {
        lineOne = lineRepository.save(new UnRegisteredLine("1호선", "blue"));
        stationS = stationRepository.insert("송탄");
        stationJ = stationRepository.insert("진위");
        stationO = stationRepository.insert("오산");
        lineService.saveSection(lineOne, stationS, stationJ, Distance.of(6));
    }

    @Test
    @DisplayName("노선에 역이 하나도 등록되지 않은 상황에서 최초 등록시 두 역이 동시 등록됩니다.")
    void initialize() {
        assertThat(lineOne.getSections())
                .as("두 개의 역이 동시에 등록되어 있습니다.")
                .hasSize(2);
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 상행방향")
    void distanceChange() {
        // A-B-C 노선에서 B 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(lineOne.findDistanceBetween(stationS, stationJ))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(Distance.of(6));

        // when
        lineService.saveSection(lineOne, stationO, stationJ, Distance.of(2));

        // then
        assertThat(lineOne.findDistanceBetween(stationS, stationO))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(Distance.of(4));
        assertThat(lineOne.findDistanceBetween(stationO, stationJ))
                .as("오산과 진위 사이의 거리는 2km.")
                .isEqualTo(Distance.of(2));
    }

    @Test
    @DisplayName("노선 가운데 역이 등록될 경우 거리 정보를 고려해야 합니다. - 하행방향")
    void distanceChange2() {
        // A-B-C 노선에서 A 다음에 D 역을 등록하려고 하는데
        // B-C가 3km, B-D거리가 2km라면 B-D거리는 2km로 등록되어야 하고 D-C 거리는 1km로 등록되어야 합니다.

        // given
        assertThat(lineOne.findDistanceBetween(stationS, stationJ))
                .as("처음 송탄과 진위 사이의 거리는 6km.")
                .isEqualTo(Distance.of(6));

        // when
        lineService.saveSection(lineOne, stationS, stationO, Distance.of(2));

        // then
        assertThat(lineOne.findDistanceBetween(stationS, stationO))
                .as("송탄과 오산 사이의 거리는 4km.")
                .isEqualTo(Distance.of(2));
        assertThat(lineOne.findDistanceBetween(stationO, stationJ))
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
        assertThatThrownBy(() -> lineService.saveSection(lineOne, stationO, stationJ, Distance.of(6)))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessage("거리 정보는 양의 정수로 제한합니다.");
    }

    @Test
    @DisplayName("노선에 역이 등록될 때 거리 정보도 함께 포함됩니다. 그래서 이웃한 역의 거리정보를 조회할 수 있습니다.")
    void distanceSaving() {
        // when & then
        assertThat(lineOne.findDistanceBetween(stationJ, stationS)).isEqualTo(Distance.of(6));
    }

    @Test
    @DisplayName("거리를 조회할 두 역을 거꾸로 제공해도 거리를 계산할 수 있습니다.")
    void distanceSavingReverse() {
        // when & then
        assertThat(lineOne.findDistanceBetween(stationJ, stationS)).isEqualTo(Distance.of(6));
    }

    @Test
    @DisplayName("하나의 역은 여러 노선에 등록될 수 있습니다.")
    void multipleSubwayMap() {
        // given
        Line lineTwo = lineRepository.save(new UnRegisteredLine("2호선", "yellow"));

        // when
        assertThatCode(() -> lineService.saveSection(lineTwo, stationO, stationS, Distance.of(6)))
                .as("송탄역을 1호선과 2호선 위에 동시에 올릴 수 있습니다.")
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("역을 처음 초기화하면 노선 정보에 상행 종점 역 정보가 저장되어 있다.")
    void head() {
        assertThat(lineOne.getHead()).isEqualTo(stationS);
    }

    @Test
    @DisplayName("역의 상행 종점 역 정보가 바뀌면, 노선 정보에 상행 종점 역 정보도 변경된다.")
    void changeHead() {
        assertThat(lineOne.getHead())
                .as("본래는 송탄이 상행 최종역이지만")
                .isEqualTo(stationS);

        lineService.saveSection(lineOne, stationO, stationS, Distance.of(4));

        assertThat(lineOne.getHead())
                .as("오산을 송탄 앞에 배치한 이후로는 오산이 상행 최종역이다.")
                .isEqualTo(stationO);
    }


    @Test
    @DisplayName("특정 노선에 등록된 역을 상행부터 순서대로 조회합니다.")
    void findAllOrderByUp() {
        lineService.saveSection(lineOne, stationO, stationS, Distance.of(6));

        assertThat(lineOne.findAllStationsOrderByUp())
                .containsExactly(stationO, stationS, stationJ);
    }
}