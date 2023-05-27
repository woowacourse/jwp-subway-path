package subway.domain.subway;

import subway.exception.SameSectionException;
import subway.exception.StationNotConnectedException;
import subway.exception.SectionNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Sections {

	private static final int END_POINT_STATION = 1;

	private final List<Section> sections;

	public Sections(final List<Section> sections) {
		this.sections = sections;
	}

	public void addSection(final Section section) {
		boolean hasUpStation = hasStation(section.getUpStation());
		boolean hasDownStation = hasStation(section.getDownStation());

		insertSection(hasUpStation, hasDownStation, section);
	}

	private boolean hasStation(final Station station) {
		return sections.stream()
			.anyMatch(section -> section.hasStation(station));
	}

	private void insertSection(final boolean hasUpStation, final boolean hasDownStation, final Section section) {
		validateSideStations(hasUpStation, hasDownStation);
		validateSameSection(hasUpStation, hasDownStation);

		if (!hasUpStation && !hasDownStation) {
			sections.add(section);
			return;
		}

		addMiddleSection(section);
	}

	private void validateSideStations(final boolean hasUpStation, final boolean hasDownStation) {
		if (!hasUpStation && !hasDownStation && !sections.isEmpty()) {
			throw new StationNotConnectedException();
		}
	}

	private void validateSameSection(final boolean hasUpStation, final boolean hasDownStation) {
		if (hasUpStation && hasDownStation) {
			throw new SameSectionException();
		}
	}

	private void addMiddleSection(final Section section) {
		Station upStation = section.getUpStation();
		Station downStation = section.getDownStation();

		Optional<Section> sectionWithUpStation = findSectionWithUpStation(upStation);
		Optional<Section> sectionWithDownStation = findSectionWithDownStation(downStation);

		if (sectionWithUpStation.isPresent()) {
			insertSectionOnUpStation(section, sectionWithUpStation.get());
			return;
		}

		if (sectionWithDownStation.isPresent()) {
			insertSectionOnDownStation(section, sectionWithDownStation.get());
			return;
		}

		sections.add(section);
	}

	private void insertSectionOnUpStation(final Section newSection, final Section sectionWithUpStation) {
		sectionWithUpStation.validateSectionDistance(newSection.getDistance());
		sections.remove(sectionWithUpStation);
		sections.add(newSection);
		sections.add(new Section(newSection.getDownStation(), sectionWithUpStation.getDownStation(),
			sectionWithUpStation.getDistance() - newSection.getDistance()));
	}

	private void insertSectionOnDownStation(final Section newSection, final Section sectionWithDownStation) {
		sectionWithDownStation.validateSectionDistance(newSection.getDistance());
		sections.remove(sectionWithDownStation);
		sections.add(newSection);
		sections.add(new Section(sectionWithDownStation.getUpStation(), newSection.getUpStation(),
			sectionWithDownStation.getDistance() - newSection.getDistance()));
	}

	private Optional<Section> findSectionWithUpStation(final Station station) {
		return sections.stream()
			.filter(nowSection -> nowSection.getUpStation().equals(station))
			.findAny();
	}

	private Optional<Section> findSectionWithDownStation(final Station station) {
		return sections.stream()
			.filter(nowSection -> nowSection.getDownStation().equals(station))
			.findAny();
	}

	public void deleteSectionByStation(final Station station) {
		if (isEndPoint(station)) {
			deleteEndPointStation(station);
			return;
		}

		deleteMiddleStation(station);
	}

	private boolean isEndPoint(final Station station) {
		final int count = (int)sections.stream()
			.filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
			.count();

		return count == END_POINT_STATION;
	}

	private void deleteEndPointStation(final Station station) {
		Section target = sections.stream()
			.filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
			.findAny()
			.orElseThrow(SectionNotFoundException::new);

		sections.remove(target);
	}

	private void deleteMiddleStation(final Station station) {
		List<Section> targetSections = getTargetSections(station);

		Section sectionWithUpStation = getSectionWithUpStation(station, targetSections);
		Section sectionWithDownStation = getSectionWithDownStation(station, targetSections);

		sections.remove(sectionWithUpStation);
		sections.remove(sectionWithDownStation);
		sections.add(new Section(sectionWithDownStation.getUpStation(), sectionWithUpStation.getDownStation(),
			sectionWithUpStation.getDistance() + sectionWithDownStation.getDistance()));
	}

	private List<Section> getTargetSections(final Station station) {
		return sections.stream()
			.filter(section -> section.getUpStation().equals(station) || section.getDownStation().equals(station))
			.collect(Collectors.toList());
	}

	private Section getSectionWithUpStation(final Station station, final List<Section> targetSections) {
		return targetSections.stream()
			.filter(section -> section.getUpStation().equals(station))
			.findAny()
			.orElseThrow(SectionNotFoundException::new);
	}

	private Section getSectionWithDownStation(final Station station, final List<Section> targetSections) {
		return targetSections.stream()
			.filter(section -> section.getDownStation().equals(station))
			.findAny()
			.orElseThrow(SectionNotFoundException::new);
	}

	public List<Section> getSections() {
		return Collections.unmodifiableList(sections);
	}
}
