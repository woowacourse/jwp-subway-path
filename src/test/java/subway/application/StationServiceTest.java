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

import static fixtures.StationFixtures.*;

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


                // when


                // then

            }
        }

        @Nested
        @DisplayName("노선이 존재하는 경우")
        class existLineTest {

            @Test
            @DisplayName("두 역 모두 존재하지 않으면 예외가 발생한다.")
            void saveNewStationTest_fail_when_allNotSaved() {
                // given


                // when


                // then

            }

            @Test
            @DisplayName("두 역 모두 존재하면 예외가 발생한다.")
            void saveNewStationTest_fail_when_allAlreadySaved() {
                // given


                // when, then

            }

            @Test
            @DisplayName("상행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
            void saveNewStationTest_success_when_upStationExist() {
                // given


                // when


                // then

            }

            @Test
            @DisplayName("하행역이 이미 존재할 때, 노선 가운데에 역을 등록한다.")
            void saveNewStationTest_success_when_downStationExist() {
                // given


                // when


                // then

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


            // when


            // then

        }

        @Test
        @DisplayName("해당 노선에 역이 2개 존재하는 경우 2개의 역 모두 제거되어야 한다.")
        void deleteStationsAndLineTest_when_twoStationsExist() {
            // given
            Long stationIdToDelete = STATION_잠실역_ID;

            // when


            // then

        }

        @Test
        @DisplayName("상행 종점을 제거하는 경우")
        void deleteUpEndStationTest() {
            // given
            Long stationIdToDelete = STATION_잠실역_ID;

            // when


            // then

        }

        @Test
        @DisplayName("하행 종점을 제거하는 경우")
        void deleteDownEndStationTest() {
            // given
            Long stationIdToDelete = STATION_건대역_ID;

            // when


            // then

        }

        @Test
        @DisplayName("두 역 사이에 있는 역을 제거하는 경우 역과 역 사이의 관계 및 거리가 재배치된다.")
        void deleteBetweenStationTest() {
            // given
            Long stationIdToDelete = STATION_강변역_ID;


            // when


            // then

        }
    }
}