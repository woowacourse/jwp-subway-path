package subway.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.AfterAll;
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
import subway.dao.StationDao;
import subway.dao.StationEntity;
import subway.domain.Distance;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.SectionSaveDto;
import subway.exception.GlobalException;

@ExtendWith(SpringExtension.class)
class SectionServiceTest {

    @Mock
    SectionDao sectionDao;

    @Mock
    StationDao stationDao;

    @Mock
    SectionsMapper sectionsMapper;

    private static MockedStatic<StationFactory> stationFactory;

    @BeforeAll
    static void beforeAll() {
        stationFactory = mockStatic(StationFactory.class);
    }

    @AfterAll
    static void afterAll() {
        stationFactory.close();
    }

    @InjectMocks
    SectionService sectionService;

    @DisplayName("구간 삭제 테스트")
    @Test
    void deleteSectionSuccessTest() {
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

        when(sectionDao.findByLineId(1L))
                .thenReturn(sectionEntities);

        when(sectionsMapper.mapFrom(sectionEntities))
                .thenReturn(sections);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        when(stationDao.findById(2L))
                .thenReturn(stationEntity2);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                .thenReturn(new Station("일역"));

        stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                .thenReturn(new Station("이역"));

        sectionService.deleteSection(1L, 1L);

        verify(sectionDao, atLeastOnce()).delete(any(SectionEntity.class));
    }

    @DisplayName("구간 저장 테스트 (도착역이 새로운 역인 경우)")
    @Test
    void saveSectionSuccessTest1() {
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

        when(sectionDao.findByLineId(1L))
                .thenReturn(sectionEntities);

        when(sectionsMapper.mapFrom(sectionEntities))
                .thenReturn(sections);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        when(stationDao.findById(2L))
                .thenReturn(stationEntity2);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                .thenReturn(new Station("일역"));

        stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                .thenReturn(new Station("이역"));

        when(stationDao.isExistStationByName("이역"))
                .thenReturn(true);

        sectionService.saveSection(1L, new SectionSaveDto("이역", "삼역", 3));

        verify(sectionDao, atLeastOnce()).insert(any(SectionEntity.class));
    }

    @DisplayName("구간 저장 테스트 (출발역 새로운 역인 경우)")
    @Test
    void saveSectionSuccessTest2() {
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

        when(sectionDao.findByLineId(1L))
                .thenReturn(sectionEntities);

        when(sectionsMapper.mapFrom(sectionEntities))
                .thenReturn(sections);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        when(stationDao.findById(2L))
                .thenReturn(stationEntity2);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                .thenReturn(new Station("일역"));

        stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                .thenReturn(new Station("이역"));

        when(stationDao.isExistStationByName("영역"))
                .thenReturn(true);

        sectionService.saveSection(1L, new SectionSaveDto("영역", "일역", 3));

        verify(sectionDao, atLeastOnce()).insert(any());
    }

    @DisplayName("구간 저장 테스트 (도착역이 새로운 역이고 사이에 추가되는 경우)")
    @Test
    void saveSectionSuccessTest3() {
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

        when(sectionDao.findByLineId(1L))
                .thenReturn(sectionEntities);

        when(sectionsMapper.mapFrom(sectionEntities))
                .thenReturn(sections);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        when(stationDao.findById(2L))
                .thenReturn(stationEntity2);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                .thenReturn(new Station("일역"));

        stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                .thenReturn(new Station("이역"));

        when(stationDao.isExistStationByName("일역"))
                .thenReturn(true);

        sectionService.saveSection(1L, new SectionSaveDto("일역", "삼역", 1));

        verify(sectionDao, atLeastOnce()).update(any(SectionEntity.class));
        verify(sectionDao, atLeastOnce()).insert(any(SectionEntity.class));
    }

    @DisplayName("구간 저장 테스트 (출발역이 새로운 역이고 사이에 추가되는 경우)")
    @Test
    void saveSectionSuccessTest4() {
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

        when(sectionDao.findByLineId(1L))
                .thenReturn(sectionEntities);

        when(sectionsMapper.mapFrom(sectionEntities))
                .thenReturn(sections);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        when(stationDao.findById(2L))
                .thenReturn(stationEntity2);

        when(stationDao.findById(1L))
                .thenReturn(stationEntity1);

        stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                .thenReturn(new Station("일역"));

        stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                .thenReturn(new Station("이역"));

        when(stationDao.isExistStationByName("삼역"))
                .thenReturn(true);

        sectionService.saveSection(1L, new SectionSaveDto("삼역", "이역", 1));

        verify(sectionDao, atLeastOnce()).insert(any(SectionEntity.class));
        verify(sectionDao, atLeastOnce()).update(any(SectionEntity.class));
    }


    @DisplayName("구간 추가 예외")
    @Nested
    class ValidateSection {

        @DisplayName("구간 저장 테스트 (같은 구간인 경우)")
        @Test
        void saveSectionFailTest1() {
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

            when(sectionDao.findByLineId(1L))
                    .thenReturn(sectionEntities);

            when(sectionsMapper.mapFrom(sectionEntities))
                    .thenReturn(sections);

            when(stationDao.findById(1L))
                    .thenReturn(stationEntity1);

            when(stationDao.findById(2L))
                    .thenReturn(stationEntity2);

            when(stationDao.findById(1L))
                    .thenReturn(stationEntity1);

            stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                    .thenReturn(new Station("일역"));

            stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                    .thenReturn(new Station("이역"));

            when(stationDao.isExistStationByName("일역"))
                    .thenReturn(true);

            assertThatThrownBy(() -> sectionService.saveSection(1L, new SectionSaveDto("일역", "이역", 1)))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("이미 존재하는 구간입니다.");
        }

        @DisplayName("구간 저장 테스트 (출발역과 도착역이 같은 경우)")
        @Test
        void saveSectionFailTest2() {
            SectionEntity sectionEntity1 = new SectionEntity(1L, 1L, 2L, 3);

            List<SectionEntity> sections = List.of(sectionEntity1);
            StationEntity stationEntity1 = new StationEntity(1L, "일역");
            StationEntity stationEntity2 = new StationEntity(2L, "이역");

            when(sectionDao.findByLineId(1L))
                    .thenReturn(sections);

            when(stationDao.findById(1L))
                    .thenReturn(stationEntity1);

            when(stationDao.findById(2L))
                    .thenReturn(stationEntity2);

            when(stationDao.findById(1L))
                    .thenReturn(stationEntity1);

            stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                    .thenReturn(new Station("일역"));

            stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                    .thenReturn(new Station("이역"));

            when(stationDao.isExistStationByName("일역"))
                    .thenReturn(true);

            assertThatThrownBy(() -> sectionService.saveSection(1L, new SectionSaveDto("일역", "일역", 1)))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("시작 역과 도착 역은 같을 수 없습니다.");
        }

        @DisplayName("구간 저장 테스트 (구간길이가 추가될 구간보다 긴 경우)")
        @Test
        void saveSectionFailTest3() {
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

            when(sectionDao.findByLineId(1L))
                    .thenReturn(sectionEntities);

            when(sectionsMapper.mapFrom(sectionEntities))
                    .thenReturn(sections);

            when(stationDao.findById(1L))
                    .thenReturn(stationEntity1);

            when(stationDao.findById(2L))
                    .thenReturn(stationEntity2);

            when(stationDao.findById(1L))
                    .thenReturn(stationEntity1);

            stationFactory.when(() -> StationFactory.toStation(stationEntity1))
                    .thenReturn(new Station("일역"));

            stationFactory.when(() -> StationFactory.toStation(stationEntity2))
                    .thenReturn(new Station("이역"));

            when(stationDao.isExistStationByName("일역"))
                    .thenReturn(true);

            assertThatThrownBy(() -> sectionService.saveSection(1L, new SectionSaveDto("일역", "삼역", 5)))
                    .isInstanceOf(GlobalException.class)
                    .hasMessage("구간 길이로 인해 연결할 수 없습니다.");
        }

    }

}
