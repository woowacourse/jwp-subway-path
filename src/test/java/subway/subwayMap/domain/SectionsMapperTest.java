package subway.subwayMap.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.section.entity.SectionDetailEntity;
import subway.domain.station.entity.StationEntity;
import subway.domain.line.repository.SectionsMapper;

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

        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 1L, stationEntity1, stationEntity2, 3),
                new SectionDetailEntity(2L, 1L, stationEntity2, stationEntity3, 3),
                new SectionDetailEntity(3L, 1L, stationEntity3, stationEntity4, 3),
                new SectionDetailEntity(4L, 1L, stationEntity4, stationEntity5, 3),
                new SectionDetailEntity(5L, 1L, stationEntity5, stationEntity6, 3),
                new SectionDetailEntity(6L, 1L, stationEntity6, stationEntity7, 3)
        );

        SectionsMapper sectionsMapper = new SectionsMapper();
        List<StationEntity> stationEntities = sectionsMapper.sectionsToStations(sectionDetailEntities);

        assertThat(stationEntities).containsExactly(
                stationEntity1,
                stationEntity2,
                stationEntity3,
                stationEntity4,
                stationEntity5,
                stationEntity6,
                stationEntity7
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

        final List<SectionDetailEntity> sectionDetailEntities = List.of(
                new SectionDetailEntity(1L, 1L, stationEntity1, stationEntity2, 3),
                new SectionDetailEntity(2L, 1L, stationEntity2, stationEntity3, 3),
                new SectionDetailEntity(3L, 1L, stationEntity3, stationEntity4, 3),
                new SectionDetailEntity(4L, 1L, stationEntity4, stationEntity5, 3),
                new SectionDetailEntity(5L, 1L, stationEntity5, stationEntity6, 3),
                new SectionDetailEntity(6L, 1L, stationEntity6, stationEntity7, 3)
        );

        SectionsMapper sectionsMapper = new SectionsMapper();
        List<StationEntity> stationEntities = sectionsMapper.sectionsToStations(sectionDetailEntities);

        assertThat(stationEntities).containsExactly(
                stationEntity1,
                stationEntity2,
                stationEntity3,
                stationEntity4,
                stationEntity5,
                stationEntity6,
                stationEntity7
        );
    }
}
