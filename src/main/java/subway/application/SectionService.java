package subway.application;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDAO;
import subway.dao.StationDao;
import subway.domain.Section;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.exception.InvalidInputException;

@Service
public class SectionService {
    
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDAO sectionDAO;
    
    public SectionService(final SectionDAO sectionDAO, final LineDao lineDao, final StationDao stationDao) {
        this.sectionDAO = sectionDAO;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }
    
    public SectionResponse saveSection(final SectionRequest sectionRequest) {
        final Section section = Section.from(sectionRequest);
        return SectionResponse.of(this.sectionDAO.insert(section));
    }
    
    public void validate(final SectionRequest sectionRequest) {
        this.validateLine(sectionRequest.getLineId());
        this.validateStation(sectionRequest.getBaseStationId());
        this.validateStation(sectionRequest.getNewStationId());
        
        // 구역테이블에 라인이 존재하지 않는 경우 (신규 둥록)
        this.validateLineInSection(sectionRequest);
        this.validateBaseStationinLine(sectionRequest.getBaseStationId(), sectionRequest.getLineId());
    }
    
    private void validateLine(final long lineId) {
        try {
            this.lineDao.findById(lineId);
        } catch (final DataAccessException exception) {
            throw new InvalidInputException("존재하지 않는 라인입니다.");
        }
    }
    
    private void validateStation(final long stationId) {
        try {
            this.stationDao.findById(stationId);
        } catch (final DataAccessException exception) {
            throw new InvalidInputException(stationId + "는 존재하지 않는 역 아이디입니다.");
        }
    }
    
    private void validateLineInSection(final SectionRequest sectionRequest) {
        final int count = this.sectionDAO.countSectionInLine(sectionRequest.getLineId());
        if (count == 0) {
            this.creatNewSection(sectionRequest);
            return;
        }
        this.addNewStationInSection(sectionRequest);
    }
    
    private void creatNewSection(final SectionRequest sectionRequest) {
        this.sectionDAO.insert(Section.from(sectionRequest));
    }
    
    private void addNewStationInSection(final SectionRequest sectionRequest) {
    }
    
    private void validateBaseStationinLine(final long baseStationId, final long lineId) {
        this.sectionDAO.findSectionsBy(baseStationId, lineId);
    }
}
