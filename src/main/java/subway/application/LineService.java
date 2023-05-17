package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.dto.CreationLineDto;
import subway.application.dto.ReadLineDto;
import subway.domain.line.Line;
import subway.persistence.repository.LineRepository;
import subway.persistence.repository.SectionRepository;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public LineService(final LineRepository lineRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public CreationLineDto saveLine(final String name, final String color) {
        final Line line = Line.of(name, color);
        final Line persistLine = lineRepository.insert(line);

        return CreationLineDto.from(persistLine);
    }

    public List<ReadLineDto> findAllLine() {
        final List<Line> persistLines = lineRepository.findAll();

        final List<Line> lines = persistLines.stream()
                .map(sectionRepository::findAllByLine)
                .collect(Collectors.toList());

        return lines.stream()
                .map(ReadLineDto::from)
                .collect(Collectors.toList());
    }

    public ReadLineDto findLineById(final Long id) {
        final Line persistLine = lineRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다."));
        final Line line = sectionRepository.findAllByLine(persistLine);

        return ReadLineDto.from(line);
    }

    @Transactional
    public void deleteLineById(final Long id) {
        lineRepository.deleteById(id);
    }
}
