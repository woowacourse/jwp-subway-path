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

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;

@SuppressWarnings("NonAsciiCharacters")
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(GetSortedLineController.class)
class GetSortedLineControllerTest { // TODO
    @MockBean
    private GetSortedLineUseCase useCase;
    
    @BeforeEach
    void setUp() {
        final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new GetSortedLineController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler(logger))
        );
    }
    
    @Test
    void lineId로_해당_노선의_정렬된_역들을_가져오기() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("lines")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.OK);
    }
    
    @Test
    void lineId가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", null);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().get("lines")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] lineId는 null일 수 없습니다."));
    }
}
