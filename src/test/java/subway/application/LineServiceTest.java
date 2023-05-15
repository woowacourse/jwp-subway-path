package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.dto.request.SectionRequest;
import subway.dto.request.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@JdbcTest
@Import({LineService.class, LineDao.class, StationDao.class, SectionDao.class, StationService.class, SectionService.class})
class LineServiceTest {

    @Autowired
    private LineService lineService;

    @DisplayName("노선에 역을 등록한다.")
    @Test
    void registerLine() {
        final long lineId = 1L;
        final SectionRequest sectionRequest =
                new SectionRequest("잠실", "강남", 10);

        lineService.registerStation(lineId, sectionRequest);

        final Line line = lineService.findById(lineId);
        assertAll(
                () -> assertThat(line.getSections().getSections().get(0).getBeforeStation().getName()).isEqualTo("잠실"),
                () -> assertThat(line.getSections().getSections().get(0).getNextStation().getName()).isEqualTo("강남"),
                () -> assertThat(line.getSections().getSections().get(0).getDistance().getValue()).isEqualTo(10)
        );
    }

    @DisplayName("노선에 역을 삭제한다.")
    @Test
    void unregisterLine() {
        final long lineId = 1L;
        final SectionRequest sectionRequest =
                new SectionRequest("잠실", "강남", 10);
        lineService.registerStation(lineId, sectionRequest);
        final StationRequest stationRequest = new StationRequest("잠실");

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
            final SectionRequest sectionRequest = new SectionRequest("잠실", "강남", 10);
            final SectionRequest duplicateRequest = new SectionRequest("강남", "잠실", 8);

            lineService.registerStation(lineId, sectionRequest);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("중간 역을 중복 등록한다.")
        @Test
        void centralDuplicate() {
            final long lineId = 1L;
            final SectionRequest sectionRequest = new SectionRequest("잠실", "강남", 10);
            final SectionRequest duplicateRequest = new SectionRequest("잠실", "강남", 8);

            lineService.registerStation(lineId, sectionRequest);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("마지막 역을 중복 등록한다.")
        @Test
        void tailDuplicate() {
            final long lineId = 1L;
            final SectionRequest sectionRequest1 = new SectionRequest("잠실", "강남", 10);
            final SectionRequest sectionRequest2 = new SectionRequest("강남", "선릉", 7);
            final SectionRequest duplicateRequest = new SectionRequest("선릉", "강남", 8);

            lineService.registerStation(lineId, sectionRequest1);
            lineService.registerStation(lineId, sectionRequest2);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, duplicateRequest));
        }

        @DisplayName("이름이 같은 역을 등록한다.")
        @Test
        void duplicateNameRegister() {
            final long lineId = 1L;
            final SectionRequest sectionRequest = new SectionRequest("잠실", "잠실", 10);

            assertThrows(IllegalArgumentException.class, () -> lineService.registerStation(lineId, sectionRequest));
        }
    }
}
