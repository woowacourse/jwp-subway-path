package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.LineEntity;
import subway.dto.LineDto;
import subway.dto.LineSaveDto;

@Transactional(readOnly = true)
@Service
public class LineService {
    private final LineDao lineDao;

    public LineService(LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Transactional
    public LineDto saveLine(LineSaveDto request) {
        Long savedId = lineDao.insert(new LineEntity(request.getLineName()));

        return new LineDto(savedId, request.getLineName());
    }

    public List<LineDto> findLineResponses() {
        List<LineEntity> persistLines = lineDao.findAll();
        return persistLines.stream()
                .map(entities -> new LineDto(entities.getId(), entities.getName()))
                .collect(Collectors.toList());
    }

    public LineDto findLineResponseById(Long id) {
        LineEntity findEntity = lineDao.findById(id);
        return new LineDto(id, findEntity.getName());
    }

}
