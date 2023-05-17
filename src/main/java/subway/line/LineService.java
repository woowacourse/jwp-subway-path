package subway.line;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.line.domain.Line;
import subway.line.dto.LineCreateDto;
import subway.line.dto.LineResponseDto;
import subway.line.persistence.LineDao;
import subway.line.persistence.LineEntity;
import subway.section.SectionService;
import subway.section.domain.Sections;

@Service
@Transactional(readOnly = true)
public class LineService {

    private final LineDao lineDao;
    private final SectionService sectionService;

    public LineService(final LineDao lineDao, final SectionService sectionService) {
        this.lineDao = lineDao;
        this.sectionService = sectionService;
    }

    @Transactional
    public Long create(final LineCreateDto lineCreateDto) {
        final Optional<LineEntity> lineEntity = lineDao.findByName(lineCreateDto.getName());
        if (lineEntity.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 노선 이름입니다.");
        }
        final Line line = new Line(lineCreateDto.getName());
        return lineDao.insert(new LineEntity(line.getLineName()));
    }

    public List<LineResponseDto> findAllLines() {
        final List<LineEntity> lines = lineDao.findAll();

        return lines.stream()
            .map((line) -> new LineResponseDto(line.getId(), line.getLineName(),
                sectionService.findSortedStations(line.getId())))
            .collect(Collectors.toList());
    }

}
