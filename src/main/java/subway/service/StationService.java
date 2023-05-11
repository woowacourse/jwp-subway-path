package subway.service;

import org.springframework.stereotype.Service;
import subway.dao.LineEntity;
import subway.dao.SectionDao;
import subway.dao.SectionEntity;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.domain.Stations;
import subway.service.dto.StationDeleteRequest;
import subway.service.dto.StationRegisterRequest;

import java.util.stream.Collectors;

@Service
public class StationService {

    private final SectionDao sectionDao;

    private final CommonService commonService;

    public StationService(
            final SectionDao sectionDao,
            final CommonService commonService
    ) {
        this.sectionDao = sectionDao;
        this.commonService = commonService;
    }

    public void registerStation(final StationRegisterRequest stationRegisterRequest) {

        final String lineName = stationRegisterRequest.getLineName();

        final Line line = commonService.mapToLineFrom(lineName);
        line.add(mapToSectionFrom(stationRegisterRequest));

        updateLine(commonService.getLineEntity(lineName), line);
    }

    private void updateLine(final LineEntity lineEntity, final Line line) {
        sectionDao.deleteAll(lineEntity.getId());

        sectionDao.batchSave(
                line.getSections()
                    .stream()
                    .map(it -> new SectionEntity(
                            it.getStations().getCurrent().getName(),
                            it.getStations().getNext().getName(),
                            it.getStations().getDistance(),
                            lineEntity.getId())
                    )
                    .collect(Collectors.toList())
        );
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
        final LineEntity lineEntity = commonService.getLineEntity(lineName);
        final Line line = commonService.mapToLineFrom(lineName);

        line.delete(new Station(stationDeleteRequest.getStationName()));

        updateLine(lineEntity, line);
    }
}
