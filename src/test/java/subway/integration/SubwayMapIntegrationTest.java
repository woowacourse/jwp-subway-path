package subway.integration;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dto.*;

import java.util.List;

import static subway.fixture.IntegrationPostRequestFixture.*;

public class SubwayMapIntegrationTest extends IntegrationTest {
    
    @BeforeEach
    void setSubway() {
        
        final LineRequest lineRequest1 = new LineRequest("2호선", "blue");
        addLineRequest(lineRequest1);
        
        final StationRequest stationRequest = new StationRequest("강남역");
        addStationRequest(stationRequest);
        
        final StationRequest stationRequest2 = new StationRequest("잠실역");
        addStationRequest(stationRequest2);
        
        final StationRequest stationRequest3 = new StationRequest("성수역");
        addStationRequest(stationRequest3);
        
        final StationRequest stationRequest4 = new StationRequest("삼성역");
        addStationRequest(stationRequest4);

        final StationRequest stationRequest5 = new StationRequest("잠실새내역");
        addStationRequest(stationRequest5);
        
        final SectionRequest sectionRequest = new SectionRequest(1, 2, 1, "DOWN", 4);
        addSectionRequest(sectionRequest);
        
        final SectionRequest sectionRequest2 = new SectionRequest(1, 3, 1, "DOWN", 1);
        addSectionRequest(sectionRequest2);
        
        final SectionRequest sectionRequest3 = new SectionRequest(1, 4, 3, "DOWN", 1);
        addSectionRequest(sectionRequest3);
        
        final SectionRequest sectionRequest4 = new SectionRequest(1, 5, 4, "DOWN", 1);
        addSectionRequest(sectionRequest4);
        
        
    }
    
    @DisplayName("한 노선의 지하철 역들을 순서대로 출력한다")
    @Test
    void findAllStationsInLine() {
        final SubwayMapResponse subwayMapResponse = RestAssured.given().log().all()
                .when().get("/lines/1/stations")
                .then().log().all()
                .extract().as(SubwayMapResponse.class);
        
        final List<StationResponse> orderedStations = subwayMapResponse.getStationResponses();
        Assertions.assertThat(orderedStations.size()).isEqualTo(5);
        Assertions.assertThat(orderedStations.get(0).getName()).isEqualTo("강남역");
        Assertions.assertThat(orderedStations.get(1).getName()).isEqualTo("성수역");
        Assertions.assertThat(orderedStations.get(2).getName()).isEqualTo("삼성역");
        Assertions.assertThat(orderedStations.get(3).getName()).isEqualTo("잠실새내역");
        Assertions.assertThat(orderedStations.get(4).getName()).isEqualTo("잠실역");
        
    }
    
    @DisplayName("지하철 노선도를 출력한다")
    @Test
    void findAllLines() {
        final StationRequest stationRequest1 = new StationRequest("몽촌토성역");
        final StationRequest stationRequest2 = new StationRequest("석촌역");
        final LineRequest line = new LineRequest("8호선", "pink");
        
        addStationRequest(stationRequest1);
        addStationRequest(stationRequest2);
        
        addLineRequest(line);
        
        final SectionRequest sectionRequest1 = new SectionRequest(2, 6, 2, "UP", 1);
        final SectionRequest sectionRequest2 = new SectionRequest(2, 7, 2, "DOWN", 1);
        
        addSectionRequest(sectionRequest1);
        addSectionRequest(sectionRequest2);
        
        final List<SubwayMapResponse> subwayMapResponse = RestAssured.given().log().all()
                .when().get("/lines/stations")
                .then().log().all()
                .extract().jsonPath().getList(".", SubwayMapResponse.class);
        
        Assertions.assertThat(subwayMapResponse.size()).isEqualTo(2);
        Assertions.assertThat(subwayMapResponse.get(0).getLineResponse().getName()).isEqualTo("2호선");
        Assertions.assertThat(subwayMapResponse.get(0).getStationResponses().size()).isEqualTo(5);
        Assertions.assertThat(subwayMapResponse.get(1).getLineResponse().getName()).isEqualTo("8호선");
        Assertions.assertThat(subwayMapResponse.get(1).getStationResponses().size()).isEqualTo(3);
    }
    
    @DisplayName("만약 노선에 역이 아무것도 없는 경우 아무 역도 출력하지 않는다")
    @Test
    void findAllStationsInLineWithNoStations() {
        final LineRequest lineRequest = new LineRequest("3호선", "green");
        addLineRequest(lineRequest);
        
        final SubwayMapResponse subwayMapResponse = RestAssured.given().log().all()
                .when().get("/lines/2/stations")
                .then().log().all()
                .extract().as(SubwayMapResponse.class);
        
        final List<StationResponse> orderedStations = subwayMapResponse.getStationResponses();
        Assertions.assertThat(orderedStations.size()).isEqualTo(0);
    }
    

}
