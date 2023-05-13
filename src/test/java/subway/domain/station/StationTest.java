package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("역은")
class StationTest {

    @Test
    void 정상적으로_생성된다() {
        final String input = "강남역";

        assertThatCode(() -> new Station(input))
            .doesNotThrowAnyException();
    }

    @Test
    void id가_같으면_같은_객체이다() {
        final Station station1 = new Station(1L, "코다");
        final Station station2 = new Station(1L, "누누");

        assertThat(station1).isEqualTo(station2);
    }

    @Test
    void id가_다르면_다른_객체이다() {
        final Station station1 = new Station(1L, "코다");
        final Station station2 = new Station(2L, "누누");

        assertThat(station1).isNotEqualTo(station2);
    }
}
