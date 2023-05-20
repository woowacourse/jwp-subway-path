package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.core.Section;

public class SectionResponse {
	private final Long id;
	private final String lineName;
	private final String upStationName;
	private final String downStationName;
	private final long distance;

	public SectionResponse(final Long id, final String lineName, final String upStationName, final String downStationName,
		final long distance) {
		this.id = id;
		this.lineName = lineName;
		this.upStationName = upStationName;
		this.downStationName = downStationName;
		this.distance = distance;
	}

	public static SectionResponse of(final String lineName, final String upStationName, final String downStationName, final long distance){
		return new SectionResponse(null, lineName, upStationName, downStationName, distance);
	}

	public static List<SectionResponse> of(List<Section> sections) {
		return sections.stream()
			.map(section -> new SectionResponse(
				section.getId(),
				section.getLine().getName(),
				section.getUpStation().getName(),
				section.getDownStation().getName(),
				section.getDistance()))
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public String getLineName() {
		return lineName;
	}

	public String getUpStationName() {
		return upStationName;
	}

	public String getDownStationName() {
		return downStationName;
	}

	public long getDistance() {
		return distance;
	}
}
