package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.persistence.entity.LineEntity;
import subway.persistence.entity.SectionEntity;
import subway.persistence.entity.StationEntity;
import subway.persistence.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {

    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        final LineEntity lineEntity = new LineEntity(request.getName(), request.getColor());
        final LineEntity inserted = lineRepository.saveLine(lineEntity);
        return LineResponse.of(inserted);
    }

    public LineResponse findLineResponseById(Long id) {
        LineEntity persistLine = lineRepository.findLineById(id);
        Line line = toLine(persistLine);
        return LineResponse.of(line);
    }

    public List<LineResponse> findLineResponses() {
        List<LineEntity> persistLines = lineRepository.findAllLines();
        System.out.println("persistLines = " + persistLines);
        List<Line> lines = toLines(persistLines);
        return lines.stream()
                .map(line -> LineResponse.of(line))
                .collect(Collectors.toList());
    }

    private List<Line> toLines(final List<LineEntity> persistLines) {
        return persistLines.stream()
                .map(lineEntity -> toLine(lineEntity))
                .collect(Collectors.toList());
    }

    private Line toLine(final LineEntity lineEntity) {
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(), toSections(lineEntity));
    }

    private Sections toSections(final LineEntity lineEntity) {
        return new Sections(
                findSectionEntitiesByLine(lineEntity).stream()
                        .map(sectionEntity -> toSection(sectionEntity))
                        .collect(Collectors.toList())
        );
    }

    private List<SectionEntity> findSectionEntitiesByLine(final LineEntity lineEntity) {
        return lineRepository.findSectionsByLine(lineEntity);
    }

    private Section toSection(SectionEntity sectionEntity) {
        StationEntity upStationEntity = lineRepository.findStationById(sectionEntity.getUpStationId());
        StationEntity downStationEntity = lineRepository.findStationById(sectionEntity.getDownStationId());
        Station upStation = new Station(upStationEntity.getName());
        Station downStation = new Station(downStationEntity.getName());
        return new Section(upStation, downStation, sectionEntity.getDistance());
    }

    public void updateLine(Long id, LineRequest lineUpdateRequest) {
        lineRepository.updateLine(new LineEntity(id, lineUpdateRequest.getName(), lineUpdateRequest.getColor()));
    }

    public void deleteLineById(Long id) {
        lineRepository.deleteLineById(id);
    }

}
