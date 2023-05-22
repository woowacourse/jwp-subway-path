package subway.service.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import subway.domain.subway.Sections;
import subway.domain.subway.Station;
import subway.dto.request.SectionCreateRequest;
import subway.dto.request.SectionDeleteRequest;
import subway.entity.LineEntity;
import subway.exception.SameSectionException;
import subway.exception.InvalidSectionDistanceException;
import subway.exception.StationNotConnectedException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;
import subway.service.SectionService;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.fixture.SectionsFixture.createSections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql("/data.sql")
public class SectionServiceIntegrationTest {

	@Autowired
	private SectionService sectionService;

	@Autowired
	private SectionRepository sectionRepository;

	@Autowired
	private LineRepository lineRepository;

	@Autowired
	private StationRepository stationRepository;

	@Test
	@DisplayName("구간 생성 서비스 인수테스트")
	void createSection() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "종합운동장역", "삼성역", 3);

		stationRepository.insertStation(new Station("잠실역"));
		stationRepository.insertStation(new Station("잠실새내역"));
		stationRepository.insertStation(new Station("종합운동장역"));
		stationRepository.insertStation(new Station("삼성역"));

		lineRepository.insertLine(new LineEntity(1L, "2호선"));
		lineRepository.insertSectionInLine(createSections(), "2호선");

		// when
		sectionService.insertSection(request);

		// then
		Sections sections = sectionRepository.findSectionsByLineName("2호선");
		assertAll(
			() -> assertThat(sections.getSections().size()).isEqualTo(3),
			() -> assertThat(sections.getSections().get(0).getUpStation().getName()).isEqualTo("잠실역")
		);
	}

	@Test
	@DisplayName("중복된 구간을 생성할 경우 예외가 발생한다")
	void exception_whenCreateSectionDuplicate() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);

		stationRepository.insertStation(new Station("잠실역"));
		stationRepository.insertStation(new Station("잠실새내역"));
		stationRepository.insertStation(new Station("종합운동장역"));
		stationRepository.insertStation(new Station("삼성역"));

		lineRepository.insertLine(new LineEntity(1L, "2호선"));
		lineRepository.insertSectionInLine(createSections(), "2호선");

		// when & then
		assertThatThrownBy(() -> sectionService.insertSection(request))
			.isInstanceOf(SameSectionException.class);
	}

	@Test
	@DisplayName("구간 생성 시 겹치는 역이 없으면 예외가 발생한다")
	void exception_whenCreateInvalidSection() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "판교역", "정자역", 3);

		stationRepository.insertStation(new Station("잠실역"));
		stationRepository.insertStation(new Station("잠실새내역"));
		stationRepository.insertStation(new Station("종합운동장역"));
		stationRepository.insertStation(new Station("삼성역"));

		lineRepository.insertLine(new LineEntity(1L, "2호선"));
		lineRepository.insertSectionInLine(createSections(), "2호선");

		// when & then
		assertThatThrownBy(() -> sectionService.insertSection(request))
			.isInstanceOf(StationNotConnectedException.class);
	}

	@Test
	@DisplayName("요청 구간이 기존 구간 거리보다 길면 예외가 발생한다")
	void exception_whenInvalidDistance() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "잠실역", "선릉역", 10);

		stationRepository.insertStation(new Station("잠실역"));
		stationRepository.insertStation(new Station("잠실새내역"));
		stationRepository.insertStation(new Station("종합운동장역"));
		stationRepository.insertStation(new Station("삼성역"));

		lineRepository.insertLine(new LineEntity(1L, "2호선"));
		lineRepository.insertSectionInLine(createSections(), "2호선");

		// when & then
		assertThatThrownBy(() -> sectionService.insertSection(request))
			.isInstanceOf(InvalidSectionDistanceException.class);
	}

	@Test
	@DisplayName("구간 삭제 서비스 인수테스트")
	void deleteSection() {
		// given
		SectionDeleteRequest request = new SectionDeleteRequest("2호선", "종합운동장역");

		stationRepository.insertStation(new Station("잠실역"));
		stationRepository.insertStation(new Station("잠실새내역"));
		stationRepository.insertStation(new Station("종합운동장역"));

		lineRepository.insertLine(new LineEntity(1L, "2호선"));
		lineRepository.insertSectionInLine(createSections(), "2호선");

		// when
		sectionService.deleteSection(request);

		// then
		Sections sections = sectionRepository.findSectionsByLineName("2호선");
		assertAll(
			() -> assertThat(sections.getSections().size()).isEqualTo(1),
			() -> assertThat(sections.getSections().get(0).getUpStation().getName()).isEqualTo("잠실역")
		);
	}
}
