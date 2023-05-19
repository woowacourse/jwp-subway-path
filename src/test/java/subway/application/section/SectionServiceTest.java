package subway.application.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.anyString;
import static org.mockito.BDDMockito.given;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.InMemorySectionDao;
import subway.dao.StationDao;
import subway.domain.station.Station;
import subway.dto.section.SectionCreateRequest;
import subway.dto.section.SectionResponse;
import subway.exception.section.IllegalDistanceException;
import subway.exception.section.IllegalSectionException;

@ExtendWith(MockitoExtension.class)
class SectionServiceTest {

    @InjectMocks
    SectionService sectionService;

    @Spy
    InMemorySectionDao sectionDao;

    @Mock
    StationDao stationDao;

    @Test
    @DisplayName("구간을 추가할 때 상행역과 하행역이 역 목록에 존재하지 않으면 예외가 발생해야 한다.")
    void saveSection_stationsNotExists() {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", 10);
        given(stationDao.findByName(anyString()))
                .willReturn(Optional.empty());

        // expect
        assertThatThrownBy(() -> sectionService.saveSection(1L, request))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("추가하려는 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("구간을 추가할 때 노선에 구간이 없으면 추가되어야 한다.")
    void saveSection_emptySectionInLine() {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", 10);
        given(stationDao.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));
        given(stationDao.findByName("잠실나루역"))
                .willReturn(Optional.of(new Station("잠실나루역")));

        // when
        sectionService.saveSection(1L, request);

        // then
        List<SectionResponse> sections = sectionService.findSectionsByLineId(1L);
        assertThat(sections)
                .hasSize(1);
        SectionResponse section = sections.get(0);
        assertThat(section.getStartStationName())
                .isEqualTo("잠실역");
        assertThat(section.getEndStationName())
                .isEqualTo("잠실나루역");
    }

    @Test
    @DisplayName("구간을 추가할 때 노선에 해당 구간에 대한 역이 모두 있으면 예외가 발생해야 한다.")
    void saveSection_hasBothStationInSections() {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", 10);
        given(stationDao.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));
        given(stationDao.findByName("잠실나루역"))
                .willReturn(Optional.of(new Station("잠실나루역")));
        sectionService.saveSection(1L, request);

        // expect
        assertThatThrownBy(() -> sectionService.saveSection(1L, request))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("노선에 이미 해당 역이 존재합니다.");
    }

    @Test
    @DisplayName("구간을 추가할 때 노선에 기준이 되는 역이 없으면 예외가 발생해야 한다.")
    void saveSection_hasNoCriteriaStations() {
        // given
        SectionCreateRequest request = new SectionCreateRequest("잠실역", "잠실나루역", 10);
        given(stationDao.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));
        given(stationDao.findByName("잠실나루역"))
                .willReturn(Optional.of(new Station("잠실나루역")));
        given(stationDao.findByName("서울역"))
                .willReturn(Optional.of(new Station("서울역")));
        given(stationDao.findByName("용산역"))
                .willReturn(Optional.of(new Station("용산역")));
        sectionService.saveSection(1L, request);

        // expect
        assertThatThrownBy(() -> sectionService.saveSection(1L, new SectionCreateRequest("서울역", "용산역", 10)))
                .isInstanceOf(IllegalSectionException.class)
                .hasMessage("노선에 기준이 되는 역을 찾을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11, 12})
    @DisplayName("구간을 추가할 때 추가할 구가할 구간의 길이가 기존의 구간의 길이보다 길거나 같으면 예외가 발생해야 한다.")
    void saveSection_overThanExistingSectionDistance(int distance) {
        // given
        given(stationDao.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));
        given(stationDao.findByName("잠실나루역"))
                .willReturn(Optional.of(new Station("잠실나루역")));
        given(stationDao.findByName("서울역"))
                .willReturn(Optional.of(new Station("서울역")));
        sectionService.saveSection(1L, new SectionCreateRequest("잠실역", "잠실나루역", 10));

        // expect
        assertThatThrownBy(() -> sectionService.saveSection(1L, new SectionCreateRequest("잠실역", "서울역", distance)))
                .isInstanceOf(IllegalDistanceException.class)
                .hasMessage("새로운 구간의 길이는 기존 구간의 길이보다 작아야 합니다.");
    }

    @Test
    @DisplayName("구간을 추가할 때 상행역 기준 상행 종점에 추가될 수 있어야 한다.")
    void saveSection_upBoundStationSuccess() {
        // given
        given(stationDao.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));
        given(stationDao.findByName("잠실나루역"))
                .willReturn(Optional.of(new Station("잠실나루역")));
        given(stationDao.findByName("서울역"))
                .willReturn(Optional.of(new Station("서울역")));
        sectionService.saveSection(1L, new SectionCreateRequest("잠실역", "잠실나루역", 10));

        // when
        sectionService.saveSection(1L, new SectionCreateRequest("서울역", "잠실역", 10));

        // then
        List<SectionResponse> sections = sectionService.findSectionsByLineId(1L);
        assertThat(sections)
                .hasSize(2);
        SectionResponse section = sections.get(0);
        assertThat(section.getStartStationName())
                .isEqualTo("서울역");
        assertThat(section.getEndStationName())
                .isEqualTo("잠실역");
    }

    @Test
    @DisplayName("구간을 추가할 때 하행역 기준 하행 종점에 추가될 수 있어야 한다.")
    void saveSection_downBoundStationSuccess() {
        // given
        given(stationDao.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));
        given(stationDao.findByName("잠실나루역"))
                .willReturn(Optional.of(new Station("잠실나루역")));
        given(stationDao.findByName("서울역"))
                .willReturn(Optional.of(new Station("서울역")));
        sectionService.saveSection(1L, new SectionCreateRequest("잠실역", "잠실나루역", 10));

        // when
        sectionService.saveSection(1L, new SectionCreateRequest("잠실나루역", "서울역", 10));

        // then
        List<SectionResponse> sections = sectionService.findSectionsByLineId(1L);
        assertThat(sections)
                .hasSize(2);
        SectionResponse section = sections.get(1);
        assertThat(section.getStartStationName())
                .isEqualTo("잠실나루역");
        assertThat(section.getEndStationName())
                .isEqualTo("서울역");
    }

    @Test
    @DisplayName("구간을 추가할 때 하행역 기준 하행 종점에 추가될 수 있어야 한다.")
    void saveSection_betweenStationSuccess() {
        // given
        given(stationDao.findByName("잠실역"))
                .willReturn(Optional.of(new Station("잠실역")));
        given(stationDao.findByName("잠실나루역"))
                .willReturn(Optional.of(new Station("잠실나루역")));
        given(stationDao.findByName("서울역"))
                .willReturn(Optional.of(new Station("서울역")));
        sectionService.saveSection(1L, new SectionCreateRequest("잠실역", "잠실나루역", 10));

        // when
        sectionService.saveSection(1L, new SectionCreateRequest("잠실역", "서울역", 3));

        // then
        List<SectionResponse> sections = sectionService.findSectionsByLineId(1L);
        assertThat(sections)
                .hasSize(2);
        SectionResponse section = sections.get(0);
        assertThat(section.getStartStationName())
                .isEqualTo("잠실역");
        assertThat(section.getEndStationName())
                .isEqualTo("서울역");
        assertThat(section.getDistance())
                .isEqualTo(3);
        assertThat(sections.get(1).getDistance())
                .isEqualTo(7);
    }
}
