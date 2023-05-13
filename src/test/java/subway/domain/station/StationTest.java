package subway.domain.station;

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
}
