package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static subway.domain.interstation.InterStationFixture.코다에서_누누_구간_id_1;
import static subway.domain.station.StationFixture.누누_역_id_2;
import static subway.domain.station.StationFixture.두둠_역_id_3;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.interstation.InterStations;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("노선은")
class LineTest {

    @Test
    void 정상적으로_생성된다() {
        final InterStations input = new InterStations(List.of(코다에서_누누_구간_id_1));

        assertThatCode(() -> new Line(1L, "코다선", "bg-blud-600", input))
            .doesNotThrowAnyException();
    }

    @Nested
    @DisplayName("구간을 추가하면")
    class Context_addInterStation {

        @Test
        void 정상적으로_추가된다() {
            final InterStations interStations = new InterStations(List.of(코다에서_누누_구간_id_1));
            final Line line = new Line(1L, "코다선", "bg-blue-600", interStations);

            line.addInterStation(누누_역_id_2.getId(), 두둠_역_id_3.getId(), 1L);

            assertThat(line.getInterStations().getInterStations()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("구간을 삭제하면")
    class Context_removeInterStation {

        @Test
        void 정상적으로_삭제된다() {
            final InterStations interStations = new InterStations(List.of(코다에서_누누_구간_id_1));
            final Line line = new Line(1L, "코다선", "bg-blue-600", interStations);

            line.deleteStation(누누_역_id_2.getId());

            assertThat(line.getInterStations().isEmpty()).isTrue();
        }
    }
}
