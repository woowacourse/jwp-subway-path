package subway.domain.subway;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.LinesFixture.createLines;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.exception.LinesEmptyException;

class PathTest {

	@Test
	@DisplayName("최단 경로 조회 테스트")
	void findShortestPath() {
		// given
		Path path = Path.from(createLines());

		// when
		List<Station> result = path.findShortestPath("잠실역", "선릉역");

		// then
		assertAll(
			() -> assertThat(result.size()).isEqualTo(3),
			() -> assertThat(result.contains(new Station("잠실역"))).isTrue()
		);
	}

	@DisplayName("최단 경로 조회 시 지하철이 비어있으면 예외가 발생한다")
	@Test
	void exception_whenFindShortestPathWithLinesEmpty() {
		// given
		Path pathOfNull = Path.createDefault();

		// then
		assertThatThrownBy(() -> pathOfNull.findShortestPath("교대역", "잠실역"))
			.isInstanceOf(LinesEmptyException.class);
	}
}
