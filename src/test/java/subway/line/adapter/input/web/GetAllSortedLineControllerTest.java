package subway.line.adapter.input.web;

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
import subway.line.application.port.input.GetAllSortedLineUseCase;
import subway.line.dto.GetAllSortedLineResponse;
import subway.line.dto.GetSortedLineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.contains;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(GetAllSortedLineController.class)
class GetAllSortedLineControllerTest {
    @MockBean
    private GetAllSortedLineUseCase useCase;
    
    @BeforeEach
    void setUp() {
        final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new GetAllSortedLineController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler(logger))
        );
    }
    
    @Test
    void 모든_노선의_역을_정렬하기() {
        // given
        final GetAllSortedLineResponse response = new GetAllSortedLineResponse(List.of(new GetSortedLineResponse(List.of("잠실역", "청라역", "선릉역"))));
        given(useCase.getAllSortedLine()).willReturn(response);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .contentType(ContentType.JSON)
                .status(HttpStatus.OK)
                .body("allSortedLines", hasSize(1))
                .body("allSortedLines[0].sortedStations", contains("잠실역", "청라역", "선릉역"));
    }
}
