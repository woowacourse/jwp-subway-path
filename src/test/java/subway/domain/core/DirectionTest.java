package subway.domain.core;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DirectionTest {

    @CsvSource({"LEFT, RIGHT", "RIGHT, LEFT"})
    @ParameterizedTest(name = "{0}의 반대 방향은 {1}이다.")
    void 반대_방향을_반환한다(final Direction sut, final Direction result) {
        // expect
        assertThat(sut.flip()).isEqualTo(result);
    }

    @Test
    void 구간을_추가한다() {
        // given
        List<Section> sections = new ArrayList<>(List.of(
                new Section("A", "B", 5),
                new Section("B", "C", 5)
        ));

        // when
        Direction.LEFT.addStation(sections, new Station("B"), new Station("D"), new Distance(2));

        // then
        assertThat(sections).contains(
                new Section("A", "D", 3),
                new Section("D", "B", 2),
                new Section("B", "C", 5)
        );
    }
}
