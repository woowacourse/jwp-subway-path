package subway.line.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.SectionRouter;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class SectionRouterTest {

    @Test
    @DisplayName("최단 거리 조회")
    void 최단_거리_조회() {
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

        SectionRouter sectionRouter = SectionRouter.of(sectionDetailEntities);

        List<Long> shortestPath = sectionRouter.findShortestPath(1L,7L);

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

    @Test
    @DisplayName("최단 거리 조회")
    void 환승_최단_거리_조회() {
        final StationEntity stationEntity5 = new StationEntity(5L, "5L");
        final StationEntity stationEntity6 = new StationEntity(6L, "6L");
        final StationEntity stationEntity7 = new StationEntity(7L, "7L");
        final StationEntity stationEntity1 = new StationEntity(1L, "1L");
        final StationEntity stationEntity2 = new StationEntity(2L, "2L");
        final StationEntity stationEntity3 = new StationEntity(3L, "3L");
        final StationEntity stationEntity4 = new StationEntity(4L, "4L");
        final StationEntity stationEntity8 = new StationEntity(8L, "8L");
        final StationEntity stationEntity9 = new StationEntity(9L, "9L");
        final StationEntity stationEntity10 = new StationEntity(10L, "10L");
        final StationEntity stationEntity11 = new StationEntity(11L, "11L");


        final List<SectionEntity> sectionDetailEntities = List.of(
                new SectionEntity(1L, 1L, stationEntity1.getId(), stationEntity2.getId(), 3),
                new SectionEntity(2L, 1L, stationEntity2.getId(), stationEntity3.getId(), 3),
                new SectionEntity(3L, 1L, stationEntity3.getId(), stationEntity4.getId(), 3),
                new SectionEntity(4L, 1L, stationEntity4.getId(), stationEntity5.getId(), 3),
                new SectionEntity(5L, 1L, stationEntity5.getId(), stationEntity6.getId(), 3),
                new SectionEntity(6L, 1L, stationEntity6.getId(), stationEntity7.getId(), 3),

                new SectionEntity(8L, 2L, stationEntity8.getId(), stationEntity9.getId(), 3),
                new SectionEntity(9L, 2L, stationEntity9.getId(), stationEntity4.getId(), 3),
                new SectionEntity(10L, 2L, stationEntity4.getId(), stationEntity10.getId(), 3),
                new SectionEntity(11L, 2L, stationEntity10.getId(), stationEntity11.getId(), 3)

        );

        SectionRouter sectionRouter = SectionRouter.of(sectionDetailEntities);

        List<Long> shortestPath = sectionRouter.findShortestPath(1L,7L);

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
