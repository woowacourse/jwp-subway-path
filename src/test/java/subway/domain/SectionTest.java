package subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.exception.ApiIllegalArgumentException;
import subway.fixture.StationFixture.삼성역;
import subway.fixture.StationFixture.잠실역;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionTest {

    @Test
    void 상행역과_하행역이_같으면_예외() {
        assertThatThrownBy(() -> new Section(삼성역.STATION, 삼성역.STATION, 3))
                .isInstanceOf(ApiIllegalArgumentException.class)
                .hasMessage("상행역과 하행역은 달라야합니다.");
    }

    @Test
    void 구간을_생성한다() {
        assertDoesNotThrow(() -> new Section(삼성역.STATION, 잠실역.STATION, 3));
    }
}
