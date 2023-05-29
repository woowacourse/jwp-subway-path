package subway.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willReturn;
import static subway.fixture.SectionFixture.SECTION_강남_잠실_5;
import static subway.fixture.SectionFixture.SECTION_잠실_몽촌토성_5;
import static subway.fixture.SectionRequestFixture.SECTION_REQUEST_강남_잠실_5;
import static subway.fixture.SectionRequestFixture.SECTION_REQUEST_몽촌토성_암사_5;
import static subway.fixture.SectionRequestFixture.SECTION_REQUEST_잠실_길동_10;
import static subway.fixture.StationFixture.STATION_강남;
import static subway.fixture.StationFixture.STATION_길동;
import static subway.fixture.StationFixture.STATION_몽촌토성;
import static subway.fixture.StationFixture.STATION_암사;
import static subway.fixture.StationFixture.STATION_잠실;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.dto.StationDto;
import subway.dto.StationsDto;
import subway.repository.SectionRepositoryImpl;

@WebMvcTest(SectionService.class)
class SectionServiceTest {
    @Autowired
    SectionService sectionService;

    @MockBean
    SectionRepositoryImpl sectionRepositoryImpl;

    @Test
    @DisplayName("해당 호선에 최초로 구간과 역을 등록한다.")
    void add_first() {
        // given
        willReturn(new Sections()).given(sectionRepositoryImpl).findSectionsByLineId(anyLong());
        willReturn(new Station(2L, "잠실")).given(sectionRepositoryImpl).addSection(any(), anyLong());

        // when
        StationDto stationDto = sectionService.add(SECTION_REQUEST_강남_잠실_5, 1L);

        // then
        assertThat(stationDto)
                .usingRecursiveComparison()
                .isEqualTo(StationDto.of(2L, "잠실"));
    }

    @Test
    @DisplayName("해당 호선에 새로운 구간과 역을 등록한다.")
    void add() {
        // given
        willReturn(new Sections(List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5))).given(sectionRepositoryImpl)
                .findSectionsByLineId(anyLong());
        willReturn(true).given(sectionRepositoryImpl).existStationByName(new Station("몽촌토성"));
        willReturn(STATION_몽촌토성).given(sectionRepositoryImpl).findStationByName(new Station("몽촌토성"));
        willReturn(false).given(sectionRepositoryImpl).existStationByName(new Station("암사"));
        willReturn(STATION_암사).given(sectionRepositoryImpl).addStation(new Station("암사"));
        willDoNothing().given(sectionRepositoryImpl).addSections(any(), anyLong());
        willDoNothing().given(sectionRepositoryImpl).deleteSections(any(), anyLong());
        willReturn(STATION_암사).given(sectionRepositoryImpl).findStationByName(STATION_암사);

        // when
        StationDto stationDto = sectionService.add(SECTION_REQUEST_몽촌토성_암사_5, 1L);

        // then
        assertThat(stationDto)
                .usingRecursiveComparison()
                .isEqualTo(StationDto.of(4L, "암사"));
    }

    @Test
    @DisplayName("새로운 구간을 추가할 때 길이가 더 크면 예외를 발생시킨다.")
    void add_distance_too_big() {
        // given
        willReturn(new Sections(List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5))).given(sectionRepositoryImpl)
                .findSectionsByLineId(anyLong());
        willReturn(true).given(sectionRepositoryImpl).existStationByName(new Station("잠실"));
        willReturn(STATION_잠실).given(sectionRepositoryImpl).findStationByName(new Station("잠실"));
        willReturn(false).given(sectionRepositoryImpl).existStationByName(new Station("길동"));
        willReturn(STATION_길동).given(sectionRepositoryImpl).addStation(new Station("길동"));

        //when
        assertThatThrownBy(() -> sectionService.add(SECTION_REQUEST_잠실_길동_10, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 구간의 거리가 너무 큽니다.");
    }

    @Test
    @DisplayName("해당 노선에서 특정 역과 구간을 삭제한다.")
    void delete() {
        // given
        willReturn(new Sections(List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5))).given(sectionRepositoryImpl)
                .findSectionsByLineId(anyLong());
        willReturn(new Station(1L, "강남")).given(sectionRepositoryImpl).findStationById(anyLong());
        willDoNothing().given(sectionRepositoryImpl).addSections(any(), anyLong());
        willDoNothing().given(sectionRepositoryImpl).deleteSections(any(), anyLong());

        // when, then
        assertDoesNotThrow(
                () -> sectionService.delete(1L, 1L)
        );
    }

    @Test
    @DisplayName("역이 2개일 경우 역 제거 시도 시 해당 노선의 모든 역을 삭제한다.")
    void deleteSection_when_only_2_station_success() {
        // given
        willReturn(new Sections(List.of(SECTION_강남_잠실_5))).given(sectionRepositoryImpl).findSectionsByLineId(anyLong());
        willReturn(new Station(1L, "강남")).given(sectionRepositoryImpl).findStationById(anyLong());
        willDoNothing().given(sectionRepositoryImpl).addSections(any(), anyLong());
        willDoNothing().given(sectionRepositoryImpl).deleteSections(any(), anyLong());

        // when, then
        assertDoesNotThrow(
                () -> sectionService.delete(1L, 1L)
        );
    }

    @Test
    @DisplayName("해당 노선의 역들을 정렬하여 반환한다.")
    void findAll() {
        // given
        willReturn(new Sections(List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5))).given(sectionRepositoryImpl)
                .findSectionsByLineId(anyLong());

        // when
        StationsDto all = sectionService.findAll(1L);

        // then
        assertThat(all.getStations())
                .hasSize(3)
                .contains(StationDto.from(STATION_강남),
                        StationDto.from(STATION_잠실),
                        StationDto.from(STATION_몽촌토성));
    }

    @Test
    @DisplayName("노선에 존재하지 않는 역을 삭제한다.")
    void deleteSection_when_not_exist() {
        // given
        willReturn(new Sections(List.of(SECTION_강남_잠실_5, SECTION_잠실_몽촌토성_5))).given(sectionRepositoryImpl)
                .findSectionsByLineId(anyLong());
        willReturn(STATION_암사).given(sectionRepositoryImpl).findStationById(anyLong());

        // when, then
        assertThatThrownBy(
                () -> sectionService.delete(4L, 1L)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 노선에 존재하는 역이 아닙니다.");
    }
}
