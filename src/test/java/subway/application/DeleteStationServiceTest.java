package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.LineRepository;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationRepository;
import subway.exception.StationNotFoundException;

import java.util.List;

import static fixtures.LineFixtures.LINE2_ID;
import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteStationServiceTest {

    @InjectMocks
    DeleteStationService deleteStationService;

    @Mock
    LineRepository lineRepository;
    @Mock
    StationRepository stationRepository;
    @Mock
    SectionRepository sectionRepository;

    @Test
    @DisplayName("구간 정보에 존재하지 않는 역을 삭제하려고 하면 예외가 발생한다.")
    void deleteStationTest_fail_whenNotExistStation() {
        // given
        Long wrongId = 3L;
        when(stationRepository.findStationById(wrongId)).thenReturn(STATION_강변역);
        when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of());

        // when, then
        assertThatThrownBy(() -> deleteStationService.deleteStationById(wrongId))
                .isInstanceOf(StationNotFoundException.class)
                .hasMessage("해당 역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("역을 삭제하고 나서 해당 노선에 역이 1개밖에 안 남았다면 남은 역과 해당 노선을 삭제해야 한다.")
    void deleteStationsAndLineTest_when_twoStationsExist() {
        // given
        Long stationIdToDelete = STATION_잠실역_ID;
        when(stationRepository.findStationById(stationIdToDelete)).thenReturn(STATION_잠실역);
        when(sectionRepository.findSectionsByLineId(LINE2_ID))
                .thenReturn(List.of(SECTION_잠실역_TO_건대역));
        doNothing().when(stationRepository).remove(STATION_잠실역);
        doNothing().when(lineRepository).deleteById(LINE2_ID);

        // when
        Long changedLineId = deleteStationService.deleteStationById(stationIdToDelete);

        // then
        assertThat(changedLineId).isEqualTo(LINE2_ID);
    }

    @Test
    @DisplayName("상행 종점을 제거하는 경우")
    void deleteUpEndStationTest() {
        // given
        Long stationIdToDelete = STATION_잠실역_ID;
        when(stationRepository.findStationById(stationIdToDelete)).thenReturn((STATION_잠실역));
        when(sectionRepository.findSectionsByLineId(LINE2_ID))
                .thenReturn(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역));
        doNothing().when(stationRepository).remove(STATION_잠실역);

        // when
        Long changedLineId = deleteStationService.deleteStationById(stationIdToDelete);

        // then
        assertThat(changedLineId).isEqualTo(LINE2_ID);
    }

    @Test
    @DisplayName("하행 종점을 제거하는 경우")
    void deleteDownEndStationTest() {
        // given
        Long stationIdToDelete = STATION_건대역_ID;
        when(stationRepository.findStationById(stationIdToDelete)).thenReturn(STATION_건대역);
        when(sectionRepository.findSectionsByLineId(LINE2_ID))
                .thenReturn(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역));
        doNothing().when(stationRepository).remove(STATION_건대역);

        // when
        Long changedLineId = deleteStationService.deleteStationById(stationIdToDelete);

        // then
        assertThat(changedLineId).isEqualTo(LINE2_ID);
    }

    @Test
    @DisplayName("두 역 사이에 있는 역을 제거하는 경우 역과 역 사이의 관계 및 거리가 재배치된다.")
    void deleteBetweenStationTest1() {
        // given
        Long stationIdToDelete = STATION_강변역_ID;
        when(stationRepository.findStationById(stationIdToDelete)).thenReturn(STATION_강변역);
        when(sectionRepository.findSectionsByLineId(LINE2_ID))
                .thenReturn(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역));
        doNothing().when(stationRepository).remove(STATION_강변역);
        when(sectionRepository.insert(SECTION_TO_INSERT_AFTER_DELETE_잠실역_TO_건대역)).thenReturn(SECTION_AFTER_DELETE_잠실역_TO_건대역);

        // when
        Long changedLineId = deleteStationService.deleteStationById(stationIdToDelete);

        // then
        assertThat(changedLineId).isEqualTo(LINE2_ID);
    }

    @Test
    @DisplayName("두 역 사이에 있는 역을 제거하는 경우 역과 역 사이의 관계 및 거리가 재배치된다.")
    void deleteBetweenStationTest2() {
        // given
        Long stationIdToDelete = STATION_강변역_ID;
        when(stationRepository.findStationById(stationIdToDelete)).thenReturn(STATION_강변역);
        when(sectionRepository.findSectionsByLineId(LINE2_ID))
                .thenReturn(List.of(SECTION_강변역_TO_건대역, SECTION_잠실역_TO_강변역));
        doNothing().when(stationRepository).remove(STATION_강변역);
        when(sectionRepository.insert(SECTION_TO_INSERT_AFTER_DELETE_잠실역_TO_건대역)).thenReturn(SECTION_AFTER_DELETE_잠실역_TO_건대역);

        // when
        Long changedLineId = deleteStationService.deleteStationById(stationIdToDelete);

        // then
        assertThat(changedLineId).isEqualTo(LINE2_ID);
    }
}