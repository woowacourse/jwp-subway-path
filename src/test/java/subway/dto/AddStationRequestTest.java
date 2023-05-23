package subway.dto;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import subway.application.SectionService;
import subway.domain.vo.Section;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AddStationRequestTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @MockBean
    private SectionService sectionService;

    @ParameterizedTest(name = "{0} 요청시 실패")
    @MethodSource("invalidParameterProvider")
    void invalidRequestTest(final String name, final String departure, final String arrival, final int distance, final String error) throws IllegalAccessException {
        final AddStationRequest addStationRequest = new AddStationRequest(departure, arrival, distance);
        final List<Section> mockSections = new ArrayList<>();
        Mockito.lenient().when(sectionService.addStationByLineId(1l, departure, arrival, distance)).thenReturn(mockSections);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addStationRequest)
                .when().post("/lines/1/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is(error));

    }

    private static Stream<Arguments> invalidParameterProvider() {
        return Stream.of(
                Arguments.of("거리 음수", "봉천", "신림", -1, "거리는 양의 정수여야 합니다."),
                Arguments.of("거리 0", "사당", "방배", 0, "거리는 양의 정수여야 합니다."),
                Arguments.of("출발역 빈값", "", "봉천", 1, "출발역은 공백이면 안됩니다."),
                Arguments.of("출발역 공백", " ", "신봉천", 1, "출발역은 공백이면 안됩니다."),
                Arguments.of("출발역 null", null, "신봉천", 1, "출발역은 공백이면 안됩니다."),
                Arguments.of("도착역 빈값", "봉천", "", 1, "도착역은 공백이면 안됩니다."),
                Arguments.of("도착역 공백", "봉천", " ", 1, "도착역은 공백이면 안됩니다."),
                Arguments.of("도착역 null", "봉천", null, 1, "도착역은 공백이면 안됩니다.")

        );
    }

    @Test
    @DisplayName("제대로된 요청일 경우 성공하는 테스트")
    void successTest() throws IllegalAccessException {
        final AddStationRequest addStationRequest = new AddStationRequest("신대방", "신림", 1);
        final List<Section> mockSections = new ArrayList<>();
        Mockito.lenient().when(sectionService.addStationByLineId(1l, "신대방", "신림", 1)).thenReturn(mockSections);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addStationRequest)
                .when().post("/lines/1/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}
