package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.LineDao;
import subway.dao.SectionDAO;
import subway.dao.StationDao;
import subway.domain.AdjacentSection;
import subway.domain.Direction;
import subway.domain.LineSections;
import subway.domain.Section;
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
    
    @Transactional(readOnly = true)
    public List<SectionResponse> findAll() {
        return this.sectionDAO.findAll()
                .stream()
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
    
    @Transactional(readOnly = true)
    public List<SectionResponse> findSectionsByLineId(final long lineId) {
        return this.sectionDAO.findSectionsBy(lineId)
                .stream()
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private void validate(final SectionRequest sectionRequest) {
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
    
    @Transactional
    public List<SectionResponse> insertSection(final SectionRequest sectionRequest) {
        this.validate(sectionRequest);
        final List<Section> sections = this.sectionDAO.findSectionsBy(sectionRequest.getLineId());
        final LineSections lineSections = LineSections.from(sections);
        if (lineSections.isEmpty()) {
            return this.insertNewSection(sectionRequest);
        }
        return this.insertStationInLine(sectionRequest, lineSections);
    }
    
    private List<SectionResponse> insertNewSection(final SectionRequest sectionRequest) {
        final Section section = Section.from(sectionRequest);
        final SectionResponse sectionResponse = SectionResponse.of(this.sectionDAO.insert(section));
        return List.of(sectionResponse);
    }
    
    private List<SectionResponse> insertStationInLine(final SectionRequest sectionRequest,
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
            return this.insertNewSection(sectionRequest);
        }
        
        // 중간에 넣는 경우
        final List<Section> baseStationSections = lineSections.getAdjacentSections(baseStationId);
        final AdjacentSection adjacentSection = AdjacentSection.filter(baseStationSections, baseStationId, direction);
        
        adjacentSection.validate(sectionRequest.getDistance());
        final Section oldSection = adjacentSection.getSection();
        this.sectionDAO.deleteById(oldSection.getId());
        return adjacentSection.splitTwoSections(sectionRequest.getNewStationId(), sectionRequest.getDistance(),
                        sectionRequest.getLineId()).stream()
                .map(this.sectionDAO::insert)
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
    
    private void validate(final DeleteSectionRequest deleteSectionRequest) {
        this.validateLine(deleteSectionRequest.getLineId());
        this.validateStation(deleteSectionRequest.getStationId());
    }
    
    private List<Section> findSections(final long stationId, final long lineId) {
        final List<Section> sections = this.sectionDAO.findSectionsBy(stationId, lineId);
        if (sections.size() == 0) {
            throw new InvalidInputException(stationId + "는 라인 아이디 " + lineId + "에 존재하지 않는 역 아이디입니다.");
        }
        return sections;
    }
    
    @Transactional
    public void deleteSection(final DeleteSectionRequest deleteSectionRequest) {
        this.validate(deleteSectionRequest);
        final List<Section> sections = this.findSections(deleteSectionRequest.getStationId(),
                deleteSectionRequest.getLineId());
        if (sections.size() == 1) {
            this.sectionDAO.deleteById(sections.get(0).getId());
            return;
        }
        
        if (sections.size() == 2) {
            final Section section1 = sections.get(0);
            final Section section2 = sections.get(1);
            this.sectionDAO.deleteById(section1.getId());
            this.sectionDAO.deleteById(section2.getId());
            
            if (section1.getUpStationId() == deleteSectionRequest.getStationId()) {
                this.mergeSections(section1, section2);
                return;
            }
            this.mergeSections(section2, section1);
            return;
        }
        throw new InvalidInputException("잘못된 요청입니다.");
    }
    
    private void mergeSections(final Section upSection,
            final Section downSection) {
        final int distance = upSection.getDistance() + downSection.getDistance();
        final Section newSection = new Section(upSection.getLineId(), upSection.getUpStationId(),
                downSection.getDownStationId(), distance);
        this.sectionDAO.insert(newSection);
    }
}