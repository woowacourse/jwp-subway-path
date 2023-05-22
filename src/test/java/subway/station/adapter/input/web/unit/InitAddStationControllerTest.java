package subway.station.adapter.input.web.unit;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.station.adapter.input.web.InitAddStationController;
import subway.station.application.port.input.InitAddStationUseCase;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(InitAddStationController.class)
class InitAddStationControllerTest {
    @MockBean
    private InitAddStationUseCase initAddStationUseCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new InitAddStationController(initAddStationUseCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void 지정한_노선에_최초로_역을_추가한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 3L);
        params.put("lineId", 1L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .header("Location", is("/stations"));
    }
    
    @ParameterizedTest(name = "{displayName} : stationName = {0}")
    @NullAndEmptySource
    void 최초_등록시_왼쪽_역_이름이_null_또는_empty일_경우_예외_발생(final String stationName) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("firstStation", stationName);
        params.put("secondStation", "선릉역");
        params.put("distance", 3L);
        params.put("lineId", 1L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 역 이름은 null 또는 빈값이 올 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : stationName = {0}")
    @NullAndEmptySource
    void 최초_등록시_오른쪽_역_이름이_null_또는_empty일_경우_예외_발생(final String stationName) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("firstStation", "선릉역");
        params.put("secondStation", stationName);
        params.put("distance", 3L);
        params.put("lineId", 1L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 역 이름은 null 또는 빈값이 올 수 없습니다."));
    }
    
    @Test
    void 최초_등록시_거리가_null일_경우_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", null);
        params.put("lineId", 1L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 거리는 null일 수 없습니다."));
    }
    
    @Test
    void 최초_등록시_lineId가_null일_경우_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", 3L);
        params.put("lineId", null);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] lineId는 null일 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : distance = {0}")
    @ValueSource(longs = {-1L, 0L})
    void 최초_등록시_거리가_양수가_아닌_경우_예외_발생(final Long distance) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("firstStation", "잠실역");
        params.put("secondStation", "선릉역");
        params.put("distance", distance);
        params.put("lineId", 1L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations/init")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 거리는 양수여야 합니다."));
    }
}
