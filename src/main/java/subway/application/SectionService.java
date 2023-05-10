package subway.application;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDAO;
import subway.dao.StationDao;
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
    
    public List<SectionResponse> getSections(final long lineId) {
        return this.sectionDAO.findSectionsBy(lineId)
                .stream()
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }
    
    public List<SectionResponse> saveSection(final SectionRequest sectionRequest) {
        // 구역테이블에 라인이 존재하지 않는 경우 (신규 둥록)
        final int count = this.sectionDAO.countSectionInLine(sectionRequest.getLineId());
        if (count == 0) {
            return this.creatNewSection(sectionRequest);
        }
        return this.addNewStationInSection(sectionRequest);
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
    
    private List<SectionResponse> creatNewSection(final SectionRequest sectionRequest) {
        return List.of(SectionResponse.of(this.sectionDAO.insert(Section.from(sectionRequest))));
    }
    
    private List<SectionResponse> addNewStationInSection(final SectionRequest sectionRequest) {
        final List<Section> newStationSections = this.sectionDAO.findSectionsBy(sectionRequest.getNewStationId(),
                sectionRequest.getLineId());
        if (newStationSections.size() != 0) {
            throw new InvalidInputException("새로운역이 라인에 이미 존재합니다.");
        }
        
        final List<Section> baseStationSections = this.sectionDAO.findSectionsBy(sectionRequest.getBaseStationId(),
                sectionRequest.getLineId());
        final int sectionSize = baseStationSections.size();
        if (sectionSize == 0) {
            throw new InvalidInputException("기준역이 라인에 존재하지 않습니다.");
        }
        
        if (sectionSize == 1) {
            if (baseStationSections.get(0).getUpStationId() == sectionRequest.getBaseStationId()
                    && !sectionRequest.isDirection()) {
                //새로운 역 상행 종점
                return this.creatNewSection(sectionRequest);
            }
            if (baseStationSections.get(0).getDownStationId() == sectionRequest.getBaseStationId()
                    && sectionRequest.isDirection()) {
                //새로운 역 하행 종점
                return this.creatNewSection(sectionRequest);
            }
        }
        
        // 새로운 역이 종점이 아닐 경우
        for (final Section section : baseStationSections) {
            if (sectionRequest.isDirection() && section.getUpStationId() == sectionRequest.getBaseStationId()) {
                // 거리비교
                if (sectionRequest.getDistance() >= section.getDistance()) {
                    throw new InvalidInputException("거리가 기존 구역 거리를 초과했습니다.");
                }
                // 구역 하나 제거하고, 두개 입력해야함
                this.sectionDAO.deleteById(section.getId());
                final Section newSection1 = this.sectionDAO.insert(Section.from(sectionRequest));
                
                //up
                final long upStationId = sectionRequest.getNewStationId();
                //down
                final long downStationId = section.getDownStationId();
                //distance
                final int distance = section.getDistance() - sectionRequest.getDistance();
                final Section newSection2 = new Section(section.getLineId(), upStationId, downStationId, distance);
                this.sectionDAO.insert(newSection2);
                return List.of(SectionResponse.of(newSection1), SectionResponse.of(newSection2));
            }
            if (!sectionRequest.isDirection() && section.getDownStationId() == sectionRequest.getBaseStationId()) {
                // 거리비교
                if (sectionRequest.getDistance() >= section.getDistance()) {
                    throw new InvalidInputException("거리가 기존 구역 거리를 초과했습니다");
                }
                
                // 구역 하나 제거하고, 두개 입력해야함
                this.sectionDAO.deleteById(section.getId());
                final Section newSection1 = this.sectionDAO.insert(Section.from(sectionRequest));
                
                //up
                final long upStationId = section.getDownStationId();
                //down
                final long downStationId = sectionRequest.getNewStationId();
                //distance
                final int distance = section.getDistance() - sectionRequest.getDistance();
                final Section newSection = new Section(section.getLineId(), upStationId, downStationId, distance);
                final Section newSection2 = this.sectionDAO.insert(newSection);
                return List.of(SectionResponse.of(newSection1), SectionResponse.of(newSection2));
            }
        }
        throw new InvalidInputException("도달할 수 없는 예외입니다." + sectionSize);
    }
    
    public void validate(final DeleteSectionRequest deleteSectionRequest) {
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
    
    public void deleteSection(final DeleteSectionRequest deleteSectionRequest) {
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