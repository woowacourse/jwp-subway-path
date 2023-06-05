package subway.interstation.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.interstation.domain.InterStationFixture.코다에서_누누_구간_id_1;
import static subway.station.domain.StationFixture.누누_역_id_2;
import static subway.station.domain.StationFixture.두둠_역_id_3;
import static subway.station.domain.StationFixture.코다_역_id_1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.interstation.domain.exception.InterStationException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("구간은")
class InterStationTest {

    @Test
    void 정상적으로_생성된다() {
        assertThatCode(() -> new InterStation(코다_역_id_1.getId(), 누누_역_id_2.getId(), 1L))
                .doesNotThrowAnyException();
    }

    @Test
    void 출발역과_도착역이_같으면_예외가_발생한다() {
        assertThatCode(() -> new InterStation(코다_역_id_1.getId(), 코다_역_id_1.getId(), 1L))
                .isInstanceOf(InterStationException.class)
                .hasMessage("상행역과 하행역이 같습니다.");
    }

    @Test
    void 거리가_음수이면_예외가_발생한다() {
        assertThatCode(() -> new InterStation(코다_역_id_1.getId(), 누누_역_id_2.getId(), -1L))
                .hasMessage("거리는 양수이어야 합니다.");
    }

    @Test
    void id가_같으면_같은_객체이다() {
        InterStation interStation1 = new InterStation(1L, 코다_역_id_1.getId(), 누누_역_id_2.getId(), 1L);

        assertThat(interStation1).isEqualTo(코다에서_누누_구간_id_1);
    }

    @Test
    void id가_다르면_다른_객체이다() {
        assertThat(new InterStation(2L, 코다_역_id_1.getId(), 누누_역_id_2.getId(), 1L)).isNotEqualTo(코다에서_누누_구간_id_1);
    }

    @Nested
    @DisplayName("contains 메서드는")
    class Context_contains {

        @Test
        void 구간에_역이_포함되어_있으면_true_를_반환한다() {
            assertSoftly(
                    softly -> {
                        softly.assertThat(코다에서_누누_구간_id_1.contains(코다_역_id_1.getId())).isTrue();
                        softly.assertThat(코다에서_누누_구간_id_1.contains(누누_역_id_2.getId())).isTrue();
                    }
            );
        }

        @Test
        void 구간에_역이_포함되어_있지_않으면_false_를_반환한다() {
            assertSoftly(
                    softly -> softly.assertThat(코다에서_누누_구간_id_1.contains(두둠_역_id_3.getId())).isFalse()

            );
        }
    }
}
