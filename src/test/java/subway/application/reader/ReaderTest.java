package subway.application.reader;

import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
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
import subway.application.exception.AddSectionException;
import subway.dao.SectionDao;
import subway.domain.vo.Distance;
import subway.domain.vo.Section;
import subway.domain.vo.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReaderTest {

    @LocalServerPort
    private int port;
    @MockBean
    private SectionDao sectionDao;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
        setMockSectionDao();
    }

    @ParameterizedTest(name = "{0} 타입 선정 테스트")
    @MethodSource("validParameterProvider")
    void caseTypeSetterTest(final String name, final String departure, final String arrival, final int distance, final List<Section> sections, final CaseType caseType) {
        CaseDto caseDto = new CaseDto.Builder()
                .lineId(1l)
                .departure(departure)
                .arrival(arrival)
                .distance(distance)
                .build();
        Assertions.assertThat(CaseTypeSetter.setCase(caseDto, sections).getCaseType()).isEqualTo(caseType);
    }

    private static Stream<Arguments> validParameterProvider() {
        return Stream.of(
                Arguments.of("첫 등록시", "사당", "신림", 1, new ArrayList(), CaseType.NON_DELETE_SAVE_CASE),
                Arguments.of("상행 종점 추가시", "신대방", "신림", 1, setSectionList(), CaseType.NON_DELETE_SAVE_CASE),
                Arguments.of("하행 종점 추가시", "사당", "방배", 1, setSectionList(), CaseType.NON_DELETE_SAVE_CASE),
                Arguments.of("상행 중간 추가시", "봉천", "신봉천", 1, setSectionList(), CaseType.UPPER),
                Arguments.of("하행 중간 추가시", "신봉천", "서울대입구", 1, setSectionList(), CaseType.LOWER),
                Arguments.of("순환 노선 추가시", "사당", "신림", 1, setSectionList(), CaseType.EXCEPTION_CASE),
                Arguments.of("이미 존재하는 구간 추가시", "신림", "서울대입구", 1, setSectionList(), CaseType.EXCEPTION_CASE),
                Arguments.of("없는 역으로 추가 시", "신신림", "신봉천", 1, setSectionList(), CaseType.EXCEPTION_CASE)
        );
    }

    @Test
    @DisplayName("삭제없이 단순 저장 성공 테스트")
    void nonDeleteSaveTest() throws IllegalAccessException {
        CaseDto caseDto = new CaseDto.Builder()
                .lineId(1l)
                .departure("신대방")
                .arrival("신림")
                .distance(1)
                .caseType(CaseType.NON_DELETE_SAVE_CASE)
                .build();

        Reader reader = new NonDeleteSaveCase(sectionDao);
        Assertions.assertThat(reader.save(caseDto)).hasSize(1);
    }

    @Test
    @DisplayName("상행 저장 성공 테스트")
    void upperSaveTest() throws IllegalAccessException {
        CaseDto caseDto = new CaseDto.Builder()
                .lineId(1l)
                .departure("신대방")
                .arrival("신림")
                .distance(1)
                .caseType(CaseType.UPPER)
                .deleteSection(new Section(1l, "신신대방", "신림", 2))
                .build();

        Reader reader = new NonDeleteSaveCase(sectionDao);
        Assertions.assertThat(reader.save(caseDto)).hasSize(2);
    }

    @Test
    @DisplayName("하행 저장 성공 테스트")
    void lowerSaveTest() throws IllegalAccessException {
        CaseDto caseDto = new CaseDto.Builder()
                .lineId(1l)
                .departure("신대방")
                .arrival("신림")
                .distance(1)
                .caseType(CaseType.LOWER)
                .deleteSection(new Section(1l, "신신대방", "신림", 2))
                .build();

        Reader reader = new NonDeleteSaveCase(sectionDao);
        Assertions.assertThat(reader.save(caseDto)).hasSize(2);
    }

    @Test
    @DisplayName("예외 케이스 실패")
    void exceptionTest() {
        CaseDto caseDto = new CaseDto.Builder()
                .caseType(CaseType.EXCEPTION_CASE)
                .build();
        Reader reader = new ExceptionCase(sectionDao);
        Assertions.assertThatThrownBy(() -> reader.save(caseDto))
                .isInstanceOf(AddSectionException.class)
                .hasMessage("역을 추가할 수 없습니다.");
    }

    private static List<Section> setSectionList() {
        return List.of(
                new Section(1l, new Station(1l, "신림"), new Station(2l, "봉천"), new Distance(2)),
                new Section(1l, new Station(2l, "봉천"), new Station(3l, "서울대입구"), new Distance(2)),
                new Section(1l, new Station(3l, "서울대입구"), new Station(4l, "낙성대"), new Distance(2)),
                new Section(1l, new Station(4l, "낙성대"), new Station(5l, "사당"), new Distance(2))
        );
    }

    private void setMockSectionDao() {
        Mockito.lenient().when(sectionDao.saveSection(1l, 1, "신대방", "신림")).thenReturn(1l);
    }
}
