package subway.application;

import io.restassured.RestAssured;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import subway.dao.SectionDao;
import subway.domain.vo.Section;
import subway.dto.RouteResponse;

import java.util.List;
import java.util.stream.Stream;

import static subway.fixture.Fixture.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RouteServiceTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @MockBean
    private SectionDao sectionDao;
    @Autowired
    private RouteService routeService;

    @ParameterizedTest(name = "{0} {1}원인지 테스트")
    @MethodSource("shortcutProvider")
    void findShortcut(final String name, final String feeName, final long departure, final long arrival, final RouteResponse routeResponse) {
        List<Section> allSections = List.of(SILIM_BONCHUN, BONCHUN_SEOUL, SEOUL_NAKSUNG, NAKSUNG_SADANG, SILIM_NODLE, NODLE_EESU, EESU_SADANG, SILIM_KM_9, SILIM_KM_10, SILIM_KM_11, SILIM_KM_16, SILIM_KM_58);
        Mockito.lenient().when(sectionDao.findAllSections()).thenReturn(allSections);
        RouteResponse actual = routeService.findShortcut(departure, arrival);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(actual.getFee()).isEqualTo(routeResponse.getFee());
            softly.assertThat(actual.getRoute()).isEqualTo(routeResponse.getRoute());
        });
    }

    private static Stream<Arguments> shortcutProvider() {
        return Stream.of(
                Arguments.of("최소 경로가 ", "1250", 1l, 5l, new RouteResponse(List.of(SINLIM.getName(), BONGCHUN.getName(), SEOUL.getName(), NAKSUNG.getName(), SADANG.getName()), 1250)),
                Arguments.of("9키로미터 요금이", "1250", 1l, 8l, new RouteResponse(List.of(SINLIM.getName(), "9키로"), 1250)),
                Arguments.of("10키로미터 요금이", "1250", 1l, 9l, new RouteResponse(List.of(SINLIM.getName(), "10키로"), 1250)),
                Arguments.of("11키로미터 요금이", "1350", 1l, 10l, new RouteResponse(List.of(SINLIM.getName(), "11키로"), 1350)),
                Arguments.of("16키로미터 요금이", "1450", 1l, 11l, new RouteResponse(List.of(SINLIM.getName(), "16키로"), 1450)),
                Arguments.of("58키로미터 요금이", "2150", 1l, 12l, new RouteResponse(List.of(SINLIM.getName(), "58키로"), 2150))
        );
    }
}
