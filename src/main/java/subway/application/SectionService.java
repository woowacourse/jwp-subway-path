package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Station;
import subway.domain.strategy.AddSectionStrategy;
import subway.domain.strategy.DirectionStrategy;
import subway.dto.DeleteSectionRequest;
import subway.dto.SectionRequest;
import subway.dto.SectionResponse;
import subway.entity.SectionEntity;
import subway.exception.InvalidInputException;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionDao sectionDAO;

    private final LineRepository lineRepository;

    public SectionService(final SectionDao sectionDAO, final LineDao lineDao, final StationDao stationDao, LineRepository lineRepository) {
        this.sectionDAO = sectionDAO;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.lineRepository = lineRepository;
    }

    public List<SectionResponse> getSections(final long lineId) {
        return this.sectionDAO.findSectionsBy(lineId)
                .stream()
                .map(SectionResponse::of)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<SectionResponse> saveSection(final SectionRequest sectionRequest) {
        Line findLine = lineRepository.findById(sectionRequest.getLineId());
        Station newStation = lineRepository.findStationById(sectionRequest.getNewStationId());
        Station baseStation = lineRepository.findStationById(sectionRequest.getBaseStationId());

        AddSectionStrategy addSectionStrategy = findLine.preprocess(
                baseStation,
                newStation,
                DirectionStrategy.from(sectionRequest.getDirection()),
                sectionRequest.getDistance()
        );

        addSectionStrategy.execute(lineRepository);
        Line line = lineRepository.findById(findLine.getId());
        return line.getSectionResponse();
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

    private List<SectionEntity> findSections(final long stationId, final long lineId) {
        final List<SectionEntity> sectionEntities = this.sectionDAO.findSectionsBy(stationId, lineId);
        if (sectionEntities.size() == 0) {
            throw new InvalidInputException(stationId + "는 라인 아이디 " + lineId + "에 존재하지 않는 역 아이디입니다.");
        }
        return sectionEntities;
    }

    private void mergeSections(final SectionEntity upSectionEntity,
                               final SectionEntity downSectionEntity) {
        final int distance = upSectionEntity.getDistance() + downSectionEntity.getDistance();
        final SectionEntity newSectionEntity = new SectionEntity(upSectionEntity.getLineId(), upSectionEntity.getUpStationId(),
                downSectionEntity.getDownStationId(), distance);
        this.sectionDAO.insert(newSectionEntity);
    }
}