package subway.facade;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.domain.entity.LineEntity;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.exception.LineNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LineFacade {

    private final LineDao lineDao;

    public LineFacade(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Transactional
    public Long insert(final LineEntity line) {
        return lineDao.insert(line);
    }

    public List<LineResponse> findAll() {
        return lineDao.findAll().stream()
                .map(LineResponse::of)
                .collect(Collectors.toList());
    }

    public LineResponse findById(final Long id) {
        return LineResponse.of(lineDao.findById(id)
                .orElseThrow(() -> LineNotFoundException.THROW));
    }

    @Transactional
    public void update(final Long id, final LineRequest request) {
        LineEntity lineEntity = lineDao.findById(id)
                .orElseThrow(() -> LineNotFoundException.THROW);
        lineEntity.updateInfo(request.getName(), request.getColor());
        lineDao.updateById(id, lineEntity);
    }

    @Transactional
    public void deleteById(final Long id) {
        lineDao.deleteById(id);
    }

}
