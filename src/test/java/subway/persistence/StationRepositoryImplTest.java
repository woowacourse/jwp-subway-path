package subway.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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

	Station jamsil;
	Station samsung;

	@BeforeEach
	void setUp() {
		jamsil = new Station("잠실");
		samsung = new Station("삼성");
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
		jdbcTemplate.execute("TRUNCATE TABLE station RESTART IDENTITY");
		jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
	}

	@DisplayName("역 생성 테스트")
	@Test
	void createStation() {
		// when
		final long jamsilId = repository.createStation(jamsil);

		// then
		Assertions.assertThat(1L).isEqualTo(jamsilId);
	}

	@DisplayName("역 전체 조회 테스트")
	@Test
	void findAll() {
		// when
		repository.createStation(jamsil);
		repository.createStation(samsung);
		final List<Station> stations = repository.findAll();

		// then
		Assertions.assertThat(2).isEqualTo(stations.size());
	}

	@DisplayName("ID로 역 조회 테스트")
	@Test
	void findById() {
		// when
		final long stationId = repository.createStation(jamsil);
		final Station station = repository.findById(stationId);

		// then
		Assertions.assertThat(jamsil).isEqualTo(station);
	}

	@DisplayName("역 수정 테스트")
	@Test
	void updateStation() {
		// given
		final long jamsilId = repository.createStation(jamsil);

		// when
		repository.updateStation(jamsilId, samsung);

		final Station foundStation = repository.findById(jamsilId);

		// then
		Assertions.assertThat(foundStation).isEqualTo(samsung);
	}

	@Test
	void deleteById() {
		// given
		final long jamsilId = repository.createStation(jamsil);

		// when
		final boolean isDelete = repository.deleteById(jamsilId);

		//then
		Assertions.assertThat(isDelete).isTrue();
	}
}
