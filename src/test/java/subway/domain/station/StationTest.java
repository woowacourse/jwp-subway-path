package subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.station.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
public class StationTest {

    @Test
    void 역을_생성한다() {
        String name = "역삼역";

        assertDoesNotThrow(() -> new Station(name));
    }

    @Test
    void 이름은_50자를_넘길_수_없다() {
        String name = "a".repeat(51);

        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("이름은 50자 이하여야합니다.");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void 이름은_공백일_수_없다(String name) {
        assertThatThrownBy(() -> new Station(name))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("이름은 비어있을 수 없습니다.");
    }

    @Test
    void 이름에_공백이_포함될_경우_공백을_제거한다() {
        String name = "   a ";

        Station station = new Station(name);

        assertThat(station.getName()).isEqualTo("a");
    }
}
