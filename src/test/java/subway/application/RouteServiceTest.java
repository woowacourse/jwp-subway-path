package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.dao.StationEntity;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.ShortestPathAlgorithmStrategy;
import subway.domain.Station;
import subway.dto.RouteDto;

@ExtendWith(SpringExtension.class)
class RouteServiceTest {

    @Mock
    SectionDao sectionDao;

    @Mock
    SectionsMapper sectionsMapper;

    @Mock
    ShortestPathAlgorithmStrategy shortestPathAlgorithmStrategy;

    private static MockedStatic<StationFactory> stationFactory;

    @InjectMocks
    RouteService routeService;

    @BeforeAll
    static void init() {
        stationFactory = mockStatic(StationFactory.class);
    }

    @DisplayName("구간에 따른 요금 반환 테스트")
    @Test
    void getFeeByStations() {
        int distanceValue = 3;
        Distance distance = new Distance(distanceValue);
        SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, distanceValue);

        List<SectionEntity> sectionEntities = List.of(sectionEntity1);
        StationEntity stationEntity1 = new StationEntity(1L, "일역");
        StationEntity stationEntity2 = new StationEntity(2L, "이역");
        Station station1 = new Station("일역");
        Station station2 = new Station("이역");
        Section section = Section.builder()
                .startStation(station1)
                .endStation(station2)
                .distance(distance).build();
        Sections sections = new Sections(List.of(section));

        when(sectionDao.findAll())
                .thenReturn(sectionEntities);

        when(sectionsMapper.mapFrom(sectionEntities))
                .thenReturn(sections);

        stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                .thenReturn(new Station("일역"));

        stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                .thenReturn(new Station("이역"));

        when(shortestPathAlgorithmStrategy.getShortestPath(sections, station1, station2))
                .thenReturn(List.of(station1, station2));

        when(shortestPathAlgorithmStrategy.getShortestPathWeight(sections, station1, station2))
                .thenReturn(distance);

        RouteDto routeDto = routeService.getFeeByStations("일역", "이역");

        assertThat(routeDto.getDistance().getDistance()).isEqualTo(3);
        assertThat(routeDto.getFee().getFee()).isEqualTo(1250);
    }

}