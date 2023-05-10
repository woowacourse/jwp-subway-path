package subway.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.dao.section.SectionDao;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.entity.LineEntity;
import subway.domain.entity.SectionEntity;
import subway.domain.entity.StationEntity;
import subway.dto.section.SectionCreateRequest;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {

    private final LineService lineService;
    private final StationService stationService;
    private final SectionDao sectionDao;

    public SectionService(final LineService lineService, final StationService stationService, final SectionDao sectionDao) {
        this.lineService = lineService;
        this.stationService = stationService;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public void insertSection(final SectionCreateRequest request) {
        // 있는지 없는지 조회
        LineEntity lineEntity = lineService.findLineByName(request.getLineName());

        StationEntity stationAEntity = stationService.findStationByName(request.getUpStation());
        StationEntity stationBEntity = stationService.findStationByName(request.getDownStation());

        Station stationA = new Station(stationAEntity.getName());
        Station stationB = new Station(stationBEntity.getName());
        Section requestSection = new Section(stationA, stationB, request.getDistance());
        SectionEntity requestSectionEntity = new SectionEntity(null, lineEntity.getLineId(), stationAEntity.getStationId(), stationBEntity.getStationId(), request.getDistance());

        List<Section> foundSections = sectionDao.findSectionsByLineId(lineEntity.getLineId())
                .stream()
                .map(sectionEntity -> {
                    StationEntity stationEntityA = stationService.findStationEntityById(sectionEntity.getUpStationId());
                    Station upStation = new Station(stationEntityA.getName());
                    StationEntity stationEntityB = stationService.findStationEntityById(sectionEntity.getDownStationId());
                    Station downStation = new Station(stationEntityB.getName());
                    return new Section(upStation, downStation, sectionEntity.getDistance());
                })
                .collect(Collectors.toList());

        Sections sections = new Sections(foundSections);


        boolean isExistA = sections.isExistStation(requestSection.getUpStation());
        boolean isExistB = sections.isExistStation(requestSection.getDownStation());
        sections.validateFirst(isExistA, isExistB);

        if (!isExistA && !isExistB) {
            sections.addSection(new Section(stationA, stationB, requestSection.getDistance()));
            sectionDao.insert(new SectionEntity(null, lineEntity.getLineId(), stationAEntity.getStationId(), stationBEntity.getStationId(), requestSection.getDistance()));
        }


        // 이미 A만 존재하는 경우
        if (isExistA && !isExistB) {
            boolean isExistAsUpStation = sections.isExistAsUpStation(requestSection.getUpStation());

            if (isExistAsUpStation) {
                // 상행으로 존재하는 경우
                Section foundSection = sections.findSectionWithUpStation(requestSection.getUpStation());
                StationEntity foundSectionUpStationEntity = stationService.findStationByName(foundSection.getUpStation().getName());
                StationEntity foundSectionDownStationEntity = stationService.findStationByName(foundSection.getDownStation().getName());
                foundSection.validateDistance(requestSection.getDistance());

                sections.removeSection(foundSection);
                sections.addSection(requestSection);
                Section newSection = new Section(requestSection.getDownStation(), foundSection.getDownStation(), foundSection.getDistance() - requestSection.getDistance());
                sections.addSection(newSection);

                sectionDao.remove(new SectionEntity(null, lineEntity.getLineId(), foundSectionUpStationEntity.getStationId(), foundSectionDownStationEntity.getStationId(), foundSection.getDistance()));
                sectionDao.insert(requestSectionEntity);
                sectionDao.insert(new SectionEntity(null, lineEntity.getLineId(), stationBEntity.getStationId(), foundSectionDownStationEntity.getStationId(), foundSection.getDistance() - requestSection.getDistance()));
            }

            if (!isExistAsUpStation) {
                sections.addSection(requestSection);
                sectionDao.insert(requestSectionEntity);
            }

        }


        if (!isExistA && isExistB) {
            boolean isExistAsUpStation = sections.isExistAsUpStation(requestSection.getDownStation());

            if (isExistAsUpStation) {
                // 상행으로 존재하는 경우
                sections.addSection(requestSection);
                sectionDao.insert(requestSectionEntity);
            }

            if (!isExistAsUpStation) {
                // 하행으로 존재하는 경우
                Section foundSection = sections.findSectionWithDownStation(requestSection.getDownStation());
                StationEntity foundSectionUpStationEntity = stationService.findStationByName(foundSection.getUpStation().getName());
                StationEntity foundSectionDownStationEntity = stationService.findStationByName(foundSection.getDownStation().getName());
                foundSection.validateDistance(requestSection.getDistance());

                sections.removeSection(foundSection);
                sections.addSection(requestSection);
                Section newSection = new Section(foundSection.getUpStation(), requestSection.getUpStation(), foundSection.getDistance() - requestSection.getDistance());
                sections.addSection(newSection);

                sectionDao.remove(new SectionEntity(null, lineEntity.getLineId(), foundSectionUpStationEntity.getStationId(), foundSectionDownStationEntity.getStationId(), foundSection.getDistance()));
                sectionDao.insert(requestSectionEntity);
                sectionDao.insert(new SectionEntity(null, lineEntity.getLineId(), foundSectionUpStationEntity.getStationId(), requestSectionEntity.getUpStationId(), foundSection.getDistance() - requestSection.getDistance()));
            }
        }
    }

    @Transactional(readOnly = true)
    public List<SectionEntity> findSectionsByLineNumber(final Long lineNumber) {
        LineEntity lineEntity = lineService.findByLineNumber(lineNumber);
        return sectionDao.findSectionsByLineId(lineEntity.getLineId());
    }
}
