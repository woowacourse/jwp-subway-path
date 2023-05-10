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
                when(lineDao.insert(LINE2_INSERT_ENTITY)).thenReturn(DUMMY_LINE_ID);
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
                when(sectionDao.findByUpStationId(DUMMY_잠실_INSERTED_ID)).thenReturn(Optional.of(잠실_TO_건대_FIND_SECTION_ENTITY));
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
                when(sectionDao.findByDownStationId(DUMMY_건대_INSERTED_ID)).thenReturn(Optional.of(잠실_TO_건대_FIND_SECTION_ENTITY));
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
}