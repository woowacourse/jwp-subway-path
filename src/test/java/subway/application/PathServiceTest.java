package subway.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class PathServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @Autowired
    private PathService pathService;

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM SECTION");
        jdbcTemplate.execute("DELETE FROM STATION");
        jdbcTemplate.execute("DELETE FROM line");
    }

    @Test
    @DisplayName("최단 경로를 조회한다.")
    void find_short_path() {
        // given
        LineRequest line2Request = new LineRequest("2호선", "#123456");
        LineRequest line3Request = new LineRequest("3호선", "#abcdef");

        lineService.saveLine(line2Request);
        lineService.saveLine(line3Request);

        StationInitRequest line2InitRequest = new StationInitRequest(line2Request.getName(), "잠실역", "강남역", 100);
        StationInitRequest line3InitRequest = new StationInitRequest(line3Request.getName(), "잠실역", "선릉역", 3);
        stationService.saveInitStations(line2InitRequest);
        stationService.saveInitStations(line3InitRequest);

        StationRequest line3StationReqeust = new StationRequest("강남역", line3Request.getName(), "선릉역", "right", 2);
        stationService.saveStation(line3StationReqeust);

        PathRequest pathRequest = new PathRequest(line2Request.getName(), "잠실역", line3Request.getName(), "강남역");

        // when
        PathResponse result = pathService.findShortPath(pathRequest);

        // then
        assertThat(result.getDistance()).isEqualTo(5);
        assertThat(result.getFee()).isEqualTo(1250);
        assertThat(result.getStations()).isEqualTo(List.of("잠실역", "선릉역", "강남역"));
    }
}
