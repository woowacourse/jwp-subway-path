package subway.dao;

import static subway.dao.mapper.LineMapper.convertLineWithSectionRes;
import static subway.exception.ErrorCode.DB_DELETE_ERROR;
import static subway.exception.ErrorCode.DB_UPDATE_ERROR;
import static subway.exception.ErrorCode.LINE_NOT_FOUND;

import java.util.List;
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
        final LineEntity requestLineEntity = new LineEntity(line.name().name(), line.color(),
            line.extraFare().fare());
        return lineDao.insert(requestLineEntity);
    }

    @Override
    public List<LineWithSectionRes> findWithSectionsByLineId(final Long id) {
        final List<LineWithSection> lineWithSections = lineDao.findByLindIdWithSections(id);
        return convertLineWithSectionRes(lineWithSections);
    }

    @Override
    public List<LineWithSectionRes> findAllWithSections() {
        final List<LineWithSection> lineWithSections = lineDao.findAllWithSections();
        return convertLineWithSectionRes(lineWithSections);
    }

    @Override
    public Line findById(final Long id) {
        return lineDao.findById(id)
            .map(entity -> new Line(entity.getName(), entity.getColor(), entity.getExtraFare()))
            .orElseThrow(() -> new NotFoundException(
                LINE_NOT_FOUND,
                LINE_NOT_FOUND.getMessage() + " id = " + id));
    }

    @Override
    public void updateById(final Long id, final Line line) {
        final LineEntity requestLineEntity = new LineEntity(id, line.name().name(), line.color(),
            line.extraFare().fare());
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

    @Override
    public List<LineWithSectionRes> getPossibleSections(final Long sourceStationId, final Long targetStationId) {
        final List<LineWithSection> lineWithSections = lineDao.getAllLineSectionsSourceAndTargetStationId(
            sourceStationId, targetStationId);
        return convertLineWithSectionRes(lineWithSections);
    }
}
