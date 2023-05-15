package subway.ui.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import subway.domain.Section;

public class SectionResponse {
	private final Long id;
	private final long lindId;
	private final String upStationName;
	private final String downStationName;
	private final long distance;

	public SectionResponse(final Long id, final long lindId, final String upStationName, final String downStationName,
		final long distance) {
		this.id = id;
		this.lindId = lindId;
		this.upStationName = upStationName;
		this.downStationName = downStationName;
		this.distance = distance;
	}

	public static SectionResponse of(final long lindId, final String upStationName, final String downStationName, final long distance){
		return new SectionResponse(null, lindId, upStationName, downStationName, distance);
	}

	public static List<SectionResponse> of(List<Section> sections) {
		return sections.stream()
			.map(section -> new SectionResponse(
				section.getId(),
				section.getLine().getId(),
				section.getUpStation().getName(),
				section.getDownStation().getName(),
				section.getDistance()))
			.collect(Collectors.toList());
	}

	public Long getId() {
		return id;
	}

	public long getLindId() {
		return lindId;
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
