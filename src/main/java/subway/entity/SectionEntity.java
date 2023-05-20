package subway.entity;

import subway.domain.Section;

import java.util.List;
import java.util.stream.Collectors;

public class SectionEntity {
	private final long id;
	private final String line;
	private final String upStation;
	private final String downStation;
	private final Long distance;

	public SectionEntity(final long id, final String line, final String upStation, final String downStation,
		final Long distance) {
		this.id = id;
		this.line = line;
		this.upStation = upStation;
		this.downStation = downStation;
		this.distance = distance;
	}

	public static List<SectionEntity> of(final List<Section> sections) {
		return sections.stream()
			.map(section -> new SectionEntity(
				section.getId(),
				section.getLine().getName(),
				section.getUpStation().getName(),
				section.getDownStation().getName(),
				section.getDistance()
			))
			.collect(Collectors.toList());
	}

	public long getId() {
		return id;
	}

	public String getLine() {
		return line;
	}

	public String getUpStation() {
		return upStation;
	}

	public String getDownStation() {
		return downStation;
	}

	public Long getDistance() {
		return distance;
	}
}
