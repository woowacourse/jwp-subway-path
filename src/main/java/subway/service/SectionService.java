package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.controller.exception.OptionalHasNoLineException;
import subway.controller.exception.OptionalHasNoStationException;
import subway.domain.line.Line;
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
        Line line = lineDao.findById(request.getLineId())
                .orElseThrow(OptionalHasNoLineException::new);
        Station upward = stationDao.findById(request.getUpwardId())
                .orElseThrow(OptionalHasNoStationException::new);
        Station downward = stationDao.findById(request.getDownwardId())
                .orElseThrow(OptionalHasNoStationException::new);

        LineSections lineSections = LineSections.from(line, sectionRepository.readSectionsByLine(line));
        lineSections.addSection(upward, downward, request.getDistance());

        sectionRepository.updateAllSectionsInLine(line, lineSections.getAllSections());
    }

    public void removeStationFromLine(SectionDeleteRequest request) {
        Station removeStation = stationDao.findById(request.getStationId())
                .orElseThrow(OptionalHasNoStationException::new);
        Line line = lineDao.findById(request.getLineId())
                .orElseThrow(OptionalHasNoStationException::new);

        LineSections lineSections = LineSections.from(line, sectionRepository.readSectionsByLine(line));
        lineSections.removeStation(removeStation);

        sectionRepository.updateAllSectionsInLine(line, lineSections.getAllSections());
    }

    public LineStationsResponse readAllStationsOfLine(Long lineId) {
        Line line = lineDao.findById(lineId)
                .orElseThrow(OptionalHasNoStationException::new);

        LineSections lineSections = LineSections.from(line, sectionRepository.readSectionsByLine(line));
        List<Station> orderedStations = lineSections.getOrderedStations();

        return DtoMapper.convertToLineStationsResponse(line, orderedStations);
    }

    public List<LineStationsResponse> readAllStationsOfAllLines() {
        List<Line> lines = lineDao.findAll();
        List<LineStationsResponse> lineStationsResponses = new ArrayList<>();

        lines.forEach(line -> lineStationsResponses.add(readAllStationsOfLine(line.getId())));

        return lineStationsResponses;
    }
}
