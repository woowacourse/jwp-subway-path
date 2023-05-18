package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.domain.SectionLocator;
import subway.domain.section.domain.SectionRouter;
import subway.domain.section.entity.SectionEntity;
import subway.domain.station.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionsMapperTest {

    @DisplayName("노선의 경로 조회 테스트")
    @Test
    void getStations() {
        final StationEntity stationEntity1 = new StationEntity(1L, "1L");
        final StationEntity stationEntity2 = new StationEntity(2L, "2L");
        final StationEntity stationEntity3 = new StationEntity(3L, "3L");
        final StationEntity stationEntity4 = new StationEntity(4L, "4L");
        final StationEntity stationEntity5 = new StationEntity(5L, "5L");
        final StationEntity stationEntity6 = new StationEntity(6L, "6L");
        final StationEntity stationEntity7 = new StationEntity(7L, "7L");

        final List<SectionEntity> sectionDetailEntities = List.of(
                new SectionEntity(1L, 1L, stationEntity1.getId(), stationEntity2.getId(), 3),
                new SectionEntity(2L, 1L, stationEntity2.getId(), stationEntity3.getId(), 3),
                new SectionEntity(3L, 1L, stationEntity3.getId(), stationEntity4.getId(), 3),
                new SectionEntity(4L, 1L, stationEntity4.getId(), stationEntity5.getId(), 3),
                new SectionEntity(5L, 1L, stationEntity5.getId(), stationEntity6.getId(), 3),
                new SectionEntity(6L, 1L, stationEntity6.getId(), stationEntity7.getId(), 3)
        );

        SectionLocator sectionLocator = SectionLocator.of(sectionDetailEntities);
        SectionRouter sectionRouter = SectionRouter.of(sectionDetailEntities);

        List<Long> shortestPath = sectionRouter.findShortestPath(sectionLocator.findStartStation(), sectionLocator.findEndStation());

        assertThat(shortestPath).containsExactly(
                stationEntity1.getId(),
                stationEntity2.getId(),
                stationEntity3.getId(),
                stationEntity4.getId(),
                stationEntity5.getId(),
                stationEntity6.getId(),
                stationEntity7.getId()
        );
    }

    @DisplayName("노선의 경로 조회 테스트")
    @Test
    void getStations2() {
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
        SectionRouter sectionRouter = SectionRouter.of(sectionDetailEntities);

        List<Long> shortestPath = sectionRouter.findShortestPath(sectionLocator.findStartStation(), sectionLocator.findEndStation());

        assertThat(shortestPath).containsExactly(
                stationEntity1.getId(),
                stationEntity2.getId(),
                stationEntity3.getId(),
                stationEntity4.getId(),
                stationEntity5.getId(),
                stationEntity6.getId(),
                stationEntity7.getId()
        );
    }
}
