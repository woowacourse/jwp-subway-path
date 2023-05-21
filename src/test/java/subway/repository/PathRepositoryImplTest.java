package subway.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static subway.fixture.SectionFixture.SECTION_강남_잠실_5;
import static subway.fixture.SectionFixture.SECTION_몽촌토성_암사_5;
import static subway.fixture.SectionFixture.SECTION_잠실_몽촌토성_5;
import static subway.fixture.SectionVoFixture.SECTION_VO_강남_잠실_5;
import static subway.fixture.SectionVoFixture.SECTION_VO_몽촌토성_암사_5;
import static subway.fixture.SectionVoFixture.SECTION_VO_잠실_몽촌토성_5;
import static subway.fixture.StationFixture.STATION_강남;

import java.util.List;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Sections;
import subway.domain.Station;
import subway.entity.StationEntity;
import subway.entity.vo.SectionVo;

@WebMvcTest(PathRepositoryImpl.class)
class PathRepositoryImplTest {
    private final List<SectionVo> sectionVos = List.of(SECTION_VO_강남_잠실_5, SECTION_VO_잠실_몽촌토성_5, SECTION_VO_몽촌토성_암사_5);

    @Autowired
    private PathRepositoryImpl pathRepository;

    @MockBean
    private SectionDao sectionDao;

    @MockBean
    private StationDao stationDao;

    @Test
    @DisplayName("전체 구간을 조회한다.")
    void findAllSections() {
        // given
        willReturn(sectionVos).given(sectionDao).findAll();

        // when
        Sections all = pathRepository.findAllSections();

        // then
        assertThat(all.getSections())
                .hasSize(sectionVos.size())
                .contains(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5, SECTION_몽촌토성_암사_5);
    }

    @Test
    @DisplayName("id로 역을 조회한다.")
    void findStationById() {
        // given
        StationEntity expectedStationEntity = StationEntity.of(1L, "강남");
        willReturn(true).given(stationDao).existsById(anyLong());
        willReturn(expectedStationEntity).given(stationDao).findById(anyLong());

        // when
        Station station = pathRepository.findStationById(1L);

        // then
        AssertionsForClassTypes.assertThat(station).isEqualTo(STATION_강남);
    }

    @Test
    @DisplayName("존재하지 않는 id로 역을 조회하면 예외가 발생한다.")
    void findStationById_fail() {
        // given
        willReturn(false).given(stationDao).existsById(anyLong());

        // when, then
        assertThatThrownBy(
                () -> pathRepository.findStationById(10L)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 STATION ID 입니다.");
    }
}
