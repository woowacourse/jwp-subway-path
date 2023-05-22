package subway.domain.subway;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.*;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.SectionsFixture.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subway.exception.InvalidSectionDistanceException;
import subway.exception.SameSectionException;
import subway.exception.SectionNotFoundException;
import subway.exception.StationNotConnectedException;

class SectionsTest {

	@Test
	@DisplayName("구간 생성 테스트")
	void sectionCreate() {
		// given
		Sections sections = createSections();
		Section section = createSection(new Station("선릉역"), new Station("강남역"));

		// when
		sections.addSection(section);

		// then
		assertAll(
			() -> assertThat(sections.getSections().size()).isEqualTo(3),
			() -> assertThat(sections.getSections().get(2).getUpStation().getName()).isEqualTo(section.getUpStation().getName())
		);
	}

	@Test
	@DisplayName("요청 구간에 접하는 역이 없으면 예외가 밣생한다")
	void exception_whenStationNotConnected() {
		// given
		Sections sections = createSections();
		Section section = new Section(new Station("사당역"), new Station("교대역"), 10L);

		// then
		assertThatThrownBy(() -> sections.addSection(section))
			.isInstanceOf(StationNotConnectedException.class);
	}

	@Test
	@DisplayName("구간이 같으면 예외가 발생한다")
	void exception_whenSameSection() {
		// given
		Sections sections = createSections();
		Section section = new Section(new Station("잠실역"), new Station("잠실새내역"), 10L);

		// then
		assertThatThrownBy(() -> sections.addSection(section))
			.isInstanceOf(SameSectionException.class);
	}

	@Test
	@DisplayName("요청 구간 거리가 기존 구간 거리보다 길면 예외가 발생한다")
	void exception_whenInvalidDistance() {
		// given
		Sections sections = createSections();
		Section section = new Section(new Station("잠실새내역"), new Station("건대역"), 10L);

		// then
		assertThatThrownBy(() -> sections.addSection(section))
			.isInstanceOf(InvalidSectionDistanceException.class);
	}

	@Test
	@DisplayName("구간의 삭제한다")
	void deleteSection() {
		// given
		Sections sections = createSections();
		Station targetStation = new Station("잠실새내역");

		// when
		sections.deleteSectionByStation(targetStation);

		// then
		assertAll(
			() -> assertThat(sections.getSections().size()).isEqualTo(1),
			() -> assertThat(sections.getSections().get(0).getUpStation().getName()).isEqualTo("잠실역")
		);
	}

	@Test
	@DisplayName("등록되어 있지 않은 역 제거를 시도하면 예외가 발생한다")
	void exception_whenDeleteNotFoundStation() {
		// given
		Sections sections = createSections();
		Station targetStation = new Station("사당역");

		// then
		assertThatThrownBy(() -> sections.deleteSectionByStation(targetStation))
			.isInstanceOf(SectionNotFoundException.class);
	}
}
