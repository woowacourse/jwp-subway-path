package subway.dto.response;

import subway.domain.core.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineResponse {
	private long id;
	private String name;

	public LineResponse() {
	}

	public LineResponse(final long id, final String name) {
		this.id = id;
		this.name = name;
	}

	public static List<LineResponse> of(final List<Line> lines) {
		return lines.stream()
			.map(line -> new LineResponse(line.getId(), line.getName()))
			.collect(Collectors.toList());
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
