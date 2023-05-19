package subway.station.adapter.input.web.unit;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.station.adapter.input.web.DeleteStationOnAllLinesController;
import subway.station.application.port.input.DeleteStationOnAllLineUseCase;

import static org.hamcrest.Matchers.is;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(DeleteStationOnAllLinesController.class)
class DeleteStationOnAllLinesControllerTest {
    @MockBean
    private DeleteStationOnAllLineUseCase useCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new DeleteStationOnAllLinesController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void 모든_노선의_해당_역을_삭제한다() {
        // expect
        // 청라 - 잠실 - 선릉
        RestAssuredMockMvc.given().log().all()
                .when().delete("/stations/1")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.NO_CONTENT);
    }
    
    @Test
    void stationId가_null이면_예외_발생() {
        // expect
        // 잠실 - 청라 - 선릉
        RestAssuredMockMvc.given().log().all()
                .when().delete("/stations/" + null)
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 파라미터 타입과 일치하지 않습니다."));
    }
}
