package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.integration.TestFixture.GANGNAM;
import static subway.integration.TestFixture.SAMSUNG;
import static subway.integration.TestFixture.SEONGLENUG;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.domain.DistanceProportionalFarePolicy;
import subway.domain.path.DijkstraShortestPathFinder;
import subway.persistence.dao.SectionDao;
import subway.persistence.dao.StationDao;
import subway.service.dto.PathRequest;
import subway.service.dto.PathResponse;
import subway.service.dto.StationResponse;

@JdbcTest
@Import({PathService.class, SectionDao.class, StationDao.class,
        DijkstraShortestPathFinder.class, DistanceProportionalFarePolicy.class})
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @DisplayName("출발지와 목적지를 입력하면, 최단 경로와 거리와 요금을 반환하는 기능 테스트")
    @Test
    void findPath() {
        final PathRequest pathRequest = new PathRequest("강남", "삼성");

        final PathResponse pathResponse = pathService.findPath(pathRequest);

        final List<StationResponse> stationResponses = pathResponse.getPathStations();
        assertAll(
                () -> assertThat(stationResponses)
                        .extracting(StationResponse::getName)
                        .containsExactly(GANGNAM.getName(), SEONGLENUG.getName(), SAMSUNG.getName()),
                () -> assertThat(pathResponse)
                        .extracting(PathResponse::getTotalDistance, PathResponse::getTotalFare)
                        .containsExactly(10, 1250)
        );
    }
}
