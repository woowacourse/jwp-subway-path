package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.persistence.dao.LineDao;
import subway.persistence.dao.StationDao;
import subway.domain.Line;
import subway.domain.SectionRepository;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.DtoMapper;
import subway.dto.request.SectionDeleteRequest;
import subway.dto.request.SectionRequest;
import subway.dto.response.LineStationsResponse;

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
        Line line = lineDao.findById(request.getLineId());
        Station upward = stationDao.findById(request.getUpwardId());
        Station downward = stationDao.findById(request.getDownwardId());

        Sections lineSections = Sections.from(sectionRepository.readSectionsByLine(line));
        lineSections.addSection(line, upward, downward, request.getDistance());

        sectionRepository.updateAllSectionsInLine(line, lineSections.findLineSections(line));
    }

    public void removeStationFromLine(SectionDeleteRequest request) {
        Station removeStation = stationDao.findById(request.getStationId());
        Line line = lineDao.findById(request.getLineId());

        Sections lineSections = Sections.from(sectionRepository.readSectionsByLine(line));
        lineSections.removeStationFromLine(line, removeStation);

        sectionRepository.updateAllSectionsInLine(line, lineSections.findLineSections(line));
    }

    public LineStationsResponse readAllStationsOfLine(Long lineId) {
        Line line = lineDao.findById(lineId);

        Sections lineSections = Sections.from(sectionRepository.readSectionsByLine(line));
        List<Station> inOrderLineStations = lineSections.findStationsInOrder(line);

        return DtoMapper.convertToLineStationsResponse(line, inOrderLineStations);
    }

    public List<LineStationsResponse> readAllStationsOfAllLines() {
        List<Line> lines = lineDao.findAll();
        List<LineStationsResponse> lineStationsResponses = new ArrayList<>();

        lines.forEach(line -> lineStationsResponses.add(readAllStationsOfLine(line.getId())));

        return lineStationsResponses;
    }
}
