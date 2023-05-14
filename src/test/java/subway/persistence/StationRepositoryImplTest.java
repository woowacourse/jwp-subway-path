package subway.persistence;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import subway.domain.Station;

@SpringBootTest
class StationRepositoryImplTest {

	@Autowired
	StationRepositoryImpl repository;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@DisplayName("역 생성 테스트")
	@Test
	void createStation() {
		// given
		final Station jamsil = new Station("잠실");

		// when
		final long jamsilId = repository.createStation(jamsil);

		// then
		Assertions.assertThat(1L).isEqualTo(jamsilId);
	}

	@Test
	void findAll() {
	}

	@Test
	void findById() {
	}

	@Test
	void updateStation() {
	}

	@Test
	void deleteById() {
	}
}
