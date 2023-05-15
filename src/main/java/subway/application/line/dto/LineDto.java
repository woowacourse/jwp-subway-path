package subway.application.line.dto;

import subway.persistence.entity.LineEntity;

public class LineDto {

	private Long id;
	private String name;
	private String color;

	public LineDto(final LineEntity lineEntity) {
		this(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor());
	}

	public LineDto(final Long id, final String name, final String color) {
		this.id = id;
		this.name = name;
		this.color = color;
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
