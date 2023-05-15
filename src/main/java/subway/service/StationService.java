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
import java.util.function.Consumer;

@Service
@Transactional
public class StationService {

    private final SectionCommandService sectionCommandService;
    private final LineCommandService lineCommandService;
    private final LineQueryService lineQueryService;

    public StationService(
            final SectionCommandService sectionCommandService,
            final LineCommandService lineCommandService,
            final LineQueryService lineQueryService
    ) {
        this.sectionCommandService = sectionCommandService;
        this.lineCommandService = lineCommandService;
        this.lineQueryService = lineQueryService;
    }

    public void registerStation(final StationRegisterRequest stationRegisterRequest) {

        final String lineName = stationRegisterRequest.getLineName();
        final Line line = lineQueryService.findByLineName(lineName);
        final List<Section> originSection = line.getSections();
        final Section newSection = mapToSectionFrom(stationRegisterRequest);

        line.add(newSection);

        sectionCommandService.registerSection(
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
                      .ifPresent(sectionCommandService::updateSection);
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
        final Line line = lineQueryService.findByLineName(lineName);

        final List<Section> originSection = line.getSections();

        line.delete(new Station(stationDeleteRequest.getStationName()));

        final List<Section> updatedSection = line.getSections();

        updatedSection.stream()
                      .filter(it -> originSection.stream().noneMatch(it::isSame))
                      .findAny()
                      .ifPresentOrElse(
                              updateAndDeleteSection(originSection, updatedSection),
                              onlyDeleteSection(originSection, updatedSection)
                      );

        if (line.isDeleted()) {
            sectionCommandService.deleteAll(line.getId());
            lineCommandService.deleteLine(line.getId());
        }
    }

    private Consumer<Section> updateAndDeleteSection(
            final List<Section> originSection,
            final List<Section> updatedSection
    ) {
        return section -> {
            sectionCommandService.updateSection(section);

            originSection.stream()
                         .filter(origin -> updatedSection.stream().noneMatch(origin::isSame))
                         .filter(origin -> !origin.equals(section))
                         .findFirst()
                         .ifPresent(origin -> sectionCommandService.deleteSection(origin.getId()));
        };
    }

    private Runnable onlyDeleteSection(
            final List<Section> originSection,
            final List<Section> updatedSection
    ) {
        return () -> originSection.stream()
                                  .filter(it -> updatedSection.stream().noneMatch(it::isSame))
                                  .findFirst()
                                  .ifPresent(it -> sectionCommandService.deleteSection(it.getId()));
    }
}
