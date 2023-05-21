package subway.shortestpathfinder.adapter.input.web.unit;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.shortestpathfinder.adapter.input.web.GetShortestPathController;
import subway.shortestpathfinder.application.port.input.GetShortestPathUseCase;
import subway.shortestpathfinder.dto.GetShortestPathResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(GetShortestPathController.class)
class GetShortestPathControllerTest {
    @MockBean
    private GetShortestPathUseCase useCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new GetShortestPathController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void 최단_경로를_조회한다() {
        // given
        final List<String> shortestPath = List.of("잠실역", "청라역", "계양역");
        final Long shortestDistance = 59L;
        final Long fee = 2250L;
        final GetShortestPathResponse shortestPathResponse = new GetShortestPathResponse(shortestPath, shortestDistance, fee);
        given(useCase.getShortestPath("잠실역", "계양역")).willReturn(shortestPathResponse);
        
        // expect
        final Map<String, Object> params = new HashMap<>();
        params.put("startStationName", "잠실역");
        params.put("endStationName", "계양역");
        
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/shortest-path")
                .then().log().all()
                .contentType(ContentType.JSON)
                .status(HttpStatus.OK)
                .body("shortestPath", contains("잠실역", "청라역", "계양역"))
                .body("shortestDistance", is(59))
                .body("fee", is(2250));
    }
    
    @ParameterizedTest(name = "{displayName} : startStationName = {0}")
    @NullAndEmptySource
    void 최단_경로를_조회시_startStaionName이_null_또는_empty이면_예외_발생(final String startStationName) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("startStationName", startStationName);
        params.put("endStationName", "계양역");
        
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/shortest-path")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);
    }
    
    @ParameterizedTest(name = "{displayName} : endStationName = {0}")
    @NullAndEmptySource
    void 최단_경로를_조회시_endStaionName이_null_또는_empty이면_예외_발생(final String endStationName) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("startStationName", "강남역");
        params.put("endStationName", endStationName);
        
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("/shortest-path")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.BAD_REQUEST);
    }
}
