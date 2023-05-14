package subway.line.adapter.input.web;

import config.TestConfig;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.line.application.port.input.AddLineUseCase;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@SuppressWarnings("NonAsciiCharacters")
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(AddLineController.class)
class AddLineControllerTest {
    @MockBean
    private AddLineUseCase addLineUseCase;
    
    @BeforeEach
    void setUp() {
        final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new AddLineController(addLineUseCase))
                        .setControllerAdvice(new GlobalExceptionHandler(logger))
        );
    }
    
    @Test
    void 노선_추가하기() {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", "파랑");
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .header("Location", startsWith("/lines/"));
    }
    
    @ParameterizedTest(name = "{displayName} : name = {0}")
    @NullAndEmptySource
    void 이름이_null_또는_empty일_수_없다(final String name) {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", "파랑");
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 노선의 이름은 null 또는 빈값일 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : color = {0}")
    @NullAndEmptySource
    void 색상이_null_또는_empty일_수_없다(final String color) {
        // given
        final Map<String, String> params = new HashMap<>();
        params.put("name", "1호선");
        params.put("color", color);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/lines")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .contentType(ContentType.JSON)
                .body("message", is("[ERROR] 노선의 색상은 null 또는 빈값일 수 없습니다."));
    }
}
