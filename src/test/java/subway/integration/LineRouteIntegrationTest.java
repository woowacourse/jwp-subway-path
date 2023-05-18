package subway.integration;

import io.restassured.RestAssured;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dto.LineRequest;
import subway.dto.LineRouteResponse;
import subway.dto.SectionRequest;
import subway.dto.StationRequest;
import subway.dto.StationResponse;
import subway.util.Request;

public class LineRouteIntegrationTest extends IntegrationTest {
    
    private long lastStationId;
    private long lastLineId;
    private long lastSectionId;
    
    @BeforeEach
    void setSection() {
        final LineRequest lineRequest = new LineRequest("테스트 호선", "blue");
        this.lastLineId = Request.postLineRequest(lineRequest);
        
        final StationRequest stationRequest = new StationRequest("테스트역");
        this.lastStationId = Request.postStationRequest(stationRequest);
        
        final StationRequest stationRequest2 = new StationRequest("다음 테스트역");
        final long nextStationId = Request.postStationRequest(stationRequest2);
        
        final SectionRequest sectionRequest = new SectionRequest(this.lastLineId, nextStationId, this.lastStationId,
                "DOWN",
                1);
        this.lastSectionId = Request.postSectionRequest(sectionRequest);
        
        final StationRequest stationRequest3 = new StationRequest("맨 앞 테스트역");
        final long nextNextStationId = Request.postStationRequest(stationRequest3);
        
        final SectionRequest sectionRequest2 = new SectionRequest(this.lastLineId, nextNextStationId,
                this.lastStationId,
                "UP", 1);
        Request.postSectionRequest(sectionRequest2);
    }
    
    @DisplayName("한 노선의 지하철 역들을 순서대로 출력한다")
    @Test
    void findAllStationsInLine() {
        final LineRouteResponse lineRouteResponse = RestAssured.given().log().all()
                .when().get("/lines/" + this.lastLineId + "/stations")
                .then().log().all()
                .extract().as(LineRouteResponse.class);
        
        final List<StationResponse> orderedStations = lineRouteResponse.getStations();
        Assertions.assertThat(orderedStations.size()).isEqualTo(3);
        Assertions.assertThat(orderedStations.get(0).getName()).isEqualTo("맨 앞 테스트역");
        Assertions.assertThat(orderedStations.get(1).getName()).isEqualTo("테스트역");
        Assertions.assertThat(orderedStations.get(2).getName()).isEqualTo("다음 테스트역");
        
        
    }
    
    @DisplayName("지하철 노선도를 출력한다")
    @Test
    void findAllLines() {
        final List<LineRouteResponse> lineRouteResponse = RestAssured.given().log().all()
                .when().get("/lines/stations")
                .then().log().all()
                .extract().jsonPath().getList(".", LineRouteResponse.class);
        final Optional<Long> response = lineRouteResponse.stream()
                .filter(subway -> subway.getLine().getId() == this.lastLineId)
                .findFirst()
                .map(subway -> {
                    return subway.getLine().getId();
                });
        Assertions.assertThat(response.isPresent()).isTrue();
    }
    
}
