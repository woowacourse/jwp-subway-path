package subway.subway.domain;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.line.domain.Line;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SuppressWarnings("NonAsciiCharacters")
class SubwayTest {
    @Test
    void 지하철_객체_생성() {
        // given
        final Subway subway = new Subway();
        
        // when
        final Set<Line> lines = subway.getLines();
        
        // then
        assertThat(lines).isNotNull();
    }
}
