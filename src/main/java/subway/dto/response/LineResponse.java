package subway.dto.response;

import subway.entity.LineEntity;

public class LineResponse {

	private final long lineId;
	private final String name;

	private LineResponse(final long lineId, final String name) {
		this.lineId = lineId;
		this.name = name;
	}

	public static LineResponse from(final LineEntity lineEntity) {
		return new LineResponse(
			lineEntity.getLineId(),
			lineEntity.getName()
		);
	}

	public long getLineId() {
		return lineId;
	}

	public String getName() {
		return name;
	}
}
