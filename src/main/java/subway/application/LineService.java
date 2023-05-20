package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.LineDao;
import subway.domain.core.Line;
import subway.dto.request.LineCreateRequest;
import subway.dto.response.LineResponse;

@Service
@Transactional
public class LineService {
	private final LineDao lineDao;

	public LineService(final LineDao lineDao) {
		this.lineDao = lineDao;
	}

	public LineResponse createLine(final LineCreateRequest lineCreateRequest) {
		final List<LineResponse> lines = findAll();
		for (LineResponse line : lines) {
			checkLineExist(lineCreateRequest, line);
			lineCreateRequest.validateName();
		}
		final Line line = new Line(lineCreateRequest.getName());
		final long lineId = lineDao.createLine(line);

		return new LineResponse(lineId, lineCreateRequest.getName());
	}

	private void checkLineExist(final LineCreateRequest lineCreateRequest, final LineResponse line) {
		if (line.getName().equals(lineCreateRequest.getName())) {
			throw new IllegalArgumentException("이미 존재하는 노선입니다");
		}
	}

	public List<LineResponse> findAll() {
		return LineResponse.of(lineDao.findAll());
	}

	public LineResponse findById(final long lineId) {
		final Line line = lineDao.findById(lineId);
		return new LineResponse(lineId, line.getName());
	}

	public LineResponse updateLine(final long lineId, final LineCreateRequest request) {
		final boolean isUpdated = lineDao.updateLine(lineId, new Line(request.getName()));

		if (!isUpdated) {
			throw new IllegalStateException("노선 갱신에 실패했습니다");
		}

		return new LineResponse(lineId, request.getName());
	}

	public long deleteLine(final long lineId) {
		final boolean isDeleted = lineDao.deleteById(lineId);

		if (!isDeleted) {
			throw new NullPointerException( "존재하지 않는 노선입니다");
		}
		return lineId;
	}
}
