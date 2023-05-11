package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.entity.LineEntity;
import subway.repository.LineRepository;
import subway.repository.StationRepository;
import subway.repository.dao.LineDao;

@Service
public class LineService {

    private final LineDao lineDao;
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public LineService(LineDao lineDao, LineRepository lineRepository,
                       StationRepository stationRepository) {
        this.lineDao = lineDao;
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line line = request.toDomain();
        Long saveId = lineRepository.save(line);
        return new LineResponse(saveId);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLines = findLines();
        return persistLines.stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public List<LineEntity> findLines() {
        return lineDao.findAll();
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity persistLine = findLineById(id);
        return LineResponse.of(persistLine);
    }

    public LineEntity findLineById(Long id) {
        return lineDao.findById(id);
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineDao.update(new LineEntity(id, lineUpdateRequest.getLineName()));
    }

    public void deleteLineById(Long id) {
        lineDao.deleteById(id);
    }

}
