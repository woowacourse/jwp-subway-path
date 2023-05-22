package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dto.request.LineCreateRequest;
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
	public long saveLine(final LineCreateRequest request) {
		return lineRepository.insertLine(new LineEntity(null, request.getName()));
	}

	@Transactional(readOnly = true)
	public LinesResponse findAll() {
		return LinesResponse.from(lineRepository.findAll());
	}

}
