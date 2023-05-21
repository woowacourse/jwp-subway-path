package subway.cenario;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.application.LineService;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.EmptyDistance;
import subway.line.domain.station.EmptyStation;
import subway.line.domain.station.Station;
import subway.line.domain.station.application.StationService;
import subway.line.domain.station.application.dto.StationSavingInfo;
import subway.line.application.dto.LineSavingInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
@DisplayName("노선 위에 있는 역을 삭제할 때의 상황 테스트")
public class DeletingStationFromLineTest {

    @Autowired
    private LineService lineService;
    @Autowired
    private StationService stationService;

    private Line line;
    private Station stationB;
    private Station stationS;
    private Station stationSD;

    @BeforeEach
    void setup() {
        line = lineService.saveLine(new LineSavingInfo("1호선", "blue"));
        stationB = stationService.saveStation(new StationSavingInfo("봉천역"));
        stationS = stationService.saveStation(new StationSavingInfo("신림역"));
        stationSD = stationService.saveStation(new StationSavingInfo("신도림역"));
    }

    private long saveSection(Line line, Station startingStation, Station destinationStation) {
        return lineService.saveSection(line, startingStation, destinationStation, Distance.of(4));
    }

    @Test
    @DisplayName("노선상의 역이 2개 뿐일 때 테스트")
    void Testcase1() {
        // given
        saveSection(line, stationB, stationS);

        assertAll(
                () -> assertThat(line.getHead())
                        .as("상행 끝 역은 봉천역이다.")
                        .isEqualTo(stationB),
                () -> assertThat(line.getSections())
                        .as("노선에 등록된 구간은 2개이다.")
                        .hasSize(2)
        );

        // when
        lineService.deleteStation(line, stationS);

        // then
        assertAll(
                "2개의 역 중 하나가 삭제되면 나머지 역도 사라져 노선이 비게 되어야 한다.",
                () -> assertThat(line.getHead())
                        .as("상행 끝 역은 비어있다.")
                        .isEqualTo(new EmptyStation()),
                () -> assertThat(line.getSections())
                        .as("노선에는 아무 역도 등록되어 있지 않다.")
                        .hasSize(0)
        );
    }

    @Test
    @DisplayName("노선 맨 앞의 역을 삭제할 때 테스트")
    void TestCase2() {
        // given
        saveSection(line, stationB, stationS);
        saveSection(line, stationS, stationSD);
        assertAll(
                () -> assertThat(line.getHead())
                        .as("상행 끝 역은 봉천역이다.")
                        .isEqualTo(stationB),
                () -> assertThat(line.getSections())
                        .as("노선에 등록된 구간은 3개이다.")
                        .hasSize(3)
        );

        // when
        lineService.deleteStation(line, stationB);

        // then
        assertAll(
                "노선 상행 끝의 역을 제거하면 3가지 변화가 생긴다.",
                () -> assertThat(line.getHead())
                        .as("상행 끝 역은 신림역이다.")
                        .isEqualTo(stationS),
                () -> assertThat(line.getSections())
                        .as("노선에 등록된 구간은 2개이다.")
                        .hasSize(2),
                () -> assertThat(line.findSectionByPreviousStation(stationB))
                        .as("삭제된 역을 현재역으로 하는 구간이 제거된다.")
                        .isEmpty()
        );
    }

    @Test
    @DisplayName("노선 맨 뒤의 역을 삭제할 때 테스트")
    void TestCase3() {
        // given
        saveSection(line, stationB, stationS);
        saveSection(line, stationS, stationSD);
        assertAll(
                () -> assertThat(line.findSectionByPreviousStation(stationS).or(() -> fail()).get().getNextStation())
                        .as("하행 끝에서 두 번째 구간의 다음 역 정보는 노선의 하행 끝에 있는 역을 가리킨다.")
                        .isEqualTo(stationSD),
                () -> assertThat(line.findSectionByPreviousStation(stationS).or(() -> fail()).get().getDistance())
                        .as("거리 정보 또한 정상적으로 등록되어 있다.")
                        .isEqualTo(Distance.of(4))
        );

        // when
        lineService.deleteStation(line, stationSD);

        // then
        assertAll(
                "노선 상행 끝의 역을 제거하면 2가지 변화가 생긴다.",
                () -> assertThat(line.findSectionByPreviousStation(stationSD))
                        .as("삭제된 역을 현재역으로 하는 구간이 제거된다.")
                        .isEmpty(),
                () -> assertThat(line.findSectionByPreviousStation(stationS)
                        .or(Assertions::fail).get()
                        .getNextStation())
                        .as("하행 끝에서 두 번째 구간의 다음 역 정보가 사라진다.")
                        .isEqualTo(new EmptyStation()),
                () -> assertThat(line.findSectionByPreviousStation(stationS)
                        .or(Assertions::fail).get()
                        .getDistance())
                        .as("다음 역이 없으므로 거리정보도 사라진다.")
                        .isEqualTo(new EmptyDistance())
        );
    }

    @Test
    @DisplayName("노선 한가운데 역을 삭제할 때 테스트")
    void TestCase4() {
        // given
        saveSection(line, stationB, stationS);
        saveSection(line, stationS, stationSD);
        assertAll(
                () -> assertThat(line.findSectionByPreviousStation(stationS).or(() -> fail()).get().getNextStation())
                        .as("삭제 대상 역을 current로 하는 구간 바로 앞의 구간은, next 역으로 삭제 대상 역을 가리킨다.")
                        .isEqualTo(stationSD),
                () -> assertThat(line.findSectionByPreviousStation(stationS).or(() -> fail()).get().getDistance())
                        .as("거리 정보 또한 정상적으로 등록되어 있다.")
                        .isEqualTo(Distance.of(4))
        );

        // when
        lineService.deleteStation(line, stationS);

        // then
        assertAll(
                "노선 한가운데 역을 제거하면 2가지 변화가 생긴다.",
                () -> assertThat(line.findSectionByPreviousStation(stationS))
                        .as("삭제된 역을 현재역으로 하는 구간이 제거된다.")
                        .isEmpty(),
                () -> assertThat(line.findSectionByPreviousStation(stationB).or(Assertions::fail).get().getNextStation())
                        .as("삭제된 구간의 바로 앞 구간은, next 역으로 삭제된 구간의 next 역을 가리킨다.")
                        .isEqualTo(stationSD),
                () -> assertThat(line.findSectionByPreviousStation(stationB).or(Assertions::fail).get().getDistance())
                        .as("삭제된 구간의 바로 앞 구간 거리와 삭제된 구간의 거리가 병합된다.")
                        .isEqualTo(Distance.of(8))
        );
    }
}
