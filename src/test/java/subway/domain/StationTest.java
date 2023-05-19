package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import subway.exception.EmptyNameException;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class StationTest {

    @NullAndEmptySource
    @ParameterizedTest
    void 이름에는_공백이나_null이_들어올_수_없다(String value) {
        assertThatThrownBy(() -> new Station(value))
                .isInstanceOf(EmptyNameException.class)
                .hasMessage("아름에는 빈 문자가 들어올 수 없습니다.");
    }

    @Test
    void 같은_이름의_역은_true를_반환한다() {
        //given
        Station station = new Station("강남역");
        Station other = new Station("강남역");

        //when
        boolean isSameName = station.equals(other);

        //then
        assertThat(isSameName).isTrue();
    }

    @Test
    void 다른_이름의_역은_false를_반환한다() {
        //given
        Station station = new Station("강남역");
        Station other = new Station("역삼역");

        //when
        boolean isSameName = station.equals(other);

        //then
        assertThat(isSameName).isFalse();
    }
}
