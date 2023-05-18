package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.ui.dto.request.LineCreateRequest;
import subway.ui.dto.response.LineResponse;

@Service
public class LineService {
	private final LineRepository lineRepository;

	public LineService(final LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	public LineResponse createLine(final LineCreateRequest lineCreateRequest) {
		final List<LineResponse> lines = findAll();
		for (LineResponse line : lines) {
			checkLineExist(lineCreateRequest, line);
		}
		final Line line = new Line(lineCreateRequest.getName());
		final long lineId = lineRepository.createLine(line);

		return new LineResponse(lineId, lineCreateRequest.getName());
	}

	private void checkLineExist(final LineCreateRequest lineCreateRequest, final LineResponse line) {
		if (line.getName().equals(lineCreateRequest.getName())) {
			throw new IllegalArgumentException("이미 존재하는 노선입니다");
		}
	}

	public List<LineResponse> findAll() {
		return LineResponse.of(lineRepository.findAll());
	}

	public LineResponse findById(final long lineId) {
		final Line line = lineRepository.findById(lineId);
		return new LineResponse(lineId, line.getName());
	}

	public LineResponse updateLine(final long lineId, final LineCreateRequest request) {
		final boolean isUpdated = lineRepository.updateLine(lineId, new Line(request.getName()));

		if (!isUpdated) {
			throw new IllegalStateException("노선 갱신에 실패했습니다");
		}

		return new LineResponse(lineId, request.getName());
	}

	public long deleteLine(final long lineId) {
		final boolean isDeleted = lineRepository.deleteById(lineId);

		if (!isDeleted) {
			throw new NullPointerException("노선 삭제에 실패했습니다");
		}
		return lineId;
	}
}
