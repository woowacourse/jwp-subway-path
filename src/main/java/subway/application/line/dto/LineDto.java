package subway.application.line.dto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import subway.application.section.dto.SectionDto;
import subway.domain.line.Line;

public class LineDto {

	private Long id;
	private String name;
	private String color;
	private List<SectionDto> sectionDtos;

	public LineDto(final String name, final String color) {
		this(null, name, color, Collections.emptyList());
	}

	public LineDto(final Long id, final String name, final String color, final List<SectionDto> sectionDtos) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.sectionDtos = sectionDtos;
	}

	public static LineDto from(final Line line) {
		final List<SectionDto> sectionDtos = line.getSections().stream()
			.map(SectionDto::from)
			.collect(Collectors.toList());
		return new LineDto(line.getId(), line.getName(), line.getColor(), sectionDtos);
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

	public List<SectionDto> getSectionDtos() {
		return sectionDtos;
	}
}
