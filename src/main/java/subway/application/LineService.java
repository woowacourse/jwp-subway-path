package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.dao.SectionDao;
import subway.domain.Line;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.ui.dto.LineRequest;
import subway.ui.dto.LineResponse;

@Service
public class LineService {

    private final SectionDao sectionDao;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(SectionDao sectionDao, LineRepository lineRepository, StationRepository stationRepository) {
        this.sectionDao = sectionDao;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = lineRepository.save(new Line(null, request.getName(), null));
        return LineResponse.of(line);
    }

    public LineResponse findLineResponseById(Long id) {
        Line line = lineRepository.findById(id);
        return LineResponse.of(line);
    }

    public List<LineResponse> findLineResponses() {
        return lineRepository.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteById(id);
    }
}
