package subway.ui;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import subway.application.StationService;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

class StationControllerTest {
    
    @Test
    @DisplayName("역 생성 테스트")
    void createStation() {
        // given
        final StationService stationServiceMock = mock(StationService.class);
        final StationRequest stationRequest = new StationRequest("테스트역");
        final StationResponse stationResponse = new StationResponse(1L, "테스트역");
        // when
        when(stationServiceMock.saveStation(stationRequest)).thenReturn(
                stationResponse);
        
        final ResponseEntity<StationResponse> stationResponseResponseEntity = new StationController(
                stationServiceMock).createStation(
                stationRequest);
        
        // then
        final Long id = stationResponseResponseEntity.getBody().getId();
        Assertions.assertThat(id).isEqualTo(1L);
        Assertions.assertThat(stationResponseResponseEntity.getBody().getName()).isEqualTo(
                "테스트역");
        Assertions.assertThat(stationResponseResponseEntity.getHeaders().getLocation().getPath()).isEqualTo(
                "/stations/1");
        Assertions.assertThat(stationResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED
        );
    }
    
    @Test
    @DisplayName("역 조회 테스트")
    void showStation() {
        // given
        final StationService stationServiceMock = mock(StationService.class);
        final StationResponse stationResponse = new StationResponse(1L, "테스트역");
        // when
        when(stationServiceMock.findStationResponseById(1L)).thenReturn(
                stationResponse);
        
        final ResponseEntity<StationResponse> stationResponseResponseEntity = new StationController(
                stationServiceMock).showStation(1L);
        
        // then
        final Long id = stationResponseResponseEntity.getBody().getId();
        Assertions.assertThat(id).isEqualTo(1L);
        Assertions.assertThat(stationResponseResponseEntity.getBody().getName()).isEqualTo(
                "테스트역");
        Assertions.assertThat(stationResponseResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    @DisplayName("역 수정 테스트")
    void updateStation() {
        // given
        final StationService stationServiceMock = mock(StationService.class);
        final StationRequest stationRequest = new StationRequest("테스트역");
        // when
        final ResponseEntity<Void> voidResponseEntity = new StationController(
                stationServiceMock).updateStation(1L, stationRequest);
        
        // then
        Assertions.assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
    
    @Test
    @DisplayName("역 삭제 테스트")
    void deleteStation() {
        // given
        final StationService stationServiceMock = mock(StationService.class);
        // when
        final ResponseEntity<Void> voidResponseEntity = new StationController(
                stationServiceMock).deleteStation(1L);
        
        // then
        Assertions.assertThat(voidResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
    
}