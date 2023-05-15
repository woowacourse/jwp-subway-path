package subway.dao;

import static subway.exception.ErrorCode.DB_DELETE_ERROR;
import static subway.exception.ErrorCode.DB_UPDATE_ERROR;
import static subway.exception.ErrorCode.LINE_NOT_FOUND;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import subway.dao.dto.LineWithSection;
import subway.dao.entity.LineEntity;
import subway.domain.line.Line;
import subway.domain.line.LineRepository;
import subway.domain.line.LineWithSectionRes;
import subway.exception.DBException;
import subway.exception.NotFoundException;

@Repository
public class LineRepositoryImpl implements LineRepository {

    private final LineDao lineDao;

    public LineRepositoryImpl(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    @Override
    public Long insert(final Line line) {
        final LineEntity requestLineEntity = new LineEntity(line.getName().name(), line.getColor());
        return lineDao.insert(requestLineEntity);
    }

    @Override
    public List<LineWithSectionRes> findWithSectionsByLineId(final Long id) {
        final List<LineWithSection> lineWithSections = lineDao.findByLindIdWithSections(id);
        return lineWithSections.stream()
            .map(section -> new LineWithSectionRes(section.getLineId(), section.getLineName(), section.getLineColor(),
                section.getSourceStationId(), section.getSourceStationName(), section.getTargetStationId(),
                section.getTargetStationName(), section.getDistance()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public List<LineWithSectionRes> findAllWithSections() {
        final List<LineWithSection> lineWithSections = lineDao.findAllWithSections();
        return lineWithSections.stream()
            .map(section -> new LineWithSectionRes(section.getLineId(), section.getLineName(), section.getLineColor(),
                section.getSourceStationId(), section.getSourceStationName(), section.getTargetStationId(),
                section.getTargetStationName(), section.getDistance()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Line findById(final Long id) {
        return lineDao.findById(id)
            .map(entity -> new Line(entity.getName(), entity.getColor()))
            .orElseThrow(() -> new NotFoundException(LINE_NOT_FOUND.getMessage() + " id = " + id));
    }

    @Override
    public void updateById(final Long id, final Line line) {
        final LineEntity requestLineEntity = new LineEntity(id, line.getName().name(), line.getColor());
        final int updatedCount = lineDao.update(requestLineEntity);
        if (updatedCount != 1) {
            throw new DBException(DB_UPDATE_ERROR);
        }
    }

    @Override
    public void deleteById(final Long id) {
        final int deletedCount = lineDao.deleteById(id);
        if (deletedCount != 1) {
            throw new DBException(DB_DELETE_ERROR);
        }
    }

    @Override
    public boolean existByName(final String name) {
        return lineDao.existByName(name);
    }
}
