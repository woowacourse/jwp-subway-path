package subway.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationResponse;

@JdbcTest
@Import({StationService.class, StationDao.class})
class StationServiceTest {
    
    @Autowired
    private StationService stationService;
    
    @Test
    @DisplayName("역 저장 테스트")
    void saveStationTest() {
        // given
        final int initialSize = this.stationService.findAllStationResponses().size();
        final StationRequest stationRequest = new StationRequest("테스트역");
        // when
        final StationResponse stationResponse = this.stationService.saveStation(stationRequest);
        // then
        final int finalSize = this.stationService.findAllStationResponses().size();
        assertThat(stationResponse).isNotNull();
        assertThat(stationResponse.getName()).isEqualTo(stationRequest.getName());
        System.out.println(stationResponse.getId());
        assertThat(finalSize).isEqualTo(initialSize + 1);
    }
    
    @Test
    @DisplayName("역 수정 테스트")
    void updateStationTest() {
        // given
        final StationRequest stationRequest = new StationRequest("테스트역");
        final StationResponse stationResponse = this.stationService.saveStation(stationRequest);
        // when
        this.stationService.updateStation(stationResponse.getId(), new StationRequest("수정역"));
        // then
        final StationResponse updatedStationResponse = this.stationService.findStationResponseById(
                stationResponse.getId());
        assertThat(updatedStationResponse).isNotNull();
        assertThat(updatedStationResponse.getName()).isEqualTo("수정역");
    }
    
    @Test
    @DisplayName("역 삭제 테스트")
    void deleteStationTest() {
        // given
        final StationRequest stationRequest = new StationRequest("테스트역");
        final StationResponse stationResponse = this.stationService.saveStation(stationRequest);
        final int initialSize = this.stationService.findAllStationResponses().size();
        // when
        this.stationService.deleteStationById(stationResponse.getId());
        // then
        final int finalSize = this.stationService.findAllStationResponses().size();
        assertThat(finalSize).isEqualTo(initialSize - 1);
    }
}