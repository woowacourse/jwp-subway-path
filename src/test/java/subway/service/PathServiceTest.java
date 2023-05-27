package subway.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.controller.dto.response.ShortestPathResponse;
import subway.controller.dto.response.StationResponse;
import subway.domain.fee.FeeCalculator;
import subway.domain.fee.NormalFeeCalculator;
import subway.domain.path.DijkstraShortestPathFinder;
import subway.domain.path.ShortestPathFinder;
import subway.entity.SectionDetailEntity;
import subway.entity.StationEntity;
import subway.repository.SectionDao;
import subway.repository.StationDao;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PathServiceTest {

    @Mock
    private StationDao stationDao;
    @Mock
    private SectionDao sectionDao;
    @Spy
    private ShortestPathFinder shortestPathFinder = new DijkstraShortestPathFinder();
    @Spy
    private FeeCalculator feeCalculator = new NormalFeeCalculator();

    @InjectMocks
    private PathService pathService;

    @Test
    @DisplayName("최단 경로 조회 성공")
    void findShortestPath_success() {
        // given
        final long startStationId = 1L;
        final long endStationId = 6L;

        given(sectionDao.findSectionDetail()).willReturn(
                List.of(new SectionDetailEntity(1L, 5, 1L, "1호선", "bg-blue-600", 1L, "온수", 2L, "오류동"),
                        new SectionDetailEntity(2L, 5, 1L, "1호선", "bg-blue-600", 2L, "오류동", 3L, "개봉"),
                        new SectionDetailEntity(3L, 5, 1L, "1호선", "bg-blue-600", 3L, "개봉", 4L, "구일"),
                        new SectionDetailEntity(4L, 5, 1L, "1호선", "bg-blue-600", 4L, "구일", 5L, "구로"),
                        new SectionDetailEntity(5L, 5, 1L, "1호선", "bg-blue-600", 5L, "구로", 6L, "가산디지털단지"),
                        new SectionDetailEntity(6L, 8, 2L, "7호선", "bg-olive-600", 1L, "온수", 7L, "천왕"),
                        new SectionDetailEntity(7L, 8, 2L, "7호선", "bg-olive-600", 7L, "천왕", 8L, "광명사거리"),
                        new SectionDetailEntity(8L, 8, 2L, "7호선", "bg-olive-600", 8L, "광명사거리", 9L, "철산"),
                        new SectionDetailEntity(9L, 8, 2L, "7호선", "bg-olive-600", 9L, "철산", 6L, "가산디지털단지"))
        );

        final StationEntity startStationEntity = new StationEntity(startStationId, "온수");
        final StationEntity endStationEntity = new StationEntity(endStationId, "가산디지털단지");
        given(stationDao.findById(startStationId)).willReturn(startStationEntity);
        given(stationDao.findById(endStationId)).willReturn(endStationEntity);

        // when
        final ShortestPathResponse response = pathService.findShortestPath(startStationId, endStationId);

        // then
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(25),
                () -> assertThat(response.getFee()).isEqualTo(1550),
                () -> assertThat(response.getPath().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toUnmodifiableList()))
                        .isEqualTo(List.of("온수", "오류동", "개봉", "구일", "구로", "가산디지털단지"))
        );
    }
}
