package subway.service;

import org.springframework.stereotype.Service;
import subway.domain.*;
import subway.repository.LineRepository;
import subway.repository.SectionRepository;
import subway.repository.StationRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class SubwayService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public SubwayService(final LineRepository lineRepository, final StationRepository stationRepository, final SectionRepository sectionRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public Subway findSubway() {
        final List<Line> lines = lineRepository.findAllLines();

        List<Sections> subwaySections = new ArrayList<>();

        for (final Line line : lines) {
            final List<Section> sections = sectionRepository.findAllSectionOf(line);
            final Graph newGraph = new SubwayGraph();

            final List<Long> allStationIds = sectionRepository.findAllStationIds(line);
            for (final Long stationId : allStationIds) {
                final Station station = stationRepository.findById(stationId);
                newGraph.addStation(station);
            }

            for (final Section section : sections) {
                final Station upStation = section.getUpStation();
                final Station downStation = section.getDownStation();
                final int distance = section.getDistance();

                newGraph.addSection(upStation, downStation, distance);
            }

            subwaySections.add(new Sections(line, newGraph));
            System.out.println("newGraph = " + newGraph);
        }

        return new Subway(subwaySections);
    }
}
