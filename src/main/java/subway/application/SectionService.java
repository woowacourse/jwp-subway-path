package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDAO;
import subway.dao.StationDao;
import subway.domain.AdjacentSection;
import subway.domain.Direction;
import subway.domain.LineSections;
import subway.entity.SectionEntity;
import subway.dto.DeleteSectionRequest;
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
    
    public List<SectionResponse> getSections(final long lineId) {
        return this.sectionDAO.findSectionsBy(lineId)
                .stream()
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
    
    public void validate(final SectionRequest sectionRequest) {
        this.validateLine(sectionRequest.getLineId());
        this.validateStation(sectionRequest.getBaseStationId());
        this.validateStation(sectionRequest.getNewStationId());
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
    
    public List<SectionResponse> saveSection(final SectionRequest sectionRequest) {
        final List<SectionEntity> sectionEntities = this.sectionDAO.findSectionsBy(sectionRequest.getLineId());
        final LineSections lineSections = LineSections.from(sectionEntities);
        if (lineSections.isEmpty()) {
            return this.creatNewSection(sectionRequest);
        }
        return this.addNewStationInSection(sectionRequest, lineSections);
    }
    
    private List<SectionResponse> creatNewSection(final SectionRequest sectionRequest) {
        final SectionEntity sectionEntity = SectionEntity.from(sectionRequest);
        final SectionResponse sectionResponse = SectionResponse.of(this.sectionDAO.insert(sectionEntity));
        return List.of(sectionResponse);
    }
    
    private List<SectionResponse> addNewStationInSection(final SectionRequest sectionRequest,
            final LineSections lineSections) {
        
        if (lineSections.hasStation(sectionRequest.getNewStationId())) {
            throw new InvalidInputException("새로운역이 라인에 이미 존재합니다.");
        }
        
        final long baseStationId = sectionRequest.getBaseStationId();
        if (!lineSections.hasStation(baseStationId)) {
            throw new InvalidInputException("기준역이 라인에 존재하지 않습니다.");
        }
        
        final Direction direction = Direction.of(sectionRequest.getDirection());
        
        // 종점인 경우
        if ((lineSections.isUpTerminalStation(baseStationId) && direction.isUp()) || (
                lineSections.isDownTerminalStation(baseStationId) && direction.isDown())) {
            return this.creatNewSection(sectionRequest);
        }
        
        // 중간에 넣는 경우
        final List<SectionEntity> baseStationSectionEntities = lineSections.getAdjacentSections(baseStationId);
        final AdjacentSection adjacentSection = AdjacentSection.filter(baseStationSectionEntities, baseStationId, direction);
        
        adjacentSection.validate(sectionRequest.getDistance());
        final SectionEntity oldSectionEntity = adjacentSection.getSection();
        this.sectionDAO.deleteById(oldSectionEntity.getId());
        return adjacentSection.splitTwoSections(sectionRequest.getNewStationId(), sectionRequest.getDistance(),
                        sectionRequest.getLineId()).stream()
                .map(this.sectionDAO::insert)
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
    
    public void validate(final DeleteSectionRequest deleteSectionRequest) {
        this.validateLine(deleteSectionRequest.getLineId());
        this.validateStation(deleteSectionRequest.getStationId());
    }
    
    private List<SectionEntity> findSections(final long stationId, final long lineId) {
        final List<SectionEntity> sectionEntities = this.sectionDAO.findSectionsBy(stationId, lineId);
        if (sectionEntities.size() == 0) {
            throw new InvalidInputException(stationId + "는 라인 아이디 " + lineId + "에 존재하지 않는 역 아이디입니다.");
        }
        return sectionEntities;
    }
    
    public void deleteSection(final DeleteSectionRequest deleteSectionRequest) {
        final List<SectionEntity> sectionEntities = this.findSections(deleteSectionRequest.getStationId(),
                deleteSectionRequest.getLineId());
        if (sectionEntities.size() == 1) {
            this.sectionDAO.deleteById(sectionEntities.get(0).getId());
            return;
        }
        
        if (sectionEntities.size() == 2) {
            final SectionEntity sectionEntity1 = sectionEntities.get(0);
            final SectionEntity sectionEntity2 = sectionEntities.get(1);
            this.sectionDAO.deleteById(sectionEntity1.getId());
            this.sectionDAO.deleteById(sectionEntity2.getId());
            
            if (sectionEntity1.getUpStationId() == deleteSectionRequest.getStationId()) {
                this.mergeSections(sectionEntity1, sectionEntity2);
                return;
            }
            this.mergeSections(sectionEntity2, sectionEntity1);
            return;
        }
        throw new InvalidInputException("잘못된 요청입니다.");
    }
    
    private void mergeSections(final SectionEntity upSectionEntity,
            final SectionEntity downSectionEntity) {
        final int distance = upSectionEntity.getDistance() + downSectionEntity.getDistance();
        final SectionEntity newSectionEntity = new SectionEntity(upSectionEntity.getLineId(), upSectionEntity.getUpStationId(),
                downSectionEntity.getDownStationId(), distance);
        this.sectionDAO.insert(newSectionEntity);
    }
}