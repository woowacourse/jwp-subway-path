package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.domain.line.LineRepository;
import subway.domain.section.SectionRepository;
import subway.dto.StationRequest;

import java.util.List;
import java.util.Optional;

import static fixtures.LineFixtures.*;
import static fixtures.SectionFixtures.*;
import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveSectionServiceTest {

    @InjectMocks
    SaveSectionService saveSectionService;

    @Mock
    LineRepository lineRepository;
    @Mock
    SectionRepository sectionRepository;

    @Nested
    @DisplayName("노선이 존재하지 않는 경우")
    class emptyLineTest {


        @Test
        @DisplayName("노선, 상행역과 하행역, 구간 정보를 저장한다.")
        void saveInitialSectionTest() {
            // given
            StationRequest request = REQUEST_잠실역_TO_건대역;
            when(lineRepository.findByLineName(request.getLineName())).thenReturn(Optional.empty());
            when(lineRepository.insert(LINE2_TO_INSERT)).thenReturn(LINE2);
            when(sectionRepository.insert(SECTION_TO_INSERT_잠실역_TO_건대역)).thenReturn(SECTION_잠실역_TO_건대역);

            // when
            Long changedLineId = saveSectionService.saveSection(request);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }
    }

    @Nested
    @DisplayName("노선이 존재하는 경우")
    class existLineTest {

        @Test
        @DisplayName("두 역 모두 존재하지 않으면 예외가 발생한다.")
        void saveNewStationTest_fail_when_allNotSaved() {
            // given
            StationRequest request = REQUEST_대림역_TO_신림역;
            when(lineRepository.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2));
            when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of(SECTION_잠실역_TO_건대역));
            // when, then
            assertThatThrownBy(() -> saveSectionService.saveSection(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("현재 등록된 역 중에 하나를 포함해야합니다.");
        }

        @Test
        @DisplayName("이미 저장되어 있는 구간 정보이면 예외가 발생한다.")
        void saveNewStationTest_fail_when_allAlreadySaved() {
            // given
            StationRequest request = REQUEST_잠실역_TO_건대역;
            when(lineRepository.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2));
            when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of(SECTION_잠실역_TO_건대역));

            // when, then
            assertThatThrownBy(() -> saveSectionService.saveSection(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 포함되어 있는 구간입니다.");
        }

        @Test
        @DisplayName("노선 가운데에 역을 등록함으로써 역과 역 사이의 거리가 음수가 된다면 예외가 발생한다.")
        void saveNewStationTest_fail_negativeDistance() {
            // given
            StationRequest request = REQUEST_LONG_DISTANCE;
            when(lineRepository.findByLineName(LINE2_NAME)).thenReturn(Optional.of(LINE2));
            when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of(SECTION_잠실역_TO_건대역));

            assertThatThrownBy(() -> saveSectionService.saveSection(request))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("역 사이 거리는 0km이상 100km 이하여야 합니다.");
        }

        @Test
        @DisplayName("상행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
        void saveNewStationTest_success_when_upStationExist() {
            // given
            StationRequest request = REQUEST_잠실역_TO_강변역;
            when(lineRepository.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2));
            when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of(SECTION_잠실역_TO_건대역));
            when(sectionRepository.insert(SECTION_AFTER_CALCULATE_강변역_TO_건대역)).thenReturn(SECTION_강변역_TO_건대역);
            when(sectionRepository.insert(SECTION_TO_INSERT_잠실역_TO_강변역)).thenReturn(SECTION_잠실역_TO_강변역);
            doNothing().when(sectionRepository).remove(SECTION_잠실역_TO_건대역);

            // when
            Long changedLineId = saveSectionService.saveSection(request);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }

        @Test
        @DisplayName("하행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
        void saveNewStationTest_success_when_downStationExist() {
            // given
            StationRequest request = REQUEST_강변역_TO_건대역;
            when(lineRepository.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2));
            when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of(SECTION_잠실역_TO_건대역));
            when(sectionRepository.insert(SECTION_AFTER_CALCULATE_잠실역_TO_강변역)).thenReturn(SECTION_잠실역_TO_강변역);
            when(sectionRepository.insert(SECTION_TO_INSERT_강변역_TO_건대역)).thenReturn(SECTION_강변역_TO_건대역);
            doNothing().when(sectionRepository).remove(SECTION_잠실역_TO_건대역);

            // when
            Long changedLineId = saveSectionService.saveSection(request);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }

        @Test
        @DisplayName("기존 상행 종점의 상행역을 등록한다.")
        void saveNewStationTest_success_whenSaveUpEndStation() {
            // given
            StationRequest request = REQUEST_대림역_TO_잠실역;
            when(lineRepository.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2));
            when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of(SECTION_잠실역_TO_건대역));
            when(sectionRepository.insert(SECTION_TO_INSERT_대림역_TO_잠실역)).thenReturn(SECTION_대림역_TO_잠실역);

            // when
            Long changedLineId = saveSectionService.saveSection(request);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }

        @Test
        @DisplayName("기존 하행 종점의 하행역을 등록한다.")
        void saveNewStationTest_success_whenSaveDownEndStation() {
            // given
            StationRequest request = REQUEST_건대역_TO_성수역;
            when(lineRepository.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2));
            when(sectionRepository.findSectionsByLineId(LINE2_ID)).thenReturn(List.of(SECTION_잠실역_TO_건대역));
            when(sectionRepository.insert(SECTION_TO_INSERT_건대역_TO_성수역)).thenReturn(SECTION_건대역_TO_성수역);

            // when
            Long changedLineId = saveSectionService.saveSection(request);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }
    }
}