package subway.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import subway.dao.LineDao;
import subway.domain.core.Line;

@SpringBootTest
class LineDaoTest {

	@Autowired
	LineDao repository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	Line line1;
	Line line2;

	@BeforeEach
	void setUp(){
		line1 = new Line("1호선");
		line2 = new Line("2호선");
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
		jdbcTemplate.execute("TRUNCATE TABLE line RESTART IDENTITY");
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
	}

	@DisplayName("노선 생성 테스트")
	@Test
	void createLine() {
		// when
		final long lineId = repository.createLine(line1);

		// then
		Assertions.assertThat(1L).isEqualTo(lineId);
	}

	@DisplayName("노선 전체 조회 테스트")
	@Test
	void findAll() {
		// when
		repository.createLine(line1);
		repository.createLine(line2);
		final List<Line> lines = repository.findAll();

		// then
		Assertions.assertThat(2).isEqualTo(lines.size());
	}

	@DisplayName("ID를 사용한 노선 조회 테스트")
	@Test
	void findById() {
		// given
		final long lineId = repository.createLine(line1);

		// when
		final Line foundLine = repository.findById(lineId);

		// then
		Assertions.assertThat(line1).isEqualTo(foundLine);
	}

	@DisplayName("노선 갱신 테스트")
	@Test
	void updateLine() {
		// given
		final long lineId = repository.createLine(line1);

		// when
		repository.updateLine(lineId, line2);

		final Line foundLine = repository.findById(lineId);

		// then
		Assertions.assertThat(foundLine).isEqualTo(line2);
	}

	@DisplayName("노선 삭제 테스트")
	@Test
	void deleteById() {
		//given
		final long lineId = repository.createLine(line1);

		// when
		final boolean isDelete = repository.deleteById(lineId);

		// then
		Assertions.assertThat(isDelete).isTrue();
	}
}
