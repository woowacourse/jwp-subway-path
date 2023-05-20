package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.LinesFixture.createLines;

public class LinesTest {

    @Test
    @DisplayName("역의 이름으로 역 도메인을 관리하는 객체를 반환한다.")
    void returns_stations_from_name_map() {
        // given
        Lines lines = createLines();

        // when
        Map<String, Station> result = lines.getStationsByName();

        // then
        assertThat(result.keySet().size()).isEqualTo(3);
    }

    @Test
    @DisplayName("역이 포함된 노선들의 이름을 반환해준다.")
    void returns_lines_name_from_station() {
        // given
        Station station = new Station("잠실역");
        Lines lines = createLines();

        // when
        Set<String> result = lines.getLineNamesByStation(station);

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}
