package subway.application;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.dto.*;
import subway.exception.line.LineIsNotInitException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class StationServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private LineService lineService;

    @Autowired
    private StationService stationService;

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM SECTION");
        jdbcTemplate.execute("DELETE FROM STATION");
        jdbcTemplate.execute("DELETE FROM line");
    }

    @Test
    @DisplayName("초기 두 역을 노선에 등록한다.")
    void register_init_station_to_line() {
        // given
        LineResponse lineResponse = lineService.saveLine(new LineRequest("2호선", "#123456"));
        StationInitRequest request = new StationInitRequest(lineResponse.getName(), "잠실역", "선릉역", 10);

        // when
        StationInitResponse result = stationService.saveInitStations(request);

        // then
        assertThat(result.getUpboundStationName()).isEqualTo(request.getUpBoundStationName());
        assertThat(result.getDownboundStationName()).isEqualTo(request.getDownBoundStationName());
    }

    @Test
    @DisplayName("초기 역을 등록하는데 노선의 구간이 비어있지 않으면 에러를 발생한다.")
    void check_exception_when_register_init_station_to_not_init_station() {
        // given
        LineResponse lineResponse = lineService.saveLine(new LineRequest("2호선", "#123456"));
        StationInitRequest initRequest = new StationInitRequest(lineResponse.getName(), "잠실역", "선릉역", 10);
        stationService.saveInitStations(initRequest);

        StationInitRequest request = new StationInitRequest(lineResponse.getName(), "선릉역", "강남역", 10);


        // when + then
        assertThatThrownBy(() -> stationService.saveInitStations(request))
                .isInstanceOf(LineIsNotInitException.class);

    }

    @Test
    @DisplayName("역 하나를 등록한다.")
    void register_station() {
        // given
        LineResponse lineResponse = lineService.saveLine(new LineRequest("2호선", "#123456"));
        StationInitRequest initRequest = new StationInitRequest(lineResponse.getName(), "잠실역", "선릉역", 10);
        stationService.saveInitStations(initRequest);

        StationRequest request = new StationRequest("강남역", lineResponse.getName(), initRequest.getDownBoundStationName(), "right", 10);

        // when
        StationResponse result = stationService.saveStation(request);

        // then
        assertThat(result.getName()).isEqualTo(request.getRegisterStationName());
    }

    @Test
    @DisplayName("구간이 하나인 노선에 역을 삭제한다.")
    void delete_station() {
        // given
        LineResponse lineResponse = lineService.saveLine(new LineRequest("2호선", "#123456"));
        StationInitRequest initRequest = new StationInitRequest(lineResponse.getName(), "잠실역", "선릉역", 10);
        StationInitResponse stationInitResponse = stationService.saveInitStations(initRequest);

        // when
        stationService.deleteStation(stationInitResponse.getDownboundStationId());
        LineResponse result = lineService.readLine(lineResponse.getId());

        // then
        assertThat(result.getStationResponses()).isEqualTo(List.of());
    }

    /**
     * 잠실역 - 선릉역 - 강남역 상황에서 잠실역을 삭제하는 경우
     */
    @Test
    @DisplayName("종점 역을 삭제한다.")
    void delete_bound_station() {
        // given
        LineResponse lineResponse = lineService.saveLine(new LineRequest("2호선", "#123456"));
        StationInitRequest initRequest = new StationInitRequest(lineResponse.getName(), "잠실역", "선릉역", 10);
        StationInitResponse stationInitResponse = stationService.saveInitStations(initRequest);

        StationRequest request = new StationRequest("강남역", lineResponse.getName(), initRequest.getDownBoundStationName(), "right", 10);
        StationResponse stationResponse = stationService.saveStation(request);

        // when
        stationService.deleteStation(stationInitResponse.getUpboundStationId());
        LineResponse result = lineService.readLine(lineResponse.getId());

        // then
        assertThat(result.getStationResponses().get(0).getName()).isEqualTo(stationInitResponse.getDownboundStationName());
        assertThat(result.getStationResponses().get(1).getName()).isEqualTo(stationResponse.getName());
    }

    /**
     * 잠실역 - 선릉역 - 강남역 상황에서 선릉역을 삭제
     */
    @Test
    @DisplayName("노선 총 구간의 중간 역을 삭제한다.")
    void delete_inter_station() {
        // given
        LineResponse lineResponse = lineService.saveLine(new LineRequest("2호선", "#123456"));
        StationInitRequest initRequest = new StationInitRequest(lineResponse.getName(), "잠실역", "선릉역", 10);
        StationInitResponse stationInitResponse = stationService.saveInitStations(initRequest);

        StationRequest request = new StationRequest("강남역", lineResponse.getName(), initRequest.getDownBoundStationName(), "right", 10);
        StationResponse stationResponse = stationService.saveStation(request);

        // when
        stationService.deleteStation(stationInitResponse.getDownboundStationId());
        LineResponse result = lineService.readLine(lineResponse.getId());

        // then
        assertThat(result.getStationResponses().get(0).getName()).isEqualTo(stationInitResponse.getUpboundStationName());
        assertThat(result.getStationResponses().get(1).getName()).isEqualTo(stationResponse.getName());
    }
}