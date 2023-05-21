package subway.line.adapter.input.web.unit;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.line.adapter.input.web.GetSortedLineController;
import subway.line.application.port.input.GetSortedLineUseCase;
import subway.line.dto.GetSortedLineResponse;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
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
        given(useCase.getSortedLine(1L)).willReturn(new GetSortedLineResponse("1호선", "파랑", List.of("잠실역", "선릉역")));
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().get("/lines/1")
                .then().log().all()
                .assertThat()
                .status(HttpStatus.OK)
                .body("lineName", is("1호선"))
                .body("lineColor", is("파랑"))
                .body("sortedStations", contains("잠실역", "선릉역"));
    }
    
    @Test
    void lineId가_null이면_예외_발생() {
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().get("/lines/" + null)
                .then().log().all()
                .status(HttpStatus.BAD_REQUEST)
                .body("message", is("[ERROR] 파라미터 타입과 일치하지 않습니다."));
    }
}
