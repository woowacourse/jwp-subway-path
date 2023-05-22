package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.FareCalculator;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.RoutesResponse;
import subway.dto.StationResponse;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

class RoutingServiceTest {

    private RoutingService routingService;

    private SectionRepository sectionRepositoryFake = new SectionRepositoryFake();
    private StationRepository stationRepositoryFake = new StationRepositoryFake();

    private final Long lineId = 1L;

    private Station station1;
    private Station station2;
    private Station station3;
    private Station station4;

    private Section section1;
    private Section section2;
    private Section section3;

    @BeforeEach
    void init() {
        sectionRepositoryFake = new SectionRepositoryFake();
        stationRepositoryFake = new StationRepositoryFake();

        routingService = new RoutingService(sectionRepositoryFake, stationRepositoryFake);

        station1 = new Station(1L, "사당");
        station2 = new Station(2L, "방배");
        station3 = new Station(3L, "서초");
        station4 = new Station(4L, "교대");
        stationRepositoryFake.insert(station1);
        stationRepositoryFake.insert(station2);
        stationRepositoryFake.insert(station3);
        stationRepositoryFake.insert(station4);

        section1 = new Section(1L, lineId, station1.getId(), station2.getId(), 3);
        section2 = new Section(2L, lineId, station2.getId(), station3.getId(), 3);
        section3 = new Section(3L, lineId, station3.getId(), station4.getId(), 3);
        sectionRepositoryFake.insert(section1);
        sectionRepositoryFake.insert(section2);
        sectionRepositoryFake.insert(section3);
    }

    @DisplayName("호선에 해당하는 역들을 순서대로 반환한다.")
    @Test
    void findStationResponses_success() {
        //when
        List<StationResponse> stations = routingService.findStationResponses(lineId);
        //then
        List<String> stationNames = stations.stream().map(StationResponse::getName).collect(Collectors.toList());
        assertThat(stationNames).containsExactly(station1.getName(), station2.getName(),
                station3.getName(), station4.getName());
    }

    @DisplayName("시작역과 도착역을 입력하면 해당하는 경로의 역들과 거리를 반환한다.")
    @Test
    void findRoutes_success() {
        //given
        int expectedDistance = section1.getDistance() + section2.getDistance() + section3.getDistance();
        //when
        RoutesResponse routes = routingService.findRoutes(station1.getId(), station4.getId());
        //then
        assertAll(
                () -> assertThat(routes.getStationResponses().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList())).containsExactly(station1.getName(), station2.getName(),
                        station3.getName(), station4.getName()),
                () -> assertThat(routes.getTotalDistance()).isEqualTo(expectedDistance),
                () -> assertThat(routes.getFare()).isEqualTo(FareCalculator.calculate((double) expectedDistance))
        );
    }
}