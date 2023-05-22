package subway.dto.response;

import subway.entity.LineEntity;

public class LineResponse {

	private final Long lineId;
	private final String name;

	private LineResponse(final Long lineId, final String name) {
		this.lineId = lineId;
		this.name = name;
	}

	public static LineResponse from(final LineEntity lineEntity) {
		return new LineResponse(
			lineEntity.getLineId(),
			lineEntity.getName()
		);
	}

	public Long getLineId() {
		return lineId;
	}

	public String getName() {
		return name;
	}
}
