package subway.application;

import java.util.List;

import org.springframework.stereotype.Service;

import subway.domain.Line;
import subway.domain.repository.LineRepository;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse createLine(final LineRequest lineRequest) {
        final List<LineResponse> lines = findAll();
        for(LineResponse line : lines){
            if(line.getName().equals(lineRequest.getName())){
                throw new IllegalArgumentException("이미 존재하는 노선입니다");
            }
        }
        final Line line = new Line(lineRequest.getName());
        final long lineId = lineRepository.createLine(line);

        return new LineResponse(lineId, lineRequest.getName());
    }

    public void deleteLine(final Long lineIdRequest) {
        lineRepository.deleteById(lineIdRequest);
    }

    public List<LineResponse> findAll(){
        return LineResponse.of(lineRepository.findAll());
    }
}
