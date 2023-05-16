package subway.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.application.SubwayService;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.dto.SubwayResponse;

class SubwayControllerTest {
    
    @Test
    @DisplayName("호선에 추가된 역 조회 테스트")
    void getStations() {
        // given
        final long lineId = 1L;
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "초록색");
        final List<StationResponse> stationResponses = List.of(new StationResponse(1L, "교대역"),
                new StationResponse(2L, "강남역"),
                new StationResponse(3L, "역삼역"));
        final SubwayResponse subwayResponse = new SubwayResponse(lineResponse, stationResponses);
        final SubwayService subwayServiceMock = mock(SubwayService.class);
        final SubwayController subwayController = new SubwayController(subwayServiceMock);
        // when
        when(subwayServiceMock.findAllStationsInLine(lineId)).thenReturn(subwayResponse);
        final ResponseEntity<SubwayResponse> subwayResponseEntity = subwayController.getStations(lineId);
        
        // then
        Assertions.assertThat(subwayResponseEntity.getBody()).isEqualTo(subwayResponse);
        Assertions.assertThat(subwayResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    @DisplayName("전체 역 조회 테스트")
    void getStations2() {
        // given
        final List<StationResponse> stationResponses = List.of(new StationResponse(1L, "교대역"),
                new StationResponse(2L, "강남역"),
                new StationResponse(3L, "역삼역"));
        final LineResponse lineResponse = new LineResponse(1L, "2호선", "초록색");
        final SubwayResponse subwayResponse = new SubwayResponse(lineResponse, stationResponses);
        final SubwayService subwayServiceMock = mock(SubwayService.class);
        final SubwayController subwayController = new SubwayController(subwayServiceMock);
        // when
        when(subwayServiceMock.findAllStations()).thenReturn(List.of(subwayResponse));
        final ResponseEntity<List<SubwayResponse>> subwayResponseEntity = subwayController.getStations();
        
        // then
        Assertions.assertThat(subwayResponseEntity.getBody()).isEqualTo(List.of(subwayResponse));
        Assertions.assertThat(subwayResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}