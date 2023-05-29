package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import subway.exception.SubwayIllegalArgumentException;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 상행역과_하행역이_같으면_예외() {
        assertThatThrownBy(() -> new Section(삼성역.STATION, 삼성역.STATION, 3))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("상행역과 하행역은 달라야합니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 거리가_0보다_작거나_같으면_예외(int distance) {
        assertThatThrownBy(() -> new Section(삼성역.STATION, 잠실역.STATION, distance))
                .isInstanceOf(SubwayIllegalArgumentException.class)
                .hasMessage("거리는 0보다 커야합니다.");
    }

    @Test
    void 구간을_생성한다() {
        assertDoesNotThrow(() -> new Section(삼성역.STATION, 잠실역.STATION, 1));
    }
}
