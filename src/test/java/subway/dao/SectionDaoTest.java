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
import subway.application.SectionService;
import subway.application.exception.InvalidDistanceException;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureTestDatabase
class SectionDaoTest {
    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionDao sectionDao;

    @Autowired
    private LineDao lineDao;

    @Autowired
    private StationDao stationDao;

    private Line line;
    private Station stationS;
    private Station stationJ;
    private Station stationO;

    @BeforeEach
    void setUp() {
        line = lineDao.insert(new Line("1호선", "blue"));
        stationS = stationDao.insert(new Station("송탄"));
        stationJ = stationDao.insert(new Station("진위"));
        stationO = stationDao.insert(new Station("오산"));
    }

    @Test
    @DisplayName("거리 정보는 양의 정수로 제한합니다.")
    void distanceFormat() {
        // when && then
        assertThatThrownBy(() -> sectionDao.insert(new Section(line, stationS, stationJ, Distance.of(-3))))
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

        sectionService.insert(line.getId(), stationS.getName(), stationJ.getName(), Distance.of(6), true);
        sectionService.insert(line.getId(), stationO.getName(), stationJ.getName(), Distance.of(3), false);

        // when & then
        Station stationY = stationDao.insert(new Station("양평"));
        assertThatCode(() -> sectionDao.insert(new Section(line, stations.get(index), stationY, Distance.of(2))))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("특정 노선에 등록된 역을 상행부터 순서대로 조회합니다.")
    void findAllOrderByUp() {
        sectionService.insert(line.getId(), stationS.getName(), stationJ.getName(), Distance.of(5), true);
        sectionService.insert(line.getId(), stationO.getName(), stationS.getName(), Distance.of(6), true);

        assertThat(sectionDao.findAllStationsOrderByUp(lineDao.findById(line.getId())))
                .containsExactly(stationO, stationS, stationJ);
    }
}