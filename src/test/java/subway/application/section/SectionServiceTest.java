package subway.application.section;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import subway.ui.dto.AddStationRequest;
import subway.ui.dto.AddStationResponse;

@Transactional
@SpringBootTest
@Sql(value = {"classpath:tearDown.sql", "classpath:setTest.sql"})
class SectionServiceTest {

	@Autowired
	private SectionService sectionService;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Nested
	class AddStationByLineId {

		@Test
		void 처음_두_역을_입력하면_이상없이_등록된다() {

			//given
			final long lineId = 1L;
			final AddStationRequest addStationRequest = new AddStationRequest("잠실역", "잠실새내역", 1);

			//when
			final List<AddStationResponse> addStationResponses = sectionService.addStationByLineId(1L,
				addStationRequest);

			//then
			final AddStationResponse addStationResponse = addStationResponses.get(0);
			assertThat(addStationResponses).hasSize(1);
			assertThat(addStationResponse.getSectionId()).isEqualTo(1L);
			assertThat(addStationResponse.getDepartureStation()).isEqualTo(addStationRequest.getDepartureStation());
			assertThat(addStationResponse.getArrivalStation()).isEqualTo(addStationRequest.getArrivalStation());
			assertThat(addStationResponse.getDistance()).isEqualTo(addStationRequest.getDistance());
		}

		@Test
		void 순환_노선을_입력하면_예외가_발생한다() {

			//given
			final long lineId = 1L;
			final AddStationRequest addStationRequest1 = new AddStationRequest("잠실역", "잠실새내역", 1);
			final AddStationRequest addStationRequest2 = new AddStationRequest("잠실새내역", "종합운동장역", 1);
			final AddStationRequest addStationRequest3 = new AddStationRequest("종합운동장역", "잠실역", 1);

			sectionService.addStationByLineId(1L, addStationRequest1);
			sectionService.addStationByLineId(1L, addStationRequest2);

			assertThatThrownBy(() -> sectionService.addStationByLineId(1L, addStationRequest3))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("순환 노선입니다");
		}

	}

	@Test
	void findSectionsTest() {
	}

	@Test
	void findSectionsByIdTest() {
	}

	@Test
	void deleteSectionByLineIdAndSectionIdTest() {
	}
}
