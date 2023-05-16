package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.Line;
import subway.integration.IntegrationTest;
import subway.ui.request.SectionRequest;
import subway.ui.request.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;
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
                new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE9.getValue());

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
                new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE9.getValue());
        lineService.registerStation(lineId, sectionRequest);
        final StationRequest stationRequest = new StationRequest(STATION_A.getName());

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
            final SectionRequest sectionRequest = new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE9.getValue());
            final SectionRequest duplicateRequest = new SectionRequest(STATION_B.getName(), STATION_A.getName(), DISTANCE8.getValue());

            lineService.registerStation(lineId, sectionRequest);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("중간 역을 중복 등록한다.")
        @Test
        void centralDuplicate() {
            final long lineId = 1L;
            final SectionRequest sectionRequest = new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE9.getValue());
            final SectionRequest duplicateRequest = new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE8.getValue());

            lineService.registerStation(lineId, sectionRequest);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("마지막 역을 중복 등록한다.")
        @Test
        void tailDuplicate() {
            final long lineId = 1L;
            final SectionRequest sectionRequest1 = new SectionRequest(STATION_A.getName(), STATION_B.getName(), DISTANCE9.getValue());
            final SectionRequest sectionRequest2 = new SectionRequest(STATION_B.getName(), STATION_C.getName(), DISTANCE7.getValue());
            final SectionRequest duplicateRequest = new SectionRequest(STATION_C.getName(), STATION_B.getName(), DISTANCE8.getValue());

            lineService.registerStation(lineId, sectionRequest1);
            lineService.registerStation(lineId, sectionRequest2);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("이름이 같은 역을 등록한다.")
        @Test
        void duplicateNameRegister() {
            final long lineId = 1L;
            final SectionRequest sectionRequest = new SectionRequest(STATION_A.getName(), STATION_A.getName(), DISTANCE9.getValue());

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, sectionRequest));
        }
    }
}
