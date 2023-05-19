package subway.line.adapter.input.web.unit;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.line.adapter.input.web.DeleteLineController;
import subway.line.application.port.input.DeleteLineUseCase;

import static org.hamcrest.Matchers.is;

@SuppressWarnings("NonAsciiCharacters")
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
    void lineId가_숫자가_아닐_시_예외_발생() {
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().delete("/lines/abc")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 파라미터 타입과 일치하지 않습니다."));
    }
}
