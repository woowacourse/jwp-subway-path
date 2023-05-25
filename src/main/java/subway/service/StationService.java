package subway.service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.domain.line.Line;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.domain.station.Stations;
import subway.event.StationChange;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

@Service
@Transactional
public class StationService {

  private final SectionCommandService sectionCommandService;
  private final LineCommandService lineCommandService;
  private final LineQueryService lineQueryService;
  private final ApplicationEventPublisher applicationEventPublisher;

  public StationService(
      final SectionCommandService sectionCommandService,
      final LineCommandService lineCommandService,
      final LineQueryService lineQueryService,
      final ApplicationEventPublisher applicationEventPublisher) {
    this.sectionCommandService = sectionCommandService;
    this.lineCommandService = lineCommandService;
    this.lineQueryService = lineQueryService;
    this.applicationEventPublisher = applicationEventPublisher;
  }

  public void registerStation(final StationRegisterRequest stationRegisterRequest) {

    final String lineName = stationRegisterRequest.getLineName();
    final Line line = lineQueryService.searchByLineName(lineName);
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
        .filter(section -> originSection.stream().noneMatch(section::isSame))
        .filter(section -> Objects.nonNull(section.getId()))
        .findFirst()
        .ifPresent(sectionCommandService::updateSection);

    applicationEventPublisher.publishEvent(new StationChange());
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
    final Line line = lineQueryService.searchByLineName(lineName);

    final List<Section> originSection = line.getSections();

    line.delete(new Station(stationDeleteRequest.getStationName()));

    final List<Section> updatedSection = line.getSections();

    updatedSection.stream()
        .filter(section -> originSection.stream().noneMatch(section::isSame))
        .findAny()
        .ifPresentOrElse(
            updateAndDeleteSection(originSection, updatedSection),
            onlyDeleteSection(originSection, updatedSection)
        );

    if (line.isDeleted()) {
      sectionCommandService.deleteAll(line.getId());
      lineCommandService.deleteLine(line.getId());
    }

    applicationEventPublisher.publishEvent(new StationChange());
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
          .ifPresent(origin -> sectionCommandService.deleteSectionById(origin.getId()));
    };
  }

  private Runnable onlyDeleteSection(
      final List<Section> originSection,
      final List<Section> updatedSection
  ) {
    return () -> originSection.stream()
        .filter(section -> updatedSection.stream().noneMatch(section::isSame))
        .findFirst()
        .ifPresent(section -> sectionCommandService.deleteSectionById(section.getId()));
  }
}
