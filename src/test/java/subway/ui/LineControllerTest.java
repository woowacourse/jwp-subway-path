package subway.ui;

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
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.domain.vo.*;
import subway.dto.AddStationRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class LineControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @MockBean
    private LineDao lineDao;

    @MockBean
    private SectionDao sectionDao;

    @Test
    @DisplayName("전체 역 조회 테스트")
    void findAllLines() {
        setMockSectionsMap();

        Mockito.lenient().when(sectionDao.findSections()).thenReturn(setMockSectionsMap());
        RestAssured.given()
                .when().get("/lines")
                .then().log().all();
    }

    @Test
    @DisplayName("LineId로 노선을 찾는 테스트")
    void findLineById() {
        List<Section> sections = List.of(new Section(1l, new Station(1l, "a"), new Station(1l, "b"), new Distance(1)));
        Mockito.lenient().when(lineDao.findById(1l)).thenReturn(new Line(1l, "testName", "testColor"));
        Mockito.lenient().when(sectionDao.findSectionsByLineId(1l)).thenReturn(sections);
        RestAssured.given()
                .when().get("/lines/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }


    @ParameterizedTest(name = "{0} 성공 테스트")
    @MethodSource("validParameterProvider")
    void addStationSuccessTest(final String name, final String departure, final String arrival, final long sectionId) {

        final AddStationRequest addStationRequest = new AddStationRequest(departure, arrival, 1);

        Mockito.lenient().when(sectionDao.findSectionsByLineId(1l)).thenReturn(setMockSectionList());
        Mockito.lenient().when(sectionDao.saveSection(1l, 1, departure, arrival)).thenReturn(sectionId);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addStationRequest)
                .when().post("/lines/1/stations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    private static Stream<Arguments> validParameterProvider() {
        return Stream.of(
                Arguments.of("상행 종점 추가", "신대방", "신림", 1l),
                Arguments.of("하행 종점 추가", "사당", "방배", 4l),
                Arguments.of("상행 중간 추가", "신봉천", "봉천", 1l),
                Arguments.of("하행 중간 추가", "봉천", "신봉천", 2l)
        );
    }

    @ParameterizedTest(name = "{0} 실패 테스트")
    @MethodSource("invalidParameterProvider")
    void addStationFailTest(final String name, final String departure, final String arrival, final long sectionId, final int distance) {

        final AddStationRequest addStationRequest = new AddStationRequest(departure, arrival, distance);

        Mockito.lenient().when(sectionDao.findSectionsByLineId(1l)).thenReturn(setMockSectionList());
        Mockito.lenient().when(sectionDao.saveSection(1l, 1, departure, arrival)).thenReturn(sectionId);
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(addStationRequest)
                .when().post("/lines/1/stations")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    private static Stream<Arguments> invalidParameterProvider() {
        return Stream.of(
                Arguments.of("순환 추가", "사당", "신림", 1l, 1),
                Arguments.of("역순환 추가", "신림", "사당", 4l, 1),
                Arguments.of("둘다 존재하지 않는 경우 추가", "신신촌", "봉봉천", 1l, 1),
                Arguments.of("길이가 더 긴 경우", "봉천", "신봉천", 2l, 2)
        );
    }

    @Test
    void deleteTerminalStation() {
        Mockito.lenient().when(sectionDao.findSectionByLineIdAndStationId(1l, 1l)).thenReturn(new RequestInclusiveSections(List.of(new Section(1l, new Station(1l, "신림"), new Station(1l, "봉천"), new Distance(1)))));
        RestAssured.given()
                .when().delete("/lines/1/stations/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void deleteNonTerminalStation() {
        Mockito.lenient()
                .when(sectionDao.findSectionByLineIdAndStationId(1l, 2l))
                .thenReturn(new RequestInclusiveSections(List.of(new Section(1l, new Station(1l, "신림"), new Station(1l, "봉천"), new Distance(1)),
                        new Section(2l, new Station(1l, "봉천"), new Station(1l, "서울대입구"), new Distance(1))
                )));
        Mockito.lenient().when(sectionDao.saveSection(1l, 2, "신림", "서울대입구")).thenReturn(3l);
        RestAssured.given()
                .when().delete("/lines/1/stations/2")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    private List<Section> setMockSectionList() {
        return List.of(
                new Section(1l, new Station(1l, "신림"), new Station(2l, "봉천"), new Distance(2)),
                new Section(1l, new Station(2l, "봉천"), new Station(3l, "서울대입구"), new Distance(2)),
                new Section(1l, new Station(3l, "서울대입구"), new Station(4l, "낙성대"), new Distance(2)),
                new Section(1l, new Station(4l, "낙성대"), new Station(5l, "사당"), new Distance(2))
        );
    }

    private Map<Line, List<Section>> setMockSectionsMap() {
        List<Section> sections = setMockSectionList();


        Map<Line, List<Section>> mockSectionMap = new HashMap<>();
        mockSectionMap.put(new Line(1l, "1호선", "파랑"), sections);
        mockSectionMap.put(new Line(2l, "2호선", "초록"), sections);
        return mockSectionMap;
    }


}
