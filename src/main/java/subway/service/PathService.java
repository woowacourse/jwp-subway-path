package subway.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.core.Line;
import subway.domain.core.Section;
import subway.domain.core.Subway;
import subway.domain.fare.FarePolicy;
import subway.domain.fare.Passenger;
import subway.domain.path.Path;
import subway.domain.path.PathFinder;
import subway.domain.path.SectionEdge;
import subway.dto.LineDto;
import subway.dto.SectionDto;
import subway.dto.ShortestPathRequest;
import subway.dto.ShortestPathResponse;
import subway.exception.LineNotFoundException;
import subway.repository.LineRepository;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final LineRepository lineRepository;
    private final FarePolicy farePolicy;

    public PathService(final LineRepository lineRepository, final FarePolicy farePolicy) {
        this.lineRepository = lineRepository;
        this.farePolicy = farePolicy;
    }

    public ShortestPathResponse shortestPath(final ShortestPathRequest request) {
        final Subway subway = new Subway(lineRepository.findAll());
        final PathFinder pathFinder = new PathFinder(subway);
        final Path path = pathFinder.find(request.getStart(), request.getEnd());
        final int fare = farePolicy.calculate(path, new Passenger(request.getAge()), 0);
        return new ShortestPathResponse(toLineDtos(path.getSectionEdges()), path.calculateTotalDistance(), fare);
    }

    private List<LineDto> toLineDtos(final List<SectionEdge> path) {
        final List<LineDto> result = new ArrayList<>();

        List<SectionDto> currentLine = new ArrayList<>();
        long currentLineId = 0;
        for (SectionEdge sectionEdge : path) {
            if (currentLine.isEmpty() || currentLineId != sectionEdge.getLineId()) {
                currentLine = new ArrayList<>();
                currentLineId = sectionEdge.getLineId();
                final Line line = lineRepository.findById(sectionEdge.getLineId())
                        .orElseThrow(LineNotFoundException::new);
                result.add(new LineDto(line.getName(), line.getColor(), currentLine));
            }
            final Section section = sectionEdge.toSection();
            currentLine.add(SectionDto.from(section));
        }
        return result;
    }
}
