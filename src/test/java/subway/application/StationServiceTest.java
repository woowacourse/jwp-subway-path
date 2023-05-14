package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.domain.section.SectionRepository;
import subway.domain.station.StationRepository;
import subway.dto.StationRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    StationService stationService;

    @Mock
    LineDao lineDao;
    @Mock
    SectionRepository sectionRepository;
    @Mock
    StationRepository stationRepository;

    @Nested
    @DisplayName("역 등록 시 ")
    class saveStationTest {

        @Nested
        @DisplayName("노선이 존재하지 않는 경우")
        class emptyLineTest {


            @Test
            @DisplayName("상행역과 하행역, 노선, 구간 정보를 저장한다.")
            void saveInitialStationsTest() {
                // given
                StationRequest request = REQUEST_잠실역_TO_건대역;
                when(lineDao.findByLineName(request.getLineName()))
                        .thenReturn(Optional.empty());
                when(lineDao.insert(LINE2_INSERT_ENTITY)).thenReturn(LINE2_FIND_ENTITY);
                when(sectionRepository.findSectionsContaining(SECTION_TO_INSERT_잠실역_TO_건대역))
                        .thenReturn(new ArrayList<>());

                // when
                Long changedLineId = stationService.saveSection(request);

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
                when(lineDao.findByLineName(request.getLineName()))
                        .thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(sectionRepository.findSectionsContaining(SECTION_TO_INSERT_대림역_TO_신림역))
                        .thenReturn(new ArrayList<>());

                // when, then
                assertThatThrownBy(() -> stationService.saveSection(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("현재 등록된 역 중에 하나를 포함해야합니다.");
            }

            @Test
            @DisplayName("두 역 모두 존재하면 예외가 발생한다.")
            void saveNewStationTest_fail_when_allAlreadySaved() {
                // given
                StationRequest request = REQUEST_잠실역_TO_건대역;
                when(lineDao.findByLineName(request.getLineName()))
                        .thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(sectionRepository.findSectionsContaining(SECTION_TO_INSERT_잠실역_TO_건대역))
                        .thenReturn(List.of(SECTION_잠실역_TO_건대역));

                // when, then
                assertThatThrownBy(() -> stationService.saveSection(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("이미 포함되어 있는 구간입니다.");
            }

            @Test
            @DisplayName("상행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
            void saveNewStationTest_success_when_upStationExist() {
                // given
                StationRequest request = REQUEST_잠실역_TO_강변역;
                when(lineDao.findByLineName(request.getLineName()))
                        .thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(sectionRepository.findSectionsContaining(SECTION_TO_INSERT_잠실역_TO_강변역))
                        .thenReturn(List.of(SECTION_잠실역_TO_건대역));

                // when
                Long changedLineId = stationService.saveSection(request);

                // then
                assertThat(changedLineId).isEqualTo(LINE2_ID);
            }

            @Test
            @DisplayName("하행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
            void saveNewStationTest_success_when_downStationExist() {
                // given
                StationRequest request = REQUEST_강변역_TO_건대역;
                when(lineDao.findByLineName(request.getLineName()))
                        .thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(sectionRepository.findSectionsContaining(SECTION_TO_INSERT_강변역_TO_건대역))
                        .thenReturn(List.of(SECTION_잠실역_TO_건대역));

                // when
                Long changedLineId = stationService.saveSection(request);

                // then
                assertThat(changedLineId).isEqualTo(LINE2_ID);
            }
        }
    }

    @Nested
    @DisplayName("역 삭제 시 ")
    class deleteStationTest {

        @Test
        @DisplayName("해당하는 역이 없으면 예외가 발생한다.")
        void deleteStationTest_fail_when_stationNotExist() {
            // given
            Long stationIdToDelete = STATION_잠실역_ID;
            when(stationRepository.findStationById(stationIdToDelete)).thenReturn(Optional.empty());

            // when, then
            assertThatThrownBy(() -> stationService.deleteStationById(stationIdToDelete))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("역 id에 해당하는 역 정보를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("해당 노선에 역이 2개 존재하는 경우 2개의 역 모두 제거되어야 한다.")
        void deleteStationsAndLineTest_when_twoStationsExist() {
            // given
            Long stationIdToDelete = STATION_잠실역_ID;
            when(stationRepository.findStationById(stationIdToDelete)).thenReturn(Optional.of(STATION_잠실역));
            when(sectionRepository.findSectionsByLineId(LINE2_ID))
                    .thenReturn(List.of(SECTION_잠실역_TO_건대역));

            // when
            Long changedLineId = stationService.deleteStationById(stationIdToDelete);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }

        @Test
        @DisplayName("상행 종점을 제거하는 경우")
        void deleteUpEndStationTest() {
            // given
            Long stationIdToDelete = STATION_잠실역_ID;
            when(stationRepository.findStationById(stationIdToDelete)).thenReturn(Optional.of(STATION_잠실역));
            when(sectionRepository.findSectionsByLineId(LINE2_ID))
                    .thenReturn(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역));

            // when
            Long changedLineId = stationService.deleteStationById(stationIdToDelete);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }

        @Test
        @DisplayName("하행 종점을 제거하는 경우")
        void deleteDownEndStationTest() {
            // given
            Long stationIdToDelete = STATION_건대역_ID;
            when(stationRepository.findStationById(stationIdToDelete)).thenReturn(Optional.of(STATION_건대역));
            when(sectionRepository.findSectionsByLineId(LINE2_ID))
                    .thenReturn(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역));

            // when
            Long changedLineId = stationService.deleteStationById(stationIdToDelete);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }

        @Test
        @DisplayName("두 역 사이에 있는 역을 제거하는 경우 역과 역 사이의 관계 및 거리가 재배치된다.")
        void deleteBetweenStationTest() {
            // given
            Long stationIdToDelete = STATION_강변역_ID;
            when(stationRepository.findStationById(stationIdToDelete)).thenReturn(Optional.of(STATION_강변역));
            when(sectionRepository.findSectionsByLineId(LINE2_ID))
                    .thenReturn(List.of(SECTION_잠실역_TO_강변역, SECTION_강변역_TO_건대역));

            // when
            Long changedLineId = stationService.deleteStationById(stationIdToDelete);

            // then
            assertThat(changedLineId).isEqualTo(LINE2_ID);
        }
    }
}