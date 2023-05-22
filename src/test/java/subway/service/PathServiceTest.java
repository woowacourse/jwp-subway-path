package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dao.StubSectionDao;
import subway.domain.DefaultFareStrategy;
import subway.domain.InitialAdditionalFareStrategy;
import subway.domain.SecondaryAdditionalFareStrategy;
import subway.domain.SubwayFareCalculator;
import subway.dto.PathSearchRequest;
import subway.dto.PathSearchResponse;

class PathServiceTest {

    private final StubSectionDao stubSectionDao = new StubSectionDao();
    private PathService pathService;

    @BeforeEach
    void setUp() {
        pathService = new PathService(stubSectionDao, new SubwayFareCalculator(
                List.of(
                        new DefaultFareStrategy(),
                        new InitialAdditionalFareStrategy(),
                        new SecondaryAdditionalFareStrategy()
                )
        ));
    }

    @DisplayName("최단 경로를 조회해 응답을 반환한다.")
    @Test
    void getShortestPath() {
        final PathSearchRequest request = new PathSearchRequest(4L, 6L);
        final PathSearchResponse result = pathService.getShortestPath(request);
        assertAll(
                () -> assertThat(result.getPath()).hasSize(4),
                () -> assertThat(result.getPath().get(0).getId()).isEqualTo(4L),
                () -> assertThat(result.getPath().get(0).getName()).isEqualTo("사당역"),
                () -> assertThat(result.getPath().get(1).getId()).isEqualTo(3L),
                () -> assertThat(result.getPath().get(1).getName()).isEqualTo("강남역"),
                () -> assertThat(result.getPath().get(2).getId()).isEqualTo(5L),
                () -> assertThat(result.getPath().get(2).getName()).isEqualTo("낙성대역"),
                () -> assertThat(result.getPath().get(3).getId()).isEqualTo(6L),
                () -> assertThat(result.getPath().get(3).getName()).isEqualTo("신림역"),
                () -> assertThat(result.getDistance()).isEqualTo(7),
                () -> assertThat(result.getFare()).isEqualTo(1250)
        );
    }
}
