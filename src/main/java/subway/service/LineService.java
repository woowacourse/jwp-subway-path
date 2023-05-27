package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.request.LineCreateRequest;
import subway.dto.request.LineUpdateRequest;
import subway.dto.response.LinesResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;

@Service
public class LineService {

	private final LineRepository lineRepository;

	public LineService(final LineRepository lineRepository) {
		this.lineRepository = lineRepository;
	}

	@Transactional
	public String saveLine(final LineCreateRequest request) {
		return lineRepository.insertLine(new LineEntity(null, request.getName()));
	}

	@Transactional(readOnly = true)
	public LinesResponse findAll() {
		return LinesResponse.from(lineRepository.findAll());
	}

	@Transactional
	public void updateLineByLineName(final String lineName, final LineUpdateRequest lineUpdateRequest) {
		LineEntity lineEntity = lineRepository.findLineByName(lineName);

		lineEntity.update(lineUpdateRequest.getName());
		lineRepository.updateLine(lineName, lineEntity);
	}

	@Transactional
	public void deleteLineByName(final String lineName) {
		lineRepository.deleteLineByName(lineName);
	}

	public long findIdByName(final String lineName) {
		return lineRepository.findIdByName(lineName);
	}
}
