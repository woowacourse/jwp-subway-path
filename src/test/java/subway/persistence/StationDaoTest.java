package subway.persistence;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.StationDao;
import subway.domain.core.Station;

@SpringBootTest
@Transactional
class StationDaoTest {

	@Autowired
	StationDao repository;

	Station kyodae;
	Station samsung;
	long kyodaeId;

	@BeforeEach
	void setUp() {
		kyodae = new Station("교대");
		samsung = new Station("삼성");
		kyodaeId = repository.createStation(kyodae);
	}

	@DisplayName("역 생성 테스트")
	@Test
	void createStation() {
		// when
		final Station foundStation = repository.findById(kyodaeId);

		// then
		Assertions.assertThat(foundStation.getName()).isEqualTo("교대");
	}

	@DisplayName("역 전체 조회 테스트")
	@Test
	void findAll() {
		// when
		repository.createStation(samsung);
		final List<Station> stations = repository.findAll();

		// then
		Assertions.assertThat(2).isEqualTo(stations.size());
	}

	@DisplayName("ID로 역 조회 테스트")
	@Test
	void findById() {
		// when
		final Station station = repository.findById(kyodaeId);

		// then
		Assertions.assertThat(kyodae).isEqualTo(station);
	}

	@DisplayName("역 수정 테스트")
	@Test
	void updateStation() {
		// when
		repository.updateStation(kyodaeId, samsung);

		final Station foundStation = repository.findById(kyodaeId);

		// then
		Assertions.assertThat(foundStation).isEqualTo(samsung);
	}

	@Test
	void deleteById() {
		// when
		final boolean isDelete = repository.deleteById(kyodaeId);

		//then
		Assertions.assertThat(isDelete).isTrue();
	}
}
