package subway.line.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.line.domain.SectionLocator;
import subway.domain.line.entity.SectionEntity;
import subway.domain.station.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionLocatorTest {

    @Test
    @DisplayName("첫번째역 마지막역 조회")
    void 첫번째역_마지막역_조회() {
        final StationEntity stationEntity5 = new StationEntity(5L, "5L");
        final StationEntity stationEntity6 = new StationEntity(6L, "6L");
        final StationEntity stationEntity7 = new StationEntity(7L, "7L");
        final StationEntity stationEntity1 = new StationEntity(1L, "1L");
        final StationEntity stationEntity2 = new StationEntity(2L, "2L");
        final StationEntity stationEntity3 = new StationEntity(3L, "3L");
        final StationEntity stationEntity4 = new StationEntity(4L, "4L");

        final List<SectionEntity> sectionDetailEntities = List.of(
                new SectionEntity(1L, 1L, stationEntity1.getId(), stationEntity2.getId(), 3),
                new SectionEntity(2L, 1L, stationEntity2.getId(), stationEntity3.getId(), 3),
                new SectionEntity(3L, 1L, stationEntity3.getId(), stationEntity4.getId(), 3),
                new SectionEntity(4L, 1L, stationEntity4.getId(), stationEntity5.getId(), 3),
                new SectionEntity(5L, 1L, stationEntity5.getId(), stationEntity6.getId(), 3),
                new SectionEntity(6L, 1L, stationEntity6.getId(), stationEntity7.getId(), 3)
        );

        SectionLocator sectionLocator = SectionLocator.of(sectionDetailEntities);

        Assertions.assertAll(
                ()->assertThat(sectionLocator.findStartStation()).isEqualTo(1L),
                ()->assertThat(sectionLocator.findEndStation()).isEqualTo(7L)
        );
    }
}
