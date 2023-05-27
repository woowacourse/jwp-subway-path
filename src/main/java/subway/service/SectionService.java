package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.Entity.EntityMapper;
import subway.Entity.LineEntity;
import subway.controller.exception.OptionalHasNoLineException;
import subway.controller.exception.OptionalHasNoStationException;
import subway.domain.section.LineSections;
import subway.domain.section.SectionRepository;
import subway.domain.station.Station;
import subway.dto.DtoMapper;
import subway.dto.request.SectionDeleteRequest;
import subway.dto.request.SectionRequest;
import subway.dto.response.LineStationsResponse;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.StationDao;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SectionService {

    private final LineDao lineDao;
    private final StationDao stationDao;
    private final SectionRepository sectionRepository;

    public SectionService(LineDao lineDao, StationDao stationDao, SectionRepository sectionRepository) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.sectionRepository = sectionRepository;
    }

    public void saveSectionInLine(SectionRequest request) {
        LineEntity lineEntity = lineDao.findById(request.getLineId())
                .orElseThrow(OptionalHasNoLineException::new);
        Station upward = stationDao.findById(request.getUpwardId())
                .orElseThrow(OptionalHasNoStationException::new)
                .mapToStation();
        Station downward = stationDao.findById(request.getDownwardId())
                .orElseThrow(OptionalHasNoStationException::new)
                .mapToStation();

        LineSections lineSections = LineSections.from(
                lineEntity.mapToLine(),
                sectionRepository.readSectionsByLine(lineEntity));
        lineSections.addSection(upward, downward, request.getDistance());

        sectionRepository.updateAllSectionsInLine(lineEntity, lineSections.getAllSections());
    }

    public void removeStationFromLine(SectionDeleteRequest request) {
        Station removeStation = stationDao.findById(request.getStationId())
                .orElseThrow(OptionalHasNoStationException::new)
                .mapToStation();
        LineEntity lineEntity = lineDao.findById(request.getLineId())
                .orElseThrow(OptionalHasNoStationException::new);

        LineSections lineSections = LineSections.from(
                lineEntity.mapToLine(),
                sectionRepository.readSectionsByLine(lineEntity)
        );
        lineSections.removeStation(removeStation);

        sectionRepository.updateAllSectionsInLine(lineEntity, lineSections.getAllSections());
    }

    public LineStationsResponse readAllStationsOfLine(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId)
                .orElseThrow(OptionalHasNoStationException::new);

        LineSections lineSections = LineSections.from(
                lineEntity.mapToLine(),
                sectionRepository.readSectionsByLine(lineEntity)
        );
        List<Station> orderedStations = lineSections.getOrderedStations();

        return DtoMapper.convertToLineStationsResponse(
                lineEntity,
                EntityMapper.convertToStationEntities(orderedStations, stationDao.findAll())
        );
    }

    public List<LineStationsResponse> readAllStationsOfAllLines() {
        List<LineEntity> lineEntities = lineDao.findAll();
        List<LineStationsResponse> lineStationsResponses = new ArrayList<>();

        lineEntities.forEach(lineEntity -> lineStationsResponses.add(readAllStationsOfLine(lineEntity.getId())));

        return lineStationsResponses;
    }
}
