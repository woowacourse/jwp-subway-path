package subway.persistence;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

	@DisplayName("노선 생성 테스트")
	@Test
	void createLine() {
		// given
		final Line line = new Line("2호선");

		// when
		final long lineId = repository.createLine(line);

		// then
		Assertions.assertThat(1L).isEqualTo(lineId);
	}

	@Test
	void deleteById() {
	}

	@Test
	void findAll() {
	}

	@Test
	void findById() {
	}

	@Test
	void updateLine() {
	}
}
