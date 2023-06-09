package subway.interstation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static subway.interstation.domain.InterStationFixture.누누에서_두둠_구간_id_2;
import static subway.interstation.domain.InterStationFixture.두둠에서_처음보는_역_id_3;
import static subway.interstation.domain.InterStationFixture.코다에서_누누_구간_id_1;
import static subway.station.domain.StationFixture.누누_역_id_2;
import static subway.station.domain.StationFixture.두둠_역_id_3;
import static subway.station.domain.StationFixture.처음보는_역_id_4;
import static subway.station.domain.StationFixture.코다_역_id_1;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.line.domain.interstation.InterStation;
import subway.line.domain.interstation.InterStations;
import subway.line.domain.interstation.exception.InterStationsException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("구간들은")
class InterStationsTest {

    @Test
    void 정상적으로_생성된다() {
        List<InterStation> given = List.of(코다에서_누누_구간_id_1);

        assertThatCode(() -> new InterStations(given))
                .doesNotThrowAnyException();
    }

    @Test
    void null_이면_예외가_발생한다() {
        assertThatCode(() -> new InterStations(null))
                .isInstanceOf(InterStationsException.class)
                .hasMessage("구간이 비어있습니다.");
    }

    @Test
    void 비어있으면_예외가_발생한다() {
        List<InterStation> given = List.of();

        assertThatCode(() -> new InterStations(given))
                .isInstanceOf(InterStationsException.class)
                .hasMessage("구간이 비어있습니다.");
    }

    @Test
    void 역을_통해서_생성할_수_있다() {
        assertThatCode(() -> InterStations.of(코다_역_id_1.getId(), 누누_역_id_2.getId(), 1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 구간은_항상_정렬된_상태로_유지된다() {
        List<InterStation> given = List.of(
                누누에서_두둠_구간_id_2,
                코다에서_누누_구간_id_1,
                두둠에서_처음보는_역_id_3
        );

        List<InterStation> result = new InterStations(given).getInterStations();

        assertThat(result).containsExactly(
                코다에서_누누_구간_id_1,
                누누에서_두둠_구간_id_2,
                두둠에서_처음보는_역_id_3
        );
    }

    @Test
    void 구간은_중복되지_않는다() {
        List<InterStation> given = List.of(
                코다에서_누누_구간_id_1,
                코다에서_누누_구간_id_1
        );

        assertThatCode(() -> new InterStations(given))
                .isInstanceOf(InterStationsException.class)
                .hasMessage("구간이 중복되었습니다.");
    }

    @Test
    void 구간은_연결되어_있어야_한다() {
        List<InterStation> given = List.of(
                코다에서_누누_구간_id_1,
                두둠에서_처음보는_역_id_3
        );

        assertThatCode(() -> new InterStations(given))
                .isInstanceOf(InterStationsException.class)
                .hasMessage("구간이 연결되어있지 않습니다.");
    }

    @Nested
    @DisplayName("구간 목록을")
    class Context_getStations {

        @Test
        void 상행선부터_하행선까지_순서대로_가져올_수_있다() {
            List<InterStation> given = List.of(코다에서_누누_구간_id_1, 누누에서_두둠_구간_id_2);
            InterStations interStations = new InterStations(given);

            assertThat(interStations.getAllStations()).containsExactly(
                    코다_역_id_1.getId(),
                    누누_역_id_2.getId(),
                    두둠_역_id_3.getId());
        }
    }

    @Nested
    @DisplayName("구간을 추가하면")
    class Context_add {

        @Test
        void 정상적으로_추가된다() {
            List<InterStation> given = List.of(코다에서_누누_구간_id_1);
            InterStations interStations = new InterStations(given);

            assertThatCode(() -> interStations.add(2L, null, 3L, 10))
                    .doesNotThrowAnyException();
        }

        @Test
        void 구간이_중복되면_예외가_발생한다() {
            List<InterStation> given = List.of(코다에서_누누_구간_id_1);
            InterStations interStations = new InterStations(given);

            assertThatCode(() -> interStations.add(1L, 2L, 2L, 10L))
                    .isInstanceOf(InterStationsException.class)
                    .hasMessage("역을 추가할 수 없습니다");
        }

        @Test
        void 구간이_연결되어있지_않으면_예외가_발생한다() {
            List<InterStation> given = List.of(코다에서_누누_구간_id_1);
            InterStations interStations = new InterStations(given);

            assertThatCode(() -> interStations.add(3L, null, 4L, 10L))
                    .isInstanceOf(InterStationsException.class)
                    .hasMessage("역을 추가할 수 없습니다");
        }

        @Test
        void 구간은_정렬된_상태가_유지된다() {
            List<InterStation> given = List.of(코다에서_누누_구간_id_1);
            InterStations interStations = new InterStations(given);

            interStations.add(2L, null, 3L, 10L);

            assertThat(interStations.getInterStations()).containsExactly(
                    코다에서_누누_구간_id_1,
                    new InterStation(null, 2L, 3L, 10L)
            );
        }

        @Test
        void 중간에_추가되어도_정렬된_상태가_유지된다() {
            List<InterStation> given = List.of(
                    코다에서_누누_구간_id_1,
                    누누에서_두둠_구간_id_2
            );
            InterStations interStations = new InterStations(given);
            List<InterStation> expected = List.of(
                    코다에서_누누_구간_id_1,
                    new InterStation(누누_역_id_2.getId(), 처음보는_역_id_4.getId(), 1L),
                    new InterStation(처음보는_역_id_4.getId(), 두둠_역_id_3.getId(), 9L)
            );

            interStations.add(누누_역_id_2.getId(), 3L, 4L, 1L);

            assertThat(interStations.getInterStations()).usingRecursiveComparison()
                    .isEqualTo(expected);
        }
    }

    @Nested
    @DisplayName("구간을 삭제하면")
    class Context_remove {

        @Test
        void 처음을_제거해도_정상적으로_제거된다() {
            List<InterStation> given = List.of(
                    코다에서_누누_구간_id_1,
                    누누에서_두둠_구간_id_2
            );
            InterStations interStations = new InterStations(given);

            interStations.remove(코다_역_id_1.getId());

            assertThat(interStations.getInterStations()).containsExactly(누누에서_두둠_구간_id_2);
        }

        @Test
        void 마지막을_제거해도_정상적으로_제거된다() {
            List<InterStation> given = List.of(
                    코다에서_누누_구간_id_1,
                    누누에서_두둠_구간_id_2
            );
            InterStations interStations = new InterStations(given);

            interStations.remove(두둠_역_id_3.getId());

            assertThat(interStations.getInterStations()).containsExactly(코다에서_누누_구간_id_1);
        }

        @Test
        void 중간을_제거해도_정상적으로_제거된다() {
            List<InterStation> given = List.of(
                    코다에서_누누_구간_id_1,
                    누누에서_두둠_구간_id_2
            );
            InterStations interStations = new InterStations(given);

            interStations.remove(누누_역_id_2.getId());

            assertThat(interStations.getInterStations()).usingRecursiveComparison()
                    .isEqualTo(List.of(new InterStation(null, 코다_역_id_1.getId(), 두둠_역_id_3.getId(), 20)));
        }

        @Test
        void 역이_없으면_예외가_발생한다() {
            List<InterStation> given = List.of(
                    코다에서_누누_구간_id_1,
                    누누에서_두둠_구간_id_2
            );
            InterStations interStations = new InterStations(given);

            assertThatCode(() -> interStations.remove(처음보는_역_id_4.getId()))
                    .isInstanceOf(InterStationsException.class)
                    .hasMessage("역을 제거할 수 없습니다");
        }
    }

    @Nested
    @DisplayName("비어있는지 여부를")
    class Context_empty {

        @Test
        void 확인할_수_있다() {
            List<InterStation> given = List.of(코다에서_누누_구간_id_1);
            InterStations interStations = new InterStations(given);

            assertThat(interStations.isEmpty()).isFalse();
        }
    }
}
