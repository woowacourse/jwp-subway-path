package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;
import subway.exception.InvalidInputException;
import subway.repository.LineRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Optional<Long> findLine = lineRepository.findByName(request.getName());

        if(findLine.isPresent()){
            throw new InvalidInputException("이미 존재하는 노선입니다.");
        }

        Line line = new Line(request.getName(), request.getColor(), Collections.emptyList());
        LineEntity lineEntity = lineRepository.saveLine(line);
        return LineResponse.of(lineEntity);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLineEntities = lineRepository.findOnlyLines();

        lineRepository.findOnlyLines();
        return persistLineEntities.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity persistLineEntity = lineRepository.findOnlyLineBy(id);
        return LineResponse.of(persistLineEntity);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineRepository.updateLine(new Line(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor(), Collections.emptyList()));
    }

    public void deleteLineById(Long id) {
        lineRepository.removeSectionsByLineId(id);
        lineRepository.removeLineById(id);
    }
}
