package subway.domain.subway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.fixture.LinesFixture.createLines;

public class LinesTest {

	@Test
	@DisplayName("역의 이름과 매칭되는 문자열 map 을 생성한다")
	void getStationByNameKey() {
		// given
		Lines lines = createLines();

		// when
		Map<String, Station> result = lines.getStationByNameKey();

		// then
		assertThat(result.keySet().size()).isEqualTo(3);
	}

	@Test
	@DisplayName("유효한 노선의 이름 집합을 생성한다")
	void getLineNameByStation() {
		// given
		Station station = new Station("잠실역");
		Lines lines = createLines();

		// when
		Set<String> result = lines.getAllLineNames(station);

		// then
		assertThat(result.size()).isEqualTo(1);
	}
}
