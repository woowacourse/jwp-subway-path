package subway.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Entity.SectionEntity;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.DtoMapper;
import subway.dto.InitSectionRequest;
import subway.dto.SectionAtLastRequest;
import subway.dto.SectionLastDeleteRequest;
import subway.dto.SectionRequest;
import subway.dto.StationDeleteRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SectionService {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public SectionService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public void saveSection(SectionRequest request) {
        Line line = lineDao.findById(request.getLineId());
        Station upward = stationDao.findById(request.getUpwardId());
        Station downward = stationDao.findById(request.getDownwardId());
        Station newStation = stationDao.findById(request.getStationId());

        SectionEntity sectionEntity = sectionDao.selectByStationIdsAndLineId(upward.getId(), downward.getId(), line.getId());
        Section originalSection = Section.of(
                sectionEntity.getId(),
                upward,
                downward,
                sectionEntity.getDistance(),
                line
        );

        Sections sections = Sections.from(new ArrayList<>(List.of(originalSection)));
        List<Section> newSections = sections.addSection(
                newStation,
                originalSection,
                request.getUpwardDistance(),
                request.getDownwardDistance()
        );

        sectionDao.deleteById(sectionEntity.getId());

        for (Section section : newSections) {
            sectionDao.insert(DtoMapper.convertToSectionEntity(section));
        }
    }

    public void saveInitSections(InitSectionRequest request) {
        Line line = lineDao.findById(request.getLineId());

        Station upward = stationDao.findById(request.getUpwardId());
        Station downward = stationDao.findById(request.getDownwardId());

        Sections sections = Sections.from(new ArrayList<>());
        List<Section> newSections = sections.addInitSection(upward, downward, request.getDistance(), line);

        for (Section section : newSections) {
            sectionDao.insert(DtoMapper.convertToSectionEntity(section));
        }
    }

    public void saveSectionAtLast(SectionAtLastRequest request) {
        Line line = lineDao.findById(request.getLineId());
        Station originalLastStation = stationDao.findById(request.getOriginalLastStationId());
        Station newStation = stationDao.findById(request.getStationId());

        SectionEntity sectionEntity = sectionDao.selectSectionAtLast(originalLastStation.getId(), line.getId());

        Section lastSection;

        if (sectionEntity.getUpwardId() == originalLastStation.getId()) {
            //하행종착
            lastSection = Section.ofEmptyDownwardSection(
                    originalLastStation,
                    line
            );
            Sections sections = Sections.from(new ArrayList<>(List.of(lastSection)));
            List<Section> newSections = sections.addSectionAtDownwardLast(newStation, lastSection, request.getDistance());
            sectionDao.deleteById(sectionEntity.getId());

            for (Section section : newSections) {
                sectionDao.insert(DtoMapper.convertToSectionEntity(section));
            }
        }

        if (sectionEntity.getDownwardId() == originalLastStation.getId()) {
            //상행종착
            lastSection = Section.ofEmptyUpwardSection(
                    originalLastStation,
                    line
            );
            Sections sections = Sections.from(new ArrayList<>(List.of(lastSection)));
            List<Section> newSections = sections.addSectionAtUpwardLast(newStation, lastSection, request.getDistance());
            sectionDao.deleteById(sectionEntity.getId());

            for (Section section : newSections) {
                sectionDao.insert(DtoMapper.convertToSectionEntity(section));
            }
        }
    }

    public void removeSectionsByStationAndLine(StationDeleteRequest request) {
        Station station = stationDao.findById(request.getStationId());
        Line line = lineDao.findById(request.getLineId());

        List<SectionEntity> sectionEntities = sectionDao.selectSectionsByStationIdAndLineId(station.getId(), line.getId());
        List<Section> sections = sectionEntities.stream().map(entity -> Section.of(
                entity.getId(),
                stationDao.findById(entity.getUpwardId()),
                stationDao.findById(entity.getDownwardId()),
                entity.getDistance(),
                lineDao.findById(entity.getLineId())
        )).collect(Collectors.toList());
        Sections allSections = Sections.from(sections);

        for (SectionEntity sectionEntity : sectionEntities) {
            sectionDao.deleteById(sectionEntity.getId());
        }

        sectionDao.insert(DtoMapper.convertToSectionEntity(allSections.removeSectionsByStation(station, line)));
    }

    public void removeLastSectionInLine(SectionLastDeleteRequest request) {
        Line line = lineDao.findById(request.getLineId());
        Station upward = stationDao.findById(request.getUpwardId());
        Station downward = stationDao.findById(request.getDownwardId());
        List<SectionEntity> sectionEntities = sectionDao.selectAll();
        List<Section> sections = sectionEntities.stream().map(entity -> Section.of(
                entity.getId(),
                stationDao.findById(entity.getUpwardId()),
                stationDao.findById(entity.getDownwardId()),
                entity.getDistance(),
                lineDao.findById(entity.getLineId())
        )).collect(Collectors.toList());
        Sections allSections = Sections.from(sections);
        List<Section> lineSections = allSections.findLineSections(line);
        if (lineSections.size() != 3) {
            throw new IllegalArgumentException("[ERROR] 노선의 마지막 남은 구간이 아니라 삭제할 수 없습니다.");
        }
        for (Section section : lineSections) {
            sectionDao.deleteById(section.getId());
        }
    }
}
