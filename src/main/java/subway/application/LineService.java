package subway.application;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.dao.LineDao;
import subway.domain.LineInfo;
import subway.dto.LineRequest;
import subway.dto.LineResponse;

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

	public List<LineResponse> findLineResponses() {
		List<LineInfo> persistLines = findLines();
		return persistLines.stream()
			.map(LineResponse::of)
			.collect(toList());
	}

	public List<LineInfo> findLines() {
		return lineDao.findAll();
	}

	public LineResponse findLineResponseById(Long id) {
		LineInfo persistLine = findLineById(id);
		return LineResponse.of(persistLine);
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
