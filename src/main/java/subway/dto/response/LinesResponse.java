package subway.dto.response;

import java.util.List;
import java.util.stream.Collectors;

import subway.entity.LineEntity;

public class LinesResponse {

	private final List<LineResponse> lines;

	public LinesResponse(final List<LineResponse> lines) {
		this.lines = lines;
	}

	public static LinesResponse from(final List<LineEntity> lineEntities) {
		return lineEntities.stream()
			.map(LineResponse::from)
			.collect(Collectors.collectingAndThen(Collectors.toList(), LinesResponse::new));
	}

	public List<LineResponse> getLines() {
		return lines;
	}
}
