package subway.application.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.line.dto.LineDto;
import subway.application.section.dto.SectionDto;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.line.Section;

@Service
@Transactional
public class LineService {

	private final LineRepository lineRepository;

	public LineService(final LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public LineDto saveLine(final LineDto lineDto) {
		final Line line = new Line(lineDto.getName(), lineDto.getColor());
		return LineDto.from(lineRepository.addLine(line));
	}

	@Transactional(readOnly = true)
	public List<LineDto> findLines() {
		return lineRepository.findLines()
			.stream()
			.map(this::convertToDto)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public LineDto findLineById(final Long id) {
		return LineDto.from(lineRepository.findLineById(id));
	}

	public void updateLine(final Long id, final LineDto lineDto) {
		final Line newLine = new Line(id, lineDto.getName(), lineDto.getColor());
		lineRepository.updateLine(newLine);
	}

	public void deleteLineById(Long id) {
		lineRepository.removeLine(id);
	}

	private LineDto convertToDto(final Line line) {
		final List<SectionDto> sectionDtos = convertToSectionDtos(line.getSections());
		return new LineDto(line.getId(), line.getName(), line.getColor(), sectionDtos);
	}

	private List<SectionDto> convertToSectionDtos(final List<Section> sections) {
		return sections.stream()
			.map(SectionDto::from)
			.collect(Collectors.toList());
	}

}
