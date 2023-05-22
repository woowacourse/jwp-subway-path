package subway.station.adapter.input.web.unit;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.station.adapter.input.web.AddStationController;
import subway.station.application.port.input.AddStationUseCase;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(AddStationController.class)
class AddStationControllerTest {
    @MockBean
    private AddStationUseCase addStationUseCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new AddStationController(addStationUseCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void 해당_노선에_역을_등록한다() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("baseStation", "잠실역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 3L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .header("Location", startsWith("/stations"));
    }
    
    @Test
    void 역_등록시_lineId가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", null);
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] lineId는 null일 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : baseStation = {0}")
    @NullAndEmptySource
    void 역_등록시_기준역이_null_또는_empty이면_예외_발생(final String baseStation) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("baseStation", baseStation);
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 기준역은 null 또는 empty일 수 없습니다."));
    }
    
    @Test
    void 역_등록시_방향이_LEFT_또는_RIGHT가_아니면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("baseStation", "선릉역");
        params.put("direction", "right");
        params.put("additionalStation", "청라역");
        params.put("distance", 5L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 해당 파라미터로 변환할 수 없습니다."));
    }
    
    @ParameterizedTest(name = "{displayName} : additionalStation = {0}")
    @NullAndEmptySource
    void 역_등록시_등록할_역이_null_또는_empty이면_예외_발생(final String additionalStation) {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", additionalStation);
        params.put("distance", 5L);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 등록할 역은 null 또는 empty일 수 없습니다."));
    }
    
    @Test
    void 역_등록시_거리가_null이면_예외_발생() {
        // given
        final Map<String, Object> params = new HashMap<>();
        params.put("lineId", 1L);
        params.put("baseStation", "선릉역");
        params.put("direction", "RIGHT");
        params.put("additionalStation", "청라역");
        params.put("distance", null);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/stations")
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 거리는 null일 수 없습니다."));
    }
}
