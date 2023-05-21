package subway.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationAddStrategyTest {

    private StationAddStrategy sut = (sections, base, additional, distance) -> {
    };

    @Test
    void 입력한_역이_입력한_방향에_존재하는_구간을_검색하여_반환한다() {
        // given
        List<Section> sections = List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        );

        // when
        final Section result = sut.findSectionByStationExistsAtDirection(
                sections,
                new Station("A"),
                Direction.LEFT
        ).get();

        // then
        assertThat(result).isEqualTo(new Section("A", "B", 5));
    }
}
