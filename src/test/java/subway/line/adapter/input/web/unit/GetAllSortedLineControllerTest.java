package subway.line.adapter.input.web.unit;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import subway.advice.GlobalExceptionHandler;
import subway.line.adapter.input.web.GetAllSortedLineController;
import subway.line.application.port.input.GetAllSortedLineUseCase;
import subway.line.dto.GetAllSortedLineResponse;
import subway.line.dto.GetSortedLineResponse;

import java.util.List;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@WebMvcTest(GetAllSortedLineController.class)
class GetAllSortedLineControllerTest {
    @MockBean
    private GetAllSortedLineUseCase useCase;
    
    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.standaloneSetup(
                MockMvcBuilders.standaloneSetup(new GetAllSortedLineController(useCase))
                        .setControllerAdvice(new GlobalExceptionHandler())
        );
    }
    
    @Test
    void 모든_노선의_역을_정렬하기() {
        // given
        final GetAllSortedLineResponse response =
                new GetAllSortedLineResponse(List.of(new GetSortedLineResponse("1호선", "파랑", List.of("잠실역", "청라역", "선릉역"))));
        given(useCase.getAllSortedLine()).willReturn(response);
        
        // expect
        RestAssuredMockMvc.given().log().all()
                .when().get("/lines")
                .then().log().all()
                .contentType(ContentType.JSON)
                .status(HttpStatus.OK)
                .body("allSortedLines.size()", is(1))
                .body("allSortedLines[0].lineName", is("1호선"))
                .body("allSortedLines[0].lineColor", is("파랑"))
                .body("allSortedLines[0].sortedStations", contains("잠실역", "청라역", "선릉역"));
    }
}
