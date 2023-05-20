package subway.application;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Section;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.ui.dto.request.SectionUpdateRequest;
import subway.ui.dto.response.SectionResponse;

@SpringBootTest
@Transactional
@Sql("/test-data.sql")
class SectionServiceTest {

	@Autowired
	SectionService sectionService;
	@Autowired
	SectionDao sectionRepository;

	@Autowired
	StationDao stationRepository;

	String lineName;
	String jamsil;
	String yeoksam;

	@BeforeEach
	void setUp() {
		lineName = "2호선";
		jamsil = "잠실";
		yeoksam = "역삼";
	}

	@DisplayName("구간 생성 서비스 테스트")
	@Test
	void createSection() {
		// when
		final SectionUpdateRequest request = new SectionUpdateRequest(lineName, jamsil, yeoksam, 10L);
		final SectionResponse response = sectionService.createSection(request);

		// then
		Assertions.assertThat(response)
			.hasFieldOrPropertyWithValue("upStationName", jamsil)
			.hasFieldOrPropertyWithValue("downStationName", yeoksam)
			.hasFieldOrPropertyWithValue("distance", 10L);
	}

	@DisplayName("전체 구간 조회 서비스 테스트")
	@Test
	void findAll() {
		// when
		final SectionUpdateRequest request = new SectionUpdateRequest(lineName, jamsil, yeoksam, 10L);
		final SectionResponse response = sectionService.createSection(request);
		final List<Section> sections = sectionRepository.findAll();

		// then
		Assertions.assertThat(1).isEqualTo(sections.size());
	}
}
