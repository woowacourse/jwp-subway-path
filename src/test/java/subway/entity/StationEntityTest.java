package subway.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationEntityTest {

    @Test
    void 라인과_라인id를_받아_StationEntity_리스트를_반환한다() {
        // given
        final Line line = new Line("2호선", "RED", List.of(
                new Section("B", "C", 3),
                new Section("A", "B", 2),
                new Section("D", "E", 5),
                new Section("C", "D", 4)
        ));

        // when
        List<StationEntity> result = StationEntity.of(line.findAllStation(), 1L);

        // then
        assertThat(result).extracting(StationEntity::getName).containsAll(List.of("A", "B", "C", "D", "E"));
    }
}
