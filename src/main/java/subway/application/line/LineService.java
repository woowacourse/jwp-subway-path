package subway.application.line;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.dao.line.LineDao;
import subway.domain.LineInfo;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@Service
public class LineService {

	private final LineDao lineDao;

	public LineService(final LineDao lineDao) {
		this.lineDao = lineDao;
	}

	public LineResponse saveLine(LineRequest request) {
		LineInfo persistLine = lineDao.insert(new LineInfo(request.getName(), request.getColor()));
		return LineResponse.of(persistLine);
	}

	public List<LineInfo> findLines() {
		return lineDao.findAll();
	}

	public LineInfo findLineById(Long id) {
		return lineDao.findById(id);
	}

	public void updateLine(Long id, LineRequest lineUpdateRequest) {
		lineDao.update(new LineInfo(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
	}

	public void deleteLineById(Long id) {
		lineDao.deleteById(id);
	}

}
