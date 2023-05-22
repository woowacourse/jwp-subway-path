package subway.station.adapter.input.web.unit;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.station.adapter.input.web.DeleteStationOnTheLineController;
import subway.station.application.port.input.DeleteStationOnTheLineUseCase;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(DeleteStationOnTheLineController.class)
class DeleteStationOnTheLineControllerTest {
    @MockBean
    private DeleteStationOnTheLineUseCase deleteStationOnTheLineUseCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new DeleteStationOnTheLineController(deleteStationOnTheLineUseCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void 해당_노선의_중간_역을_삭제한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("stationId", 1L);
        
        // expect
        // 잠실 - 청라 - 선릉
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/stations/one")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.NO_CONTENT);
    }
    
    @Test
    void lineId가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", null);
        params.put("stationId", 1L);
        
        // expect
        // 잠실 - 청라 - 선릉
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/stations/one")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] lineId는 null일 수 없습니다."));
    }
    
    @Test
    void stationId가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("stationId", null);
        
        // expect
        // 잠실 - 청라 - 선릉
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/stations/one")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] stationId는 null일 수 없습니다."));
    }
}
