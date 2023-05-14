package subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.StationDao;
import subway.dto.AddStationToLineRequest;
import subway.dto.LineCreateRequest;
import subway.service.LineService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

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
        Line newLine = lineService.createNewLine(
                new LineCreateRequest("3호선", "안국", "경복궁", 10));
        // when
        List<Station> allStation = lineService.getStations(newLine.getId());

        // then
        assertAll(
                () -> assertThat(allStation.get(0).getName()).isEqualTo("안국"),
                () -> assertThat(allStation.get(1).getName()).isEqualTo("경복궁")
        );
    }

    @Test
    @DisplayName("기존 노선에 새로운 역을 추가한다")
    void addStationToExistLine() {
        // given
        Station upStation = stationDao.insert(new Station("안국"));
        Station downStation = stationDao.insert(new Station("경복궁"));
        Line line = lineService.createNewLine(
                new LineCreateRequest("3호선", "안국", "경복궁", 10));

        Station newStation = stationDao.insert(new Station("충무로"));
        AddStationToLineRequest addRequest = new AddStationToLineRequest(downStation.getId(),
                newStation.getId(), 5);
        Line newLine = lineService.addStationToExistLine(line.getId(), addRequest);
        // when
        List<Station> allStation = lineService.getStations(newLine.getId());
        // then
        assertAll(
                () -> assertThat(allStation.get(0).getName()).isEqualTo("안국"),
                () -> assertThat(allStation.get(1).getName()).isEqualTo("경복궁"),
                () -> assertThat(allStation.get(2).getName()).isEqualTo("충무로")
        );
    }
}