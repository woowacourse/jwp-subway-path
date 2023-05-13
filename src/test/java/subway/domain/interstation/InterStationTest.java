package subway.domain.interstation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static subway.domain.station.StationFixture.누누_역_id_2;
import static subway.domain.station.StationFixture.두둠_역_id_3;
import static subway.domain.station.StationFixture.코다_역_id_1;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.exception.interstation.InterStationException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("구간은")
class InterStationTest {

    @Test
    void 정상적으로_생성된다() {
        assertThatCode(() -> new InterStation(코다_역_id_1, 누누_역_id_2, 1L))
            .doesNotThrowAnyException();
    }

    @Test
    void 출발역과_도착역이_같으면_예외가_발생한다() {
        assertThatCode(() -> new InterStation(코다_역_id_1, 코다_역_id_1, 1L))
            .isInstanceOf(InterStationException.class)
            .hasMessage("상행역과 하행역이 같습니다.");
    }

    @Test
    void 거리가_음수이면_예외가_발생한다() {
        assertThatCode(() -> new InterStation(코다_역_id_1, 누누_역_id_2, -1L))
            .hasMessage("거리는 양수이어야 합니다.");
    }

    @Test
    void id가_같으면_같은_객체이다() {
        final InterStation interStation1 = new InterStation(1L, 코다_역_id_1, 누누_역_id_2, 1L);
        final InterStation interStation2 = new InterStation(1L, 코다_역_id_1, 두둠_역_id_3, 1L);

        assertThat(interStation1).isEqualTo(interStation2);
    }
}
