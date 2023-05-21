package subway.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
import subway.domain.Station;
import subway.dto.RouteDto;
import subway.exception.section.DisconnectedSectionException;
import subway.exception.station.DuplicateStationNameException;
import subway.exception.station.NotFoundStationException;

@ExtendWith(SpringExtension.class)
class RouteServiceTest {

    @Mock
    SectionDao sectionDao;

    @Mock
    SectionsMapper sectionsMapper;

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
        Section section = new Section(station1, station2, distance);
        Sections sections = new Sections(List.of(section));

        when(sectionDao.findAll())
                .thenReturn(sectionEntities);

        when(sectionsMapper.mapFrom(sectionEntities))
                .thenReturn(sections);

        stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                .thenReturn(new Station("일역"));

        stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                .thenReturn(new Station("이역"));

        RouteDto routeDto = routeService.getFeeByStations("일역", "이역");

        assertThat(routeDto.getDistance().getDistance()).isEqualTo(3);
        assertThat(routeDto.getFee().getFee()).isEqualTo(1250);
    }

    @Nested
    @DisplayName("구간 요금 조회 예외 테스트")
    class ValidateSectionFee {

        @DisplayName("출발역과 도착역이 같은 경우 예외 테스트")
        @Test
        void validateFeeBySameStations() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, distanceValue);

            List<SectionEntity> sectionEntities = List.of(sectionEntity1);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Section section = new Section(station1, station2, distance);
            Sections sections = new Sections(List.of(section));

            when(sectionDao.findAll())
                    .thenReturn(sectionEntities);

            when(sectionsMapper.mapFrom(sectionEntities))
                    .thenReturn(sections);

            assertThatThrownBy(() -> routeService.getFeeByStations("일역", "일역"))
                    .isInstanceOf(DuplicateStationNameException.class)
                    .hasMessage("같은 역으로 경로를 조회할 수 없습니다.");
        }

        @DisplayName("출발역은 존재하지만 도착역이 존재하지 않는 경우 예외 테스트")
        @Test
        void validateFeeByNotExistsStations1() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, distanceValue);

            List<SectionEntity> sectionEntities = List.of(sectionEntity1);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Section section = new Section(station1, station2, distance);
            Sections sections = new Sections(List.of(section));

            when(sectionDao.findAll())
                    .thenReturn(sectionEntities);

            when(sectionsMapper.mapFrom(sectionEntities))
                    .thenReturn(sections);

            assertThatThrownBy(() -> routeService.getFeeByStations("일역", "삼역"))
                    .isInstanceOf(NotFoundStationException.class)
                    .hasMessage("출발역 또는 도착역이 존재하지 않습니다.");
        }

        @DisplayName("도착역은 존재하지만 출발역이 존재하지 않는 경우 예외 테스트")
        @Test
        void validateFeeByNotExistsStations2() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, distanceValue);

            List<SectionEntity> sectionEntities = List.of(sectionEntity1);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Section section = new Section(station1, station2, distance);
            Sections sections = new Sections(List.of(section));

            when(sectionDao.findAll())
                    .thenReturn(sectionEntities);

            when(sectionsMapper.mapFrom(sectionEntities))
                    .thenReturn(sections);

            assertThatThrownBy(() -> routeService.getFeeByStations("삼역", "이역"))
                    .isInstanceOf(NotFoundStationException.class)
                    .hasMessage("출발역 또는 도착역이 존재하지 않습니다.");
        }

        @DisplayName("연결되지 않은 구간에서의 경로 조회 시 예외 테스트")
        @Test
        void validateNotConnectedRoute() {
            int distanceValue = 3;
            Distance distance = new Distance(distanceValue);
            SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, distanceValue);
            SectionEntity sectionEntity2 = new SectionEntity(2L, 3L, 4L, distanceValue);

            List<SectionEntity> sectionEntities = List.of(sectionEntity1, sectionEntity2);
            Station station1 = new Station("일역");
            Station station2 = new Station("이역");
            Station station3 = new Station("삼역");
            Station station4 = new Station("사역");

            Section section1 = new Section(station1, station2, distance);
            Section section2 = new Section(station3, station4, distance);
            Sections sections = new Sections(List.of(section1, section2));

            when(sectionDao.findAll())
                    .thenReturn(sectionEntities);

            when(sectionsMapper.mapFrom(sectionEntities))
                    .thenReturn(sections);

            assertThatThrownBy(() -> routeService.getFeeByStations("일역", "사역"))
                    .isInstanceOf(DisconnectedSectionException.class)
                    .hasMessage("연결되어 있지 않은 구간은 조회할 수 없습니다.");
        }

    }

}