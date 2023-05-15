package subway.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import subway.domain.Line;

@SpringBootTest
class LineJdbcRepositoryTest {

	@Autowired
	LineJdbcRepository repository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@DisplayName("노선 생성 서비스 테스트")
	@Test
	void createLine() {
		// given
		final Line line = new Line("1호선");

		// when
		final long lineId = repository.createLine(line);

		// then
		Assertions.assertThat(1L).isEqualTo(lineId);
	}

	@DisplayName("노선 전체 조회 서비스 테스트")
	@Test
	void findAll() {
		// given
		final Line line1 = new Line("1호선");
		final Line line2 = new Line("2호선");

		// when
		repository.createLine(line1);
		repository.createLine(line2);
		final List<Line> lines = repository.findAll();

		// then
		Assertions.assertThat(2).isEqualTo(lines.size());
	}

	@Test
	void findById() {
	}

	@Test
	void updateLine() {
	}

	@Test
	void deleteById() {
	}
}
