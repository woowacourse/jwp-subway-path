package subway.path.adapter.input.web;

import config.TestConfig;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.path.application.port.input.GetShortestPathUseCase;
import subway.path.dto.GetShortestPathResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.startsWith;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(GetShortestPathController.class)
class GetShortestPathControllerTest {
    @MockBean
    private GetShortestPathUseCase useCase;
    
    @BeforeEach
    void setUp() {
        final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new GetShortestPathController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler(logger))
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
                .when().get("/path")
                .then().log().all()
                .contentType(ContentType.JSON)
                .status(HttpStatus.OK)
                .body("shortestPath", contains("잠실역", "청라역", "계양역"))
                .body("shortestDistance", is(59))
                .body("fee", is(2250));
    }
}
