package subway.application.line;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.persistence.dao.LineDao;
import subway.persistence.entity.LineEntity;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@Service
@Transactional
public class LineService {
	private final LineDao lineDao;

	public LineService(LineDao lineDao) {
		this.lineDao = lineDao;
	}

	public LineResponse saveLine(final LineRequest request) {
		LineEntity persistLine = lineDao.insert(new LineEntity(request.getName(), request.getColor()));
		return LineResponse.of(persistLine);
	}

	@Transactional(readOnly = true)
	public List<LineEntity> findLines() {
		return lineDao.findAll();
	}

	@Transactional(readOnly = true)
	public LineEntity findLineById(Long id) {
		return lineDao.findById(id);
	}

	public void updateLine(Long id, LineRequest lineUpdateRequest) {
		lineDao.update(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
	}

	public void deleteLineById(Long id) {
		lineDao.deleteById(id);
	}

}
