package subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.dao.StubLineDao;
import subway.dao.StubSectionDao;
import subway.domain.Station;
import subway.dto.LineSearchResponse;

class SubwayMapServiceTest {

    private SubwayMapService subwayMapService;

    @BeforeEach
    void setUp() {
        final StubLineDao stubLineDao = new StubLineDao();
        final StubSectionDao stubSectionDao = new StubSectionDao();
        subwayMapService = new SubwayMapService(stubLineDao, stubSectionDao);
    }

    @DisplayName("lineId로 해당 노선을 순서대로 가져온다.")
    @Test
    void getLineSearchResponse() {
        final LineSearchResponse lineSearchResponse = subwayMapService.getLineSearchResponse(1L);
        assertAll(
                () -> assertThat(lineSearchResponse.getId()).isEqualTo(1L),
                () -> assertThat(lineSearchResponse.getName()).isEqualTo("1호선"),
                () -> assertThat(lineSearchResponse.getColor()).isEqualTo("파란색"),
                () -> assertThat(lineSearchResponse.getStations()).containsExactly(
                        new Station(1L),
                        new Station(2L),
                        new Station(3L),
                        new Station(4L)
                )
        );
    }

    @DisplayName("모든 노선을 순서대로 가져온다.")
    @Test
    void getLineSearchResponses() {
        final List<LineSearchResponse> lineSearchResponses = subwayMapService.getLineSearchResponses();
        final LineSearchResponse line1 = lineSearchResponses.get(0);
        final LineSearchResponse line2 = lineSearchResponses.get(1);
        assertAll(
                () -> assertThat(line1.getId()).isEqualTo(1L),
                () -> assertThat(line1.getName()).isEqualTo("1호선"),
                () -> assertThat(line1.getColor()).isEqualTo("파란색"),
                () -> assertThat(line1.getStations()).containsExactly(
                        new Station(1L),
                        new Station(2L),
                        new Station(3L),
                        new Station(4L)
                ),
                () -> assertThat(line2.getId()).isEqualTo(2L),
                () -> assertThat(line2.getName()).isEqualTo("2호선"),
                () -> assertThat(line2.getColor()).isEqualTo("초록색"),
                () -> assertThat(line2.getStations()).containsExactly(
                        new Station(3L),
                        new Station(5L),
                        new Station(6L)
                )
        );
    }
}
