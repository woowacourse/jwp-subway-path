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
import subway.line.application.port.input.GetSortedLineUseCase;
import subway.line.dto.GetSortedLineResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(GetSortedLineController.class)
class GetSortedLineControllerTest {
    @MockBean
    private GetSortedLineUseCase useCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new GetSortedLineController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void lineId로_해당_노선의_정렬된_역들을_가져오기() {
        // given
        given(useCase.getSortedLine(1L)).willReturn(new GetSortedLineResponse(List.of("잠실역", "선릉역")));
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().get("/lines/1")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.OK)
                .body("sortedStations", contains("잠실역", "선릉역"));
    }
    
    @Test
    void lineId가_null이면_예외_발생() {
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().get("/lines/" + null)
                .then().log().all()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("message", is("[ERROR] 서버가 응답할 수 없습니다."));
    }
}
