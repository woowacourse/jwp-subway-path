package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static subway.domain.station.StationFixture.누누_역_id_2;
import static subway.domain.station.StationFixture.코다_역_id_1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.station.domain.Station;

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
        Station station2 = new Station(1L, "누누");

        assertThat(station2).isEqualTo(코다_역_id_1);
    }

    @Test
    void id가_다르면_다른_객체이다() {
        assertThat(코다_역_id_1).isNotEqualTo(누누_역_id_2);
    }

    @Test
    void 이름을_변경할_수_있다() {
        final String input = "강남역";
        Station station = new Station(input);
        final String updatedName = "역삼역";

        station.updateName(updatedName);

        assertThat(station.getName().getValue()).isEqualTo(updatedName);
    }
}
