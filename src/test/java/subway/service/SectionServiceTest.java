package subway.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.given;
import static subway.fixture.SectionsFixture.createSections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.subway.Line;
import subway.domain.subway.Sections;
import subway.dto.request.SectionCreateRequest;
import subway.dto.request.SectionDeleteRequest;
import subway.exception.InvalidSectionDistanceException;
import subway.exception.SameSectionException;
import subway.exception.StationNotConnectedException;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;

@ExtendWith(MockitoExtension.class)
public class SectionServiceTest {

	@InjectMocks
	private SectionService sectionService;

	@Mock
	private SectionRepository sectionRepository;

	@Mock
	private LineRepository lineRepository;

	@Test
	@DisplayName("구간 생성 서비스 테스트")
	void createSection() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "선릉역", "강남역", 3);
		Sections sections = createSections();
		Line line = new Line(sections, "2호선");

		given(sectionRepository.findSectionsByLineName(request.getLineName())).willReturn(sections);
		given(lineRepository.findLineWithSections(request.getLineName(), sections)).willReturn(line);

		// when
		sectionService.insertSection(request);

		// then
		verify(lineRepository).insertSectionInLine(sections, line.getName());
	}

	@Test
	@DisplayName("구간이 중복되면 예외가 발생한다")
	void exception_whenSectionDuplicate() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "잠실역", "잠실새내역", 3);
		Sections sections = createSections();
		Line line = new Line(sections,  "2호선");

		given(sectionRepository.findSectionsByLineName(request.getLineName())).willReturn(sections);
		given(lineRepository.findLineWithSections(request.getLineName(), sections)).willReturn(line);

		// then
		assertThatThrownBy(() -> sectionService.insertSection(request))
			.isInstanceOf(SameSectionException.class);
	}

	@Test
	@DisplayName("구간 생성 시 겹치는 역이 없으면 예외가 발생한다")
	void exception_whenNoConnectedStation
		() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "신사역", "고속터미널역", 3);
		Sections sections = createSections();
		Line line = new Line(sections, "2호선");

		given(sectionRepository.findSectionsByLineName(request.getLineName())).willReturn(sections);
		given(lineRepository.findLineWithSections(request.getLineName(), sections)).willReturn(line);

		// then
		assertThatThrownBy(() -> sectionService.insertSection(request))
			.isInstanceOf(StationNotConnectedException.class);
	}

	@Test
	@DisplayName("요청 구간이 기존 구간의 거리보다 길면 예외가 발생한다")
	void exception_whenInvalidDistance() {
		// given
		SectionCreateRequest request = new SectionCreateRequest("2호선", "잠실역", "교대역", 10);
		Sections sections = createSections();
		Line line = new Line(sections, "2호선");

		given(sectionRepository.findSectionsByLineName(request.getLineName())).willReturn(sections);
		given(lineRepository.findLineWithSections(request.getLineName(), sections)).willReturn(line);

		// then
		assertThatThrownBy(() -> sectionService.insertSection(request))
			.isInstanceOf(InvalidSectionDistanceException.class);
	}

	@Test
	@DisplayName("구간 삭제 서비스 테스트")
	void delete_section_success() {
		// given
		SectionDeleteRequest request = new SectionDeleteRequest("2호선", "선릉역");
		Sections sections = createSections();
		given(sectionRepository.findSectionsByLineName(request.getLineName())).willReturn(sections);

		// when
		sectionService.deleteSection(request);

		// then
		verify(lineRepository).insertSectionInLine(sections, request.getLineName());
	}
}
