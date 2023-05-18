package subway.dao;

import static subway.dao.mapper.SectionMapper.convertSectionEntity;
import static subway.exception.ErrorCode.DB_DELETE_ERROR;

import org.springframework.stereotype.Repository;
import subway.dao.entity.SectionEntity;
import subway.domain.section.SectionRepository;
import subway.domain.section.dto.SectionSaveReq;
import subway.exception.DBException;

@Repository
public class SectionRepositoryImpl implements SectionRepository {

    private final SectionDao sectionDao;

    public SectionRepositoryImpl(final SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Override
    public Long insert(final SectionSaveReq sectionSaveReq) {
        final SectionEntity sectionEntity = convertSectionEntity(sectionSaveReq);
        return sectionDao.insert(sectionEntity);
    }

    @Override
    public void deleteOldSection(final Long lineId, final Long sourceStationId) {
        final int deletedCount = sectionDao.deleteByLineIdAndSourceStationId(lineId, sourceStationId);
        if (deletedCount != 1) {
            throw new DBException(DB_DELETE_ERROR);
        }
    }

    @Override
    public void deleteByLineIdAndStationId(final Long lineId, final Long stationId) {
        sectionDao.deleteByLineIdAndStationId(lineId, stationId);
    }
}
