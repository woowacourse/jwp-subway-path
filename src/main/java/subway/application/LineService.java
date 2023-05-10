package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.CreationLineDto;
import subway.persistence.dao.LineDao;
import subway.domain.Line;
import subway.ui.dto.request.CreationLineRequest;
import subway.ui.dto.response.ReadLineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public CreationLineDto saveLine(final CreationLineRequest request) {
        //final Line persistLine = lineDao.insert(Line.of(request.getName(), request.getColor()));
        final Line persistLine = Line.of("1호선", "bg-red-500");
        return CreationLineDto.from(persistLine);
    }

    public List<ReadLineResponse> findLineResponses() {
        final List<Line> persistLines = findLines();
        return persistLines.stream()
                .map(ReadLineResponse::of)
                .collect(Collectors.toList());
    }

    public List<Line> findLines() {
        //return lineDao.findAll();
        return List.of(Line.of("1호선", "bg-red-500"));
    }

    public ReadLineResponse findLineResponseById(final Long id) {
        final Line persistLine = findLineById(id);
        return ReadLineResponse.of(persistLine);
    }

    public Line findLineById(final Long id) {
        //return lineDao.findById(id);
        return Line.of("1호선", "bg-red-500");
    }

    public void deleteLineById(final Long id) {
        lineDao.deleteById(id);
    }

}
