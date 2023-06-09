package subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static subway.interstation.domain.InterStationFixture.코다에서_누누_구간_id_1;
import static subway.station.domain.StationFixture.누누_역_id_2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.line.domain.interstation.InterStations;
import subway.line.domain.line.Line;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("노선은")
class LineTest {

    @Test
    void 정상적으로_생성된다() {
        InterStations input = new InterStations(List.of(코다에서_누누_구간_id_1));

        assertThatCode(() -> new Line(1L, "코다선", "bg-blud-600", input))
                .doesNotThrowAnyException();
    }

    @Test
    void 색을_변경할_수_있다() {
        InterStations interStations = new InterStations(List.of(코다에서_누누_구간_id_1));
        Line line = new Line(1L, "코다선", "bg-blue-600", interStations);
        final String updatedColor = "bg-green-600";

        line.updateColor(updatedColor);

        assertThat(line.getColor().getValue()).isEqualTo(updatedColor);
    }

    @Test
    void 이름을_변경할_수_있다() {
        InterStations interStations = new InterStations(List.of(코다에서_누누_구간_id_1));
        Line line = new Line(1L, "코다선", "bg-blue-600", interStations);
        final String updatedName = "누누선";

        line.updateName(updatedName);

        assertThat(line.getName().getValue()).isEqualTo(updatedName);
    }

    @Test
    void id가_같으면_같은_객체이다() {
        InterStations interStations = new InterStations(List.of(코다에서_누누_구간_id_1));
        Line line2 = new Line(1L, "코다선", "bg-blue-600", interStations);

        assertThat(line2).isEqualTo(new Line(1L, "코다선", "bg-blue-600", interStations));
    }

    @Nested
    @DisplayName("구간을 추가하면")
    class Context_addInterStation {

        @Test
        void 정상적으로_추가된다() {
            InterStations interStations = new InterStations(List.of(코다에서_누누_구간_id_1));
            Line line = new Line(1L, "코다선", "bg-blue-600", interStations);

            line.addInterStation(누누_역_id_2.getId(), null, 4L, 1L);

            assertThat(line.getInterStations().getInterStations()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("구간을 삭제하면")
    class Context_removeInterStation {

        @Test
        void 정상적으로_삭제된다() {
            InterStations interStations = new InterStations(List.of(코다에서_누누_구간_id_1));
            Line line = new Line(1L, "코다선", "bg-blue-600", interStations);

            line.deleteStation(누누_역_id_2.getId());

            assertThat(line.getInterStations().isEmpty()).isTrue();
        }
    }
}
