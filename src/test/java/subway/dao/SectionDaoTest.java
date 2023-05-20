package subway.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.line.Line;
import subway.line.application.LineRepository;
import subway.line.application.LineService;
import subway.line.domain.section.domain.Distance;
import subway.line.domain.section.domain.exception.InvalidDistanceException;
import subway.line.domain.station.Station;
import subway.line.domain.station.infrastructure.StationDao;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class SectionDaoTest {
    @Autowired
    private LineRepository lineRepository;
    @Autowired
    private LineService lineService;
    @Autowired
    private StationDao stationDao;

    private Line line;
    private Station stationS;
    private Station stationJ;
    private Station stationO;

    @BeforeEach
    void setUp() {
        line = lineRepository.makeLine("1호선", "blue");
        stationS = stationDao.insert("송탄");
        stationJ = stationDao.insert("진위");
        stationO = stationDao.insert("오산");
    }

    @Test
    @DisplayName("거리 정보는 양의 정수로 제한합니다.")
    void distanceFormat() {
        // when && then
        assertThatThrownBy(() -> lineService.saveSection(line, stationS, stationJ, Distance.of(-3)))
                .isInstanceOf(InvalidDistanceException.class)
                .hasMessage("거리 정보는 양의 정수로 제한합니다.");
    }

    @ParameterizedTest(name = "{displayName} - {0}")
    @DisplayName("A-B-C역이 등록되어 있는 노선의 어디에든 D역을 등록할 수 있습니다.")
    @ValueSource(ints = {0, 1, 2})
    @Sql("/schema.sql")
    void savingD(int index) {
        // given
        List<Station> stations = List.of(stationS, stationJ, stationO);

        lineService.saveSection(line, stationS, stationJ, Distance.of(6));
        lineService.saveSection(line, stationJ, stationO, Distance.of(3));

        // when & then
        Station stationY = stationDao.insert("양평");
        assertThatCode(() -> lineService.saveSection(line, stations.get(index), stationY, Distance.of(2)))
                .doesNotThrowAnyException();
    }
}