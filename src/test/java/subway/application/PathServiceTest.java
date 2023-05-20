package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.dao.DataAccessException;
import subway.application.dto.PathsResponse;
import subway.application.dto.StationResponse;
import subway.application.exception.SubwayServiceException;
import subway.config.CustomDataAccessException;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.SectionFixture;
import subway.domain.Station;
import subway.domain.StationFixture;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static subway.domain.SectionFixture.*;
import static subway.domain.StationFixture.*;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private SectionDao sectionDao;

    @Mock
    private StationDao stationDao;

    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(sectionDao, stationDao);
    }

    @DisplayName("최단 경로를 조회할 수 있다.")
    @Test
    void findShortestPaths_success() {
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(6L)).thenReturn(FIXTURE_STATION_6);
        when(sectionDao.findAll()).thenReturn(List.of(SECTION_START, SECTION_MIDDLE_1,
                SECTION_MIDDLE_2, SECTION_MIDDLE_3, SECTION_END));
        StationResponse stationResponse1 = new StationResponse(FIXTURE_STATION_1.getId(), FIXTURE_STATION_1.getName());
        StationResponse stationResponse2 = new StationResponse(FIXTURE_STATION_2.getId(), FIXTURE_STATION_2.getName());
        StationResponse stationResponse3 = new StationResponse(FIXTURE_STATION_3.getId(), FIXTURE_STATION_3.getName());
        StationResponse stationResponse4 = new StationResponse(FIXTURE_STATION_4.getId(), FIXTURE_STATION_4.getName());
        StationResponse stationResponse5 = new StationResponse(FIXTURE_STATION_5.getId(), FIXTURE_STATION_5.getName());
        StationResponse stationResponse6 = new StationResponse(FIXTURE_STATION_6.getId(), FIXTURE_STATION_6.getName());
        List<StationResponse> stationResponses = List.of(stationResponse1, stationResponse2, stationResponse3, stationResponse4, stationResponse5, stationResponse6);

        PathsResponse shortestPaths = pathService.findShortestPaths(1L, 6L);

        PathsResponse expected = PathsResponse.of(stationResponses, 50, 2050);
        assertThat(shortestPaths)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 역을 전달하면 예외가 발생한다.")
    @Test
    void findShortestPaths_fail_noStation() {
        when(stationDao.findById(1L)).thenReturn(FIXTURE_STATION_1);
        when(stationDao.findById(6L)).thenThrow(new CustomDataAccessException(""));

        Assertions.assertThatThrownBy(() -> pathService.findShortestPaths(1L, 6L))
                .isInstanceOf(SubwayServiceException.class)
                .hasMessageContaining("기존에 저장된 역 번호를 입력해주세요.");
    }
}
