package subway.line.adapter.input.web.unit;

import config.TestConfig;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
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
import subway.line.adapter.input.web.DeleteLineController;
import subway.line.application.port.input.DeleteLineUseCase;

import static org.hamcrest.Matchers.is;

@SuppressWarnings("NonAsciiCharacters")
@ContextConfiguration(classes = TestConfig.class)
@WebMvcTest(DeleteLineController.class)
class DeleteLineControllerTest {
    @MockBean
    private DeleteLineUseCase useCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new DeleteLineController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void lineId로_노선을_삭제한다() {
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().delete("/lines/1")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.NO_CONTENT);
    }
    
    @Test
    void lineId가_null일_시_예외_발생() {
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().delete("/lines/" + null)
                .then().log().all()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("message", is("[ERROR] 서버가 응답할 수 없습니다."));
    }
}
