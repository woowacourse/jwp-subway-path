package subway.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.willReturn;
import static subway.fixture.PathRequestFixture.PATH_REQUEST_1_3;
import static subway.fixture.SectionFixture.SECTION_강남_잠실_5;
import static subway.fixture.SectionFixture.SECTION_길동_암사_3;
import static subway.fixture.SectionFixture.SECTION_몽촌토성_길동_2;
import static subway.fixture.SectionFixture.SECTION_잠실_몽촌토성_5;
import static subway.fixture.StationFixture.STATION_강남;
import static subway.fixture.StationFixture.STATION_몽촌토성;
import static subway.fixture.StationFixture.STATION_잠실;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.fare.Fare;
import subway.domain.fare.FareCalculator;
import subway.dto.StationDto;
import subway.dto.response.PathResponse;
import subway.repository.PathRepository;

@WebMvcTest(PathService.class)
class PathServiceTest {

    private final List<Section> sections = List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_길동_2,
            SECTION_길동_암사_3);

    @Autowired
    private PathService pathService;

    @MockBean
    FareCalculator fareCalculator;

    @MockBean
    private PathRepository pathRepository;

    @Test
    @DisplayName("역간 경로와 거리를 구한다.")
    void findPath() {
        // given
        willReturn(new Sections(sections)).given(pathRepository).findAllSections();
        willReturn(STATION_강남).given(pathRepository).findStationById(1L);
        willReturn(STATION_몽촌토성).given(pathRepository).findStationById(3L);
        willReturn(new Fare(1_250)).given(fareCalculator).calculate(anyInt());

        // when
        PathResponse path = pathService.findPath(PATH_REQUEST_1_3);

        // then
        assertThat(path)
                .usingRecursiveComparison()
                .isEqualTo(new PathResponse(
                        List.of(StationDto.from(STATION_강남), StationDto.from(STATION_잠실),
                                StationDto.from(STATION_몽촌토성)),
                        10,
                        1_250));
    }
}
