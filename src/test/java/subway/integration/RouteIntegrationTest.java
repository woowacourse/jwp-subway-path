package subway.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.adapter.out.persistence.LineJdbcRepository;
import subway.adapter.out.persistence.StationJdbcRepository;
import subway.application.port.in.route.dto.response.RouteQueryResponse;
import subway.domain.line.Line;
import subway.domain.line.LineInfo;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.fixture.StationFixture.강남역;
import subway.fixture.StationFixture.고터역;
import subway.fixture.StationFixture.교대역;
import subway.fixture.StationFixture.논현역;
import subway.fixture.StationFixture.반포역;
import subway.fixture.StationFixture.방배역;
import subway.fixture.StationFixture.서초역;
import subway.fixture.StationFixture.신논현역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class RouteIntegrationTest extends IntegrationTest {

    @Autowired
    private LineJdbcRepository lineJdbcRepository;

    @Autowired
    private StationJdbcRepository stationJdbcRepository;

    @Test
    void 최단경로를_조회한다() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 서초역Id = stationJdbcRepository.create(서초역.STATION);
        long 교대역Id = stationJdbcRepository.create(교대역.STATION);
        long 강남역Id = stationJdbcRepository.create(강남역.STATION);
        long 고터역Id = stationJdbcRepository.create(고터역.STATION);
        long 신논현역Id = stationJdbcRepository.create(신논현역.STATION);
        long 논현역Id = stationJdbcRepository.create(논현역.STATION);
        long 반포역Id = stationJdbcRepository.create(반포역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 0));
        long 삼호선Id = lineJdbcRepository.create(new LineInfo("3호선", "ORANGE", 0));
        long 신분당선Id = lineJdbcRepository.create(new LineInfo("신분당선", "RED", 0));
        long 칠호선Id = lineJdbcRepository.create(new LineInfo("7호선", "DARK_GREEN", 0));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 0, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(서초역Id, "서초역"), 1),
                new Section(new Station(서초역Id, "서초역"), new Station(교대역Id, "교대역"), 1),
                new Section(new Station(교대역Id, "교대역"), new Station(강남역Id, "강남역"), 2)
        )));

        lineJdbcRepository.updateSections(new Line(삼호선Id, "3호선", "GREEN", 0, List.of(
                new Section(new Station(교대역Id, "교대역"), new Station(고터역Id, "고터역"), 3)
        )));

        lineJdbcRepository.updateSections(new Line(신분당선Id, "신분당선", "GREEN", 0, List.of(
                new Section(new Station(강남역Id, "강남역"), new Station(신논현역Id, "신논현역"), 3),
                new Section(new Station(신논현역Id, "신논현역"), new Station(논현역Id, "논현역"), 4)
        )));

        lineJdbcRepository.updateSections(new Line(칠호선Id, "7호선", "GREEN", 0, List.of(
                new Section(new Station(고터역Id, "고터역"), new Station(반포역Id, "반포역"), 3),
                new Section(new Station(반포역Id, "반포역"), new Station(논현역Id, "논현역"), 2)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 논현역Id);

        // then
        assertAll(
                () -> assertThat(response.getRoute())
                        .usingRecursiveComparison()
                        .ignoringFields("id")
                        .isEqualTo(List.of(
                                방배역.RESPONSE, 서초역.RESPONSE, 교대역.RESPONSE, 고터역.RESPONSE, 반포역.RESPONSE, 논현역.RESPONSE
                        )),
                () -> assertThat(response.getDistance()).isEqualTo(10),
                () -> assertThat(response.getFare()).isEqualTo(1250)
        );
    }

    @Test
    void 최단경로_조회시_거리가_10이상이면_추가요금이_발생한다() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 서초역Id = stationJdbcRepository.create(서초역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 0));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 0, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(서초역Id, "서초역"), 11)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 서초역Id);

        // then
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(11),
                () -> assertThat(response.getFare()).isEqualTo(1350)
        );
    }

    @Test
    void 최단경로_조회시_거리가_50초과이면_초과_추가요금이_발생한다() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 서초역Id = stationJdbcRepository.create(서초역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 0));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 0, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(서초역Id, "서초역"), 58)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 서초역Id);

        // then
        assertAll(
                () -> assertThat(response.getDistance()).isEqualTo(58),
                () -> assertThat(response.getFare()).isEqualTo(2150)
        );
    }

    @Test
    void 최단경로_조회시_노선에_따른_추가요금이_발생한다() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 교대역Id = stationJdbcRepository.create(교대역.STATION);
        long 고터역Id = stationJdbcRepository.create(고터역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 100));
        long 삼호선Id = lineJdbcRepository.create(new LineInfo("3호선", "ORANGE", 300));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 100, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(교대역Id, "교대역"), 1)
        )));
        lineJdbcRepository.updateSections(new Line(삼호선Id, "3호", "ORANGE", 300, List.of(
                new Section(new Station(교대역Id, "교대역"), new Station(고터역Id, "고터역"), 1)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 고터역Id);

        // then
        assertThat(response.getFare()).isEqualTo(1550);
    }

    @Test
    void 성인_요금_할인() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 서초역Id = stationJdbcRepository.create(서초역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 0));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 0, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(서초역Id, "서초역"), 8)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 서초역Id, 19);

        // then
        assertThat(response.getFare()).isEqualTo(1250);
    }

    @Test
    void 청소년_요금_할인() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 서초역Id = stationJdbcRepository.create(서초역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 0));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 0, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(서초역Id, "서초역"), 8)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 서초역Id, 13);

        // then
        assertThat(response.getFare()).isEqualTo(720);
    }

    @Test
    void 어린이_요금_할인() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 서초역Id = stationJdbcRepository.create(서초역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 0));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 0, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(서초역Id, "서초역"), 8)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 서초역Id, 6);

        // then
        assertThat(response.getFare()).isEqualTo(450);
    }

    @Test
    void 영유아_요금_할인() {
        // given
        long 방배역Id = stationJdbcRepository.create(방배역.STATION);
        long 서초역Id = stationJdbcRepository.create(서초역.STATION);

        long 이호선Id = lineJdbcRepository.create(new LineInfo("2호선", "GREEN", 0));

        lineJdbcRepository.updateSections(new Line(이호선Id, "2호선", "GREEN", 0, List.of(
                new Section(new Station(방배역Id, "방배역"), new Station(서초역Id, "서초역"), 8)
        )));

        // when
        RouteQueryResponse response = findRoute(방배역Id, 서초역Id, 5);

        // then
        assertThat(response.getFare()).isEqualTo(0);
    }
}
