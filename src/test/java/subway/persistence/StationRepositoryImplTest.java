package subway.persistence;

import java.util.List;

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

	@DisplayName("역 전체 조회 테스트")
	@Test
	void findAll() {
		// given
		final Station jamsil = new Station("잠실");
		final Station samsung = new Station("삼성");

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
		// given
		final Station jamsil = new Station("잠실");


		// when
		final long stationId = repository.createStation(jamsil);
		final Station station = repository.findById(stationId);

		// then
		Assertions.assertThat(jamsil).isEqualTo(station);
	}

	@Test
	void updateStation() {
	}

	@Test
	void deleteById() {
	}
}
