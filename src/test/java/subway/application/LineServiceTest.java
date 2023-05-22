package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.application.request.SectionRequest;
import subway.application.request.StationRequest;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.integration.IntegrationTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static subway.integration.IntegrationFixture.*;

class LineServiceTest extends IntegrationTest {

    @Autowired
    private LineService lineService;

    @DisplayName("노선에 역을 등록한다.")
    @Test
    void registerLine() {
        final long lineId = 1L;
        final SectionRequest sectionRequest =
                new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE9.getValue());

        lineService.registerStation(lineId, sectionRequest);

        final Line line = lineService.findById(lineId);
        assertAll(
                () -> assertThat(line.getSections().getSections().get(0).getBeforeStation().getName()).isEqualTo(STATION_A.getName()),
                () -> assertThat(line.getSections().getSections().get(0).getNextStation().getName()).isEqualTo(STATION_B.getName()),
                () -> assertThat(line.getSections().getSections().get(0).getDistance().getValue()).isEqualTo(DISTANCE9.getValue())
        );
    }

    @DisplayName("노선에 역을 삭제한다.")
    @Test
    void unregisterLine() {
        final long lineId = 1L;
        final SectionRequest sectionRequest =
                new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE9.getValue());
        lineService.registerStation(lineId, sectionRequest);
        final StationRequest stationRequest = new StationRequest(STATION_A.getName().getValue());

        lineService.unregisterStation(lineId, stationRequest);

        final Line line = lineService.findById(lineId);
        assertThat(line.getSections().getSections())
                .isEmpty();
    }

    @Nested
    @DisplayName("노선에 역을 중복 등록한다.")
    class DuplicateRegister {

        @DisplayName("첫 역을 중복 등록한다.")
        @Test
        void headDuplicate() {
            final long lineId = 1L;
            final SectionRequest sectionRequest = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE9.getValue());
            final SectionRequest duplicateRequest = new SectionRequest(STATION_B.getName().getValue(), STATION_A.getName().getValue(), DISTANCE8.getValue());

            lineService.registerStation(lineId, sectionRequest);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("중간 역을 중복 등록한다.")
        @Test
        void centralDuplicate() {
            final long lineId = 1L;
            final SectionRequest sectionRequest = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE9.getValue());
            final SectionRequest duplicateRequest = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE8.getValue());

            lineService.registerStation(lineId, sectionRequest);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("마지막 역을 중복 등록한다.")
        @Test
        void tailDuplicate() {
            final long lineId = 1L;
            final SectionRequest sectionRequest1 = new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE9.getValue());
            final SectionRequest sectionRequest2 = new SectionRequest(STATION_B.getName().getValue(), STATION_C.getName().getValue(), DISTANCE7.getValue());
            final SectionRequest duplicateRequest = new SectionRequest(STATION_C.getName().getValue(), STATION_B.getName().getValue(), DISTANCE8.getValue());

            lineService.registerStation(lineId, sectionRequest1);
            lineService.registerStation(lineId, sectionRequest2);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("이름이 같은 역을 등록한다.")
        @Test
        void duplicateNameRegister() {
            final long lineId = 1L;
            final SectionRequest sectionRequest = new SectionRequest(STATION_A.getName().getValue(), STATION_A.getName().getValue(), DISTANCE9.getValue());

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, sectionRequest));
        }
    }

    @Nested
    @DisplayName("여러 역이 등록된 노선에 대해 테스트한다")
    class multipleStationTest {

        private final Long lineId = 1L;

        @BeforeEach
        void setUp() {
            lineService.registerStation(lineId, new SectionRequest(STATION_A.getName().getValue(), STATION_B.getName().getValue(), DISTANCE5.getValue()));
            lineService.registerStation(lineId, new SectionRequest(STATION_B.getName().getValue(), STATION_C.getName().getValue(), DISTANCE5.getValue()));
            lineService.registerStation(lineId, new SectionRequest(STATION_C.getName().getValue(), STATION_D.getName().getValue(), DISTANCE5.getValue()));
        }

        @DisplayName("상행 종점에 역을 추가한다.")
        @Test
        void addStationHead() {
            final SectionRequest request = new SectionRequest(STATION_E.getName().getValue(), STATION_A.getName().getValue(), DISTANCE5.getValue());

            lineService.registerStation(lineId, request);

            final Line line = lineService.findById(lineId);
            assertThat(line.getSections().getSections())
                    .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_E, STATION_A, DISTANCE5),
                            tuple(STATION_A, STATION_B, DISTANCE5),
                            tuple(STATION_B, STATION_C, DISTANCE5),
                            tuple(STATION_C, STATION_D, DISTANCE5)
                    );
        }

        @DisplayName("중간에 역을 추가한다.")
        @Test
        void addStationCenter() {
            final SectionRequest request = new SectionRequest(STATION_B.getName().getValue(), STATION_E.getName().getValue(), DISTANCE3.getValue());

            lineService.registerStation(lineId, request);

            final Line line = lineService.findById(lineId);
            assertThat(line.getSections().getSections())
                    .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, STATION_B, DISTANCE5),
                            tuple(STATION_B, STATION_E, DISTANCE3),
                            tuple(STATION_E, STATION_C, DISTANCE2),
                            tuple(STATION_C, STATION_D, DISTANCE5)
                    );
        }

        @DisplayName("하행종점에 역을 추가한다.")
        @Test
        void addStationTail() {
            final SectionRequest request = new SectionRequest(STATION_D.getName().getValue(), STATION_E.getName().getValue(), DISTANCE5.getValue());

            lineService.registerStation(lineId, request);

            final Line line = lineService.findById(lineId);
            assertThat(line.getSections().getSections())
                    .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, STATION_B, DISTANCE5),
                            tuple(STATION_B, STATION_C, DISTANCE5),
                            tuple(STATION_C, STATION_D, DISTANCE5),
                            tuple(STATION_D, STATION_E, DISTANCE5)
                    );
        }

        @DisplayName("상행종점에 역을 삭제한다.")
        @Test
        void removeStationHead() {
            final StationRequest request = new StationRequest(STATION_A.getName().getValue());

            lineService.unregisterStation(lineId, request);

            final Line line = lineService.findById(lineId);
            assertThat(line.getSections().getSections())
                    .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_B, STATION_C, DISTANCE5),
                            tuple(STATION_C, STATION_D, DISTANCE5)
                    );
        }

        @DisplayName("중간에 역을 삭제한다.")
        @Test
        void removeStationCenter() {
            final StationRequest request = new StationRequest(STATION_C.getName().getValue());

            lineService.unregisterStation(lineId, request);

            final Line line = lineService.findById(lineId);
            assertThat(line.getSections().getSections())
                    .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, STATION_B, DISTANCE5),
                            tuple(STATION_B, STATION_D, DISTANCE10)
                    );
        }

        @DisplayName("하행종점 역을 삭제한다.")
        @Test
        void removeStationTail() {
            final StationRequest request = new StationRequest(STATION_D.getName().getValue());

            lineService.unregisterStation(lineId, request);

            final Line line = lineService.findById(lineId);
            assertThat(line.getSections().getSections())
                    .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, STATION_B, DISTANCE5),
                            tuple(STATION_B, STATION_C, DISTANCE5)
                    );
        }

        @DisplayName("중간에 역을 삭제하고 추가한다.")
        @Test
        void removeAndAddStation() {
            final StationRequest unregisterRequest = new StationRequest(STATION_C.getName().getValue());
            final SectionRequest registerRequest = new SectionRequest(STATION_B.getName().getValue(), STATION_E.getName().getValue(), DISTANCE3.getValue());

            lineService.unregisterStation(lineId, unregisterRequest);
            lineService.registerStation(lineId, registerRequest);


            final Line line = lineService.findById(lineId);
            assertThat(line.getSections().getSections())
                    .extracting(Section::getBeforeStation, Section::getNextStation, Section::getDistance)
                    .containsExactly(
                            tuple(STATION_A, STATION_B, DISTANCE5),
                            tuple(STATION_B, STATION_E, DISTANCE3),
                            tuple(STATION_E, STATION_D, DISTANCE7)
                    );
        }
    }
}
