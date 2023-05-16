package subway.application;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import subway.persistence.SectionJdbcRepository;
import subway.persistence.StationJdbcRepository;
import subway.ui.dto.request.SectionRequest;
import subway.ui.dto.response.SectionResponse;

@SpringBootTest
@Sql("/test-data.sql")
class SectionServiceTest {

	@Autowired
	SectionService sectionService;
	@Autowired
	SectionJdbcRepository sectionRepository;
	@Autowired
	StationJdbcRepository stationRepository;

	@DisplayName("구간 생성 서비스 테스트")
	@Test
	void createSection() {
		// given
		final String lineName = "2호선";
		final String jamsil = "잠실";
		final String yeoksam = "역삼";

		// when
		final SectionRequest request = new SectionRequest(lineName, jamsil, yeoksam, 10L);
		final SectionResponse response = sectionService.createSection(request);

		// then
		Assertions.assertThat(response)
			.hasFieldOrPropertyWithValue("upStationName", jamsil)
			.hasFieldOrPropertyWithValue("downStationName", yeoksam)
			.hasFieldOrPropertyWithValue("distance", 10L);
	}
}
