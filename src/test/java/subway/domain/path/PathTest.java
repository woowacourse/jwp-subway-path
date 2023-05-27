package subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import subway.domain.Distance;
import subway.domain.Station;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    @Test
    void 경로에_포함된_역의_이름을_리스트로_반환한다() {
        // given
        Path path = new Path(List.of(new Station("A"), new Station("B")), new Distance(5));

        // when
        List<String> allStationName = path.getAllStationName();

        // then
        assertThat(allStationName).containsExactly("A", "B");
    }

}
