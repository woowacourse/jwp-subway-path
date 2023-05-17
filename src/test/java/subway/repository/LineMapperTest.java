package subway.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.core.Distance;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.domain.core.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class LineMapperTest {

    private LineMapper lineMapper = new LineMapper();

    @Test
    void Line을_입력받아_SectionEntity_리스트를_반환한다() {
        // given
        final Long lineId = 1L;
        final Line line = new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 2),
                new Section("B", "Y", 3)
        ));
        final List<StationEntity> stationEntities = List.of(
                new StationEntity(1L, "Z", lineId),
                new StationEntity(2L, "B", lineId),
                new StationEntity(3L, "Y", lineId)
        );

        // when
        final List<SectionEntity> result = lineMapper.toSectionEntities(line, lineId, stationEntities);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new SectionEntity(1L, 2L, 2, lineId),
                new SectionEntity(2L, 3L, 3, lineId)
        ));
    }

    @Test
    void Line을_입력받아_StationEntity_리스트를_반환한다() {
        // given
        final Long lineId = 1L;
        final Line line = new Line("2호선", "BLUE", 0, List.of(
                new Section("Z", "B", 2),
                new Section("B", "Y", 3)
        ));

        // when
        final List<StationEntity> result = lineMapper.toStationEntities(line, lineId);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(List.of(
                new StationEntity("Z", lineId),
                new StationEntity("B", lineId),
                new StationEntity("Y", lineId)
        ));
    }

    @Test
    void 엔티티들을_입력받아_Line을_반환한다() {
        // given
        final LineEntity lineEntity = new LineEntity(1L, "2호선", "BLUE", 0);
        final Long lineId = 1L;
        final List<SectionEntity> sectionEntities = List.of(
                new SectionEntity(1L, 1L, 2L, 3, lineId),
                new SectionEntity(2L, 2L, 3L, 5, lineId)
        );
        final List<StationEntity> stationEntities = List.of(
                new StationEntity(1L, "Z", lineId),
                new StationEntity(2L, "B", lineId),
                new StationEntity(3L, "Y", lineId)
        );

        // when
        final Line result = lineMapper.toLine(lineEntity, sectionEntities, stationEntities);

        // then
        assertThat(result).usingRecursiveComparison().isEqualTo(
                new Line(1L, "2호선", "BLUE", 0, List.of(
                        new Section(
                                1L,
                                new Station(1L, "Z"),
                                new Station(2L, "B"),
                                new Distance(3)
                        ),
                        new Section(
                                2L,
                                new Station(2L, "B"),
                                new Station(3L, "Y"),
                                new Distance(5)
                        )
                ))
        );
    }
}
