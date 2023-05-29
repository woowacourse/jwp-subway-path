package subway.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static subway.fixture.SectionFixture.SECTION_강남_잠실_5;
import static subway.fixture.SectionFixture.SECTION_잠실_몽촌토성_5;
import static subway.fixture.StationFixture.STATION_강남;
import static subway.fixture.StationFixture.STATION_잠실;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.entity.StationEntity;
import subway.entity.vo.SectionVo;

@WebMvcTest(SectionRepositoryImpl.class)
class SectionRepositoryImplTest {

    public static final SectionVo SECTION_VO_강남_잠실_5 = SectionVo.of(1L, "강남", 2L, "잠실", 5);
    public static final SectionVo SECTION_VO_잠실_몽촌토성_5 = SectionVo.of(2L, "잠실", 3L, "몽촌토성", 5);
    @Autowired
    private SectionRepositoryImpl sectionRepositoryImpl;

    @MockBean
    private LineDao lineDao;

    @MockBean
    private SectionDao sectionDao;

    @MockBean
    private StationDao stationDao;

    @Test
    @DisplayName("해당 호선에 포함되는 모든 구간들을 조회한다.")
    void findSectionsByLineId() {
        // given
        willReturn(true).given(lineDao).existsById(anyLong());
        willReturn(List.of(SECTION_VO_강남_잠실_5, SECTION_VO_잠실_몽촌토성_5)).given(sectionDao).findSectionsByLineId(anyLong());

        // when
        Sections sections = sectionRepositoryImpl.findSectionsByLineId(1L);

        // then
        assertThat(sections.getSections())
                .hasSize(2)
                .contains(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5);
    }

    @Test
    @DisplayName("구간 하나를 추가한다.")
    void addSection() {
        // given
        willReturn(true).given(lineDao).existsById(anyLong());
        willReturn(StationEntity.of(1L, "강남")).given(stationDao).insert(STATION_강남);
        willReturn(StationEntity.of(2L, "잠실")).given(stationDao).insert(STATION_잠실);
        willDoNothing().given(sectionDao).insertSection(any(), anyLong());

        // when
        Station station = sectionRepositoryImpl.addSection(SECTION_강남_잠실_5, 1L);

        // then
        assertThat(station).isEqualTo(STATION_잠실);
    }

    @Test
    @DisplayName("다수 구간들을 추가한다.")
    void addSections() {
        // given
        Sections sections = new Sections(List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5));
        willReturn(true).given(lineDao).existsById(anyLong());
        willDoNothing().given(sectionDao).insertSections(any(), anyLong());

        // when, then
        assertDoesNotThrow(
                () -> sectionRepositoryImpl.addSections(sections, 1L)
        );
    }

    @Test
    @DisplayName("새로운 역을 추가한다.")
    void addStation() {
        // given
        StationEntity stationEntity = StationEntity.of(1L, "강남");
        willReturn(false).given(stationDao).existsByName(anyString());
        willReturn(stationEntity).given(stationDao).insert(STATION_강남);

        // when
        Station station = sectionRepositoryImpl.addStation(STATION_강남);

        // then
        assertThat(station).isEqualTo(STATION_강남);
    }

    @Test
    @DisplayName("id로 역을 조회한다.")
    void findStationById() {
        // given
        StationEntity expectedStationEntity = StationEntity.of(1L, "강남");
        willReturn(true).given(stationDao).existsById(anyLong());
        willReturn(expectedStationEntity).given(stationDao).findById(anyLong());

        // when
        Station station = sectionRepositoryImpl.findStationById(1L);

        // then
        assertThat(station).isEqualTo(STATION_강남);
    }

    @Test
    @DisplayName("다수 구간들을 삭제한다.")
    void deleteSections() {
        // given
        Sections sections = new Sections(List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5));
        willReturn(true).given(lineDao).existsById(anyLong());
        willDoNothing().given(sectionDao).deleteSections(any(), anyLong());

        // when, then
        assertDoesNotThrow(
                () -> sectionRepositoryImpl.deleteSections(sections, 1L)
        );
    }
}
