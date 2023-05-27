package subway.ui.line.dto;

import subway.application.line.dto.LineDto;

public class LineResponse {
	private Long id;
	private String name;
	private String color;

	public LineResponse(final Long id, final String name, final String color) {
		this.id = id;
		this.name = name;
		this.color = color;
	}

	public static LineResponse from(final LineDto line) {
		return new LineResponse(line.getId(), line.getName(), line.getColor());
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color;
	}
}
