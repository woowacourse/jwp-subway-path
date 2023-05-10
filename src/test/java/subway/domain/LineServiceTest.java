package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dto.LineCreateRequest;
import subway.service.LineService;

@SpringBootTest
@Transactional
class LineServiceTest {

    @Autowired
    private StationDao stationDao;

    @Autowired
    private LineService lineService;


    @BeforeEach
    void setUp() {
    }

    @DisplayName("새로운 노선을 추가한다")
    @Test
    void createNewLine() {
        // given
        Station upStation = stationDao.insert(new Station("안국"));
        Station downStation = stationDao.insert(new Station("경복궁"));
        lineService.createNewLine(new LineCreateRequest("3호선", upStation.getId(), downStation.getId(), 10));

        // when
        List<Station> allStation = lineService.findAllStation("3호선");

        // then
        assertThat(allStation).containsExactly(
                new Station("안국"),
                new Station("경복궁")
        );
    }

    @Test
    @DisplayName("기존 노선에 새로운 역을 추가한다")
    void addStationToExistLine() {
        // given
        subwayManager.addStationToExistLine("1호선", "잠실나루", "잠실새내", 7);

        // when
        List<MyStation> allStation = subwayManager.findAllStation("1호선");

        // then
        assertThat(allStation).containsExactly(
                new MyStation("잠실"),
                new MyStation("잠실나루"),
                new MyStation("잠실새내")
        );
    }
}
