package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LinesResponse;
import subway.entity.LineEntity;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

@Service
public class LineService {

	private final LineRepository lineRepository;

	public LineService(final LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	@Transactional
	public long saveLine(final LineCreateRequest request) {
		return lineRepository.insertLine(new LineEntity(null, request.getName()));
	}

	@Transactional(readOnly = true)
	public LinesResponse findAll() {
		return LinesResponse.from(lineRepository.findAll());
	}

	@Transactional
	public void updateLineById(final Long lineId, final LineUpdateRequest lineUpdateRequest) {
		LineEntity lineEntity = lineRepository.findById(lineId)
			.orElseThrow(LineNotFoundException::new);

		lineEntity.update(lineUpdateRequest.getName());
		lineRepository.updateLine(lineId, lineEntity);
	}
}
