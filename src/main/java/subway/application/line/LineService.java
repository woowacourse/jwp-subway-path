package subway.application.line;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.application.line.dto.LineDto;
import subway.error.exception.LineNotFoundException;
import subway.persistence.dao.LineDao;
import subway.persistence.entity.LineEntity;

@Service
@Transactional
public class LineService {
	private final LineDao lineDao;

	public LineService(LineDao lineDao) {
		this.lineDao = lineDao;
	}

	public LineDto saveLine(final LineDto lineDto) {
		final LineEntity addLine = new LineEntity(lineDto.getName(), lineDto.getColor());

		LineEntity persistLine = lineDao.insert(addLine);
		return new LineDto(persistLine);
	}

	@Transactional(readOnly = true)
	public List<LineDto> findLines() {
		return lineDao.findAll().stream()
			.map(LineDto::new)
			.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public LineDto findLineById(final Long id) {
		final LineEntity lineEntity = lineDao.findById(id)
			.orElseThrow(() -> LineNotFoundException.EXCEPTION);
		return new LineDto(lineEntity);
	}

	public void updateLine(final Long id, final LineDto lineDto) {
		final LineEntity newLine = new LineEntity(id, lineDto.getName(), lineDto.getColor());
		lineDao.update(newLine);
	}

	public void deleteLineById(Long id) {
		lineDao.deleteById(id);
	}

}
