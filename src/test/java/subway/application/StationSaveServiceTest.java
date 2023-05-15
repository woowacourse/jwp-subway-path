package subway.application;

import static fixtures.StationFixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.StationRequest;
import subway.dto.StationSaveResponse;

@SpringBootTest
@Transactional
class StationSaveServiceTest {

    @Autowired
    private StationSaveService stationSaveService;

    @Nested
    @DisplayName("노선 이름으로 찾은 노선이 없는 경우")
    class not_found_Line {

        @Test
        @DisplayName("노선을 등록하고 역과 구간을 저장한다. (CASE : A-C)")
        void saveInitialStation() {
            // given
            StationRequest stationRequest = INITIAL_STATION_REQUEST_A_TO_C.REQUEST;
            StationSaveResponse expectedResponse = INITIAL_SAVE_STATION_RESPONSE_A_TO_C.RESPONSE;

            // when
            StationSaveResponse stationSaveResponse = stationSaveService.saveStation(stationRequest);

            // then
            assertThat(stationSaveResponse).usingRecursiveComparison()
                    .ignoringFieldsOfTypes(Long.class).isEqualTo(expectedResponse);

        }
    }

    @Nested
    @DisplayName("노선 이름으로 찾은 노선이 있고")
    @Sql({"/test-schema.sql", "/test-data.sql"})
    class is_exist_Line {

        @Nested
        @DisplayName("저장할 역이 상행역이고")
        class stationToSaveIsUpStation {

            @Test
            @DisplayName("가운데 역일 때 역을 저장하고, 기존 구간 삭제 후 새로운 상행 구간, 하행 구간을 저장한다. (CASE - 요청 : B-C / 변화 : A-C -> A-B-C)")
            void saveSectionWhenUpStationMiddle() {
                // given
                StationRequest request = UP_MIDDLE_STATION_REQUEST_B_TO_C.REQUEST;
                StationSaveResponse expectedResponse = UP_MIDDLE_SAVE_STATION_RESPONSE_B_TO_C.RESPONSE;

                // when
                StationSaveResponse stationSaveResponse = stationSaveService.saveStation(request);

                // then
                assertThat(stationSaveResponse).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(Long.class).isEqualTo(expectedResponse);
            }

            @Test
            @DisplayName("상행 종점 역일 때 역을 저장하고, 새로운 상행 종점 구간을 저장한다. (CASE - 요청 : D-A / 변화 : A-C -> D-A-B)")
            void saveSectionWhenUpStationEnd() {
                // given
                StationRequest request = UP_END_STATION_REQUEST_D_TO_A.REQUEST;
                StationSaveResponse expectedResponse = UP_END_SAVE_STATION_RESPONSE_D_TO_A.RESPONSE;

                // when
                StationSaveResponse stationSaveResponse = stationSaveService.saveStation(request);

                // then
                assertThat(stationSaveResponse).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(Long.class).isEqualTo(expectedResponse);
            }
        }

        @Nested
        @DisplayName("저장할 역이 하행역이고")
        class stationToSaveIsDownStation {

            @Test
            @DisplayName("가운데 역일 때 역을 저장하고, 기존 구간 삭제 후 새로운 상행 구간, 하행 구간을 저장한다.(CASE - 요청 : A-B / 변화 : A-C -> A-B-C)")
            void saveSectionWhenDownStationMiddle() {
                // given
                StationRequest request = DOWN_MIDDLE_STATION_REQUEST_A_TO_B.REQUEST;
                StationSaveResponse expectedResponse = DOWN_MIDDLE_SAVE_STATION_RESPONSE_A_TO_B.RESPONSE;

                // when
                StationSaveResponse stationSaveResponse = stationSaveService.saveStation(request);

                // then
                assertThat(stationSaveResponse).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(Long.class).isEqualTo(expectedResponse);
            }

            @Test
            @DisplayName("하행 종점 역일 때 역을 저장하고, 새로운 하행 종점 구간을 저장한다. (CASE - 요청 : C-E / 변화 : A-C -> A-C-E)")
            void saveSectionWhenUpStationEnd() {
                // given
                StationRequest request = DOWN_END_STATION_REQUEST_C_TO_E.REQUEST;
                StationSaveResponse expectedResponse = DOWN_END_SAVE_STATION_RESPONSE_C_TO_E.RESPONSE;

                // when
                StationSaveResponse stationSaveResponse = stationSaveService.saveStation(request);

                // then
                assertThat(stationSaveResponse).usingRecursiveComparison()
                        .ignoringFieldsOfTypes(Long.class).isEqualTo(expectedResponse);
            }

        }
    }
}
