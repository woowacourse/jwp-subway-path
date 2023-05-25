package subway.line.domain.fare.application;

import org.springframework.stereotype.Service;
import subway.line.domain.fare.Fare;
import subway.line.domain.section.application.SectionService;
import subway.line.domain.station.Station;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

@Service
public class SurchargeService {
    private final SectionService sectionService;
    private final SurchargeRepository surchargeRepository;

    public SurchargeService(SectionService sectionService, SurchargeRepository surchargeRepository) {
        this.sectionService = sectionService;
        this.surchargeRepository = surchargeRepository;
    }

    public Fare saveSurcharge(long lineId, Fare surcharge) {
        return surchargeRepository.saveSurcharge(lineId, surcharge);
    }

    public List<Fare> getSurcharges(List<Station> path) {
        final var surcharges = new ArrayList<Fare>();
        final var stations = new ArrayDeque<>(path);

        var startingStation = stations.poll();
        var destinationStation = stations.poll();

        while (destinationStation != null) {
            final var lineId = sectionService.findLineIdBySectionHavingStations(startingStation, destinationStation);
            if (surchargeRepository.hasLineSurcharge(lineId)) {
                surcharges.add(surchargeRepository.findByLineId(lineId));
            }
            startingStation = destinationStation;
            destinationStation = stations.poll();
        }

        return surcharges;
    }
}
