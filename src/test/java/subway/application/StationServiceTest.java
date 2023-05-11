package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.dto.StationRequest;
import subway.dto.StationSaveResponse;

import java.util.Optional;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StationServiceTest {

    @InjectMocks
    StationService stationService;

    @Mock
    LineDao lineDao;
    @Mock
    StationDao stationDao;
    @Mock
    SectionDao sectionDao;

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
                StationRequest request = 잠실_TO_건대_REQUEST;
                when(lineDao.findByLineName(request.getLineName())).thenReturn(Optional.empty());
                when(lineDao.insert(LINE2_INSERT_ENTITY)).thenReturn(DUMMY_LINE2_ID);
                when(stationDao.insert(잠실_INSERT_ENTITY)).thenReturn(DUMMY_잠실_INSERTED_ID);
                when(stationDao.insert(건대_INSERT_ENTITY)).thenReturn(DUMMY_건대_INSERTED_ID);
                when(sectionDao.insert(잠실_TO_건대_INSERT_SECTION_ENTITY)).thenReturn(DUMMY_SECTION_잠실_TO_건대_ID);

                // when
                StationSaveResponse response = stationService.saveStation(request);

                // then
                assertThat(response).isEqualTo(SAVE_INITIAL_STATIONS_잠실_TO_건대_RESPONSE);
            }
        }

        @Nested
        @DisplayName("노선이 존재하는 경우")
        class existLineTest {

            @Test
            @DisplayName("두 역 모두 존재하지 않으면 예외가 발생한다.")
            void saveNewStationTest_fail_when_allNotSaved() {
                // given
                StationRequest request = 잠실_TO_건대_REQUEST;
                when(lineDao.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_잠실역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.empty());
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_건대역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.empty());

                // when, then
                assertThatThrownBy(() -> stationService.saveStation(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하는 노선이면 현재 등록된 역 중에 하나를 포함해야합니다.");
            }

            @Test
            @DisplayName("두 역 모두 존재하면 예외가 발생한다.")
            void saveNewStationTest_fail_when_allAlreadySaved() {
                // given
                StationRequest request = 잠실_TO_건대_REQUEST;
                when(lineDao.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_잠실역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.of(잠실_FIND_ENTITY));
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_건대역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.of(건대_FIND_ENTITY));

                // when, then
                assertThatThrownBy(() -> stationService.saveStation(request))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("존재하는 노선이면 현재 등록된 역 중에 하나를 포함해야합니다.");
            }

            @Test
            @DisplayName("상행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
            void saveNewStationTest_success_when_upStationExist() {
                // given
                StationRequest request = 잠실_TO_강변_REQUEST;
                when(lineDao.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_잠실역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.of(잠실_FIND_ENTITY));
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_강변역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.empty());
                when(stationDao.insert(강변_INSERT_ENTITY)).thenReturn(DUMMY_강변_INSERTED_ID);
                when(sectionDao.findByUpStationIdAndLindId(DUMMY_잠실_INSERTED_ID, DUMMY_LINE2_ID)).thenReturn(Optional.of(잠실_TO_건대_FIND_SECTION_ENTITY));
                doNothing().when(sectionDao).deleteBySectionId(DUMMY_SECTION_잠실_TO_건대_ID);
                when(sectionDao.insert(잠실_TO_강변_INSERT_SECTION_ENTITY)).thenReturn(DUMMY_SECTION_잠실_TO_강변_ID);
                when(sectionDao.insert(강변_TO_건대_INSERT_SECTION_ENTITY)).thenReturn(DUMMY_SECTION_강변_TO_건대_ID);

                // when
                StationSaveResponse response = stationService.saveStation(request);

                // then
                assertThat(response).isEqualTo(SAVE_NEW_STATION_잠실_TO_강변_RESPONSE);
            }

            @Test
            @DisplayName("하행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
            void saveNewStationTest_success_when_downStationExist() {
                // given
                StationRequest request = 강변_TO_건대_REQUEST;
                when(lineDao.findByLineName(request.getLineName())).thenReturn(Optional.of(LINE2_FIND_ENTITY));
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_강변역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.empty());
                when(stationDao.findByStationNameAndLineName(DUMMY_STATION_건대역_NAME, DUMMY_LINE2_NAME)).thenReturn(Optional.of(건대_FIND_ENTITY));
                when(stationDao.insert(강변_INSERT_ENTITY)).thenReturn(DUMMY_강변_INSERTED_ID);
                when(sectionDao.findByDownStationIdAndLindId(DUMMY_건대_INSERTED_ID, DUMMY_LINE2_ID)).thenReturn(Optional.of(잠실_TO_건대_FIND_SECTION_ENTITY));
                doNothing().when(sectionDao).deleteBySectionId(DUMMY_SECTION_잠실_TO_건대_ID);
                when(sectionDao.insert(잠실_TO_강변_INSERT_SECTION_ENTITY)).thenReturn(DUMMY_SECTION_잠실_TO_강변_ID);
                when(sectionDao.insert(강변_TO_건대_INSERT_SECTION_ENTITY)).thenReturn(DUMMY_SECTION_강변_TO_건대_ID);

                // when
                StationSaveResponse response = stationService.saveStation(request);

                // then
                assertThat(response).isEqualTo(SAVE_NEW_STATION_강변_TO_건대_RESPONSE);
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
            Long stationIdToDelete = DUMMY_잠실_INSERTED_ID;

            // when, then
            assertThatThrownBy(() -> stationService.deleteStationById(stationIdToDelete))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("역 id에 해당하는 역 정보를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("해당 노선에 역이 2개 존재하는 경우 2개의 역 모두 제거되어야 한다.")
        void deleteStationsAndLineTest_when_twoStationsExist() {
            // given
            Long stationIdToDelete = DUMMY_잠실_INSERTED_ID;

            // when


            // then

        }

        @Test
        @DisplayName("상행 종점을 제거하는 경우")
        void deleteUpEndStationTest() {
            // given
            Long stationIdToDelete = DUMMY_잠실_INSERTED_ID;

            // when


            // then

        }

        @Test
        @DisplayName("하행 종점을 제거하는 경우")
        void deleteDownEndStationTest() {
            // given
            Long stationIdToDelete = DUMMY_건대_INSERTED_ID;

            // when


            // then

        }

        @Test
        @DisplayName("두 역 사이에 있는 역을 제거하는 경우 역과 역 사이의 관계 및 거리가 재배치된다.")
        void deleteBetweenStationTest() {
            // given
            Long stationIdToDelete = DUMMY_강변_INSERTED_ID;


            // when


            // then

        }
    }
}