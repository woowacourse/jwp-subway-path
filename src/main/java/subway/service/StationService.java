package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class StationService {

    private final SectionService sectionService;
    private final LineService lineService;

    public StationService(
            final SectionService sectionService,
            final LineService lineService
    ) {
        this.sectionService = sectionService;
        this.lineService = lineService;
    }

    public void registerStation(final StationRegisterRequest stationRegisterRequest) {

        final String lineName = stationRegisterRequest.getLineName();

        final Line line = lineService.findByLineName(lineName);

        final List<Section> originSection = line.getSections();
        final Section newSection = mapToSectionFrom(stationRegisterRequest);

        line.add(newSection);

        sectionService.registerSection(
                stationRegisterRequest.getCurrentStationName(),
                stationRegisterRequest.getNextStationName(),
                stationRegisterRequest.getDistance(),
                line.getId()
        );

        final List<Section> updatedSection = line.getSections();

        updatedSection.stream()
                      .filter(it -> originSection.stream().noneMatch(it::isSame))
                      .filter(it -> Objects.nonNull(it.getId()))
                      .findFirst()
                      .ifPresent(it -> sectionService.updateSection(it, line.getId()));
    }

    private Section mapToSectionFrom(final StationRegisterRequest stationRegisterRequest) {
        final Stations newStations = new Stations(
                new Station(stationRegisterRequest.getCurrentStationName()),
                new Station(stationRegisterRequest.getNextStationName()),
                stationRegisterRequest.getDistance()
        );

        return new Section(newStations);
    }

    public void deleteStation(final StationDeleteRequest stationDeleteRequest) {

        final String lineName = stationDeleteRequest.getLineName();
        final Line line = lineService.findByLineName(lineName);

        final List<Section> originSection = line.getSections();

        line.delete(new Station(stationDeleteRequest.getStationName()));

        final List<Section> updatedSection = line.getSections();

        updatedSection.stream()
                      .filter(it -> originSection.stream().noneMatch(it::isSame))
                      .findAny()
                      .ifPresentOrElse(
                              it -> {
                                  sectionService.updateSection(it, line.getId());

                                  originSection.stream()
                                               .filter(origin -> updatedSection.stream().noneMatch(origin::isSame))
                                               .filter(origin -> !origin.equals(it))
                                               .findFirst()
                                               .ifPresent(origin -> sectionService.deleteSection(origin.getId()));

                              },

                              () -> originSection.stream()
                                                 .filter(it -> updatedSection.stream().noneMatch(it::isSame))
                                                 .findFirst()
                                                 .ifPresent(it -> sectionService.deleteSection(it.getId()))
                      );

        if (line.isDeleted()) {
            sectionService.deleteAll(line.getId());
            lineService.deleteLine(line.getId());
        }
    }
}
