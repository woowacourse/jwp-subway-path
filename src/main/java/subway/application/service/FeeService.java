package subway.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.feecalculator.FeeCalculator;
import subway.domain.Line;
import subway.domain.ShortestWayCalculator;
import subway.domain.Station;
import subway.dto.response.ShortestWayResponse;
import subway.dto.response.StationResponse;
import subway.persistence.repository.SubwayRepository;

import java.util.List;

@Transactional
@Service
public class FeeService {

    private final SubwayRepository subwayRepository;
    private final FeeCalculator feeCalculator;

    public FeeService(final SubwayRepository subwayRepository, final FeeCalculator feeCalculator) {
        this.subwayRepository = subwayRepository;
        this.feeCalculator = feeCalculator;
    }

    @Transactional(readOnly = true)
    public ShortestWayResponse showShortestWay(final Long startStationId, final Long endStationId) {
        final List<Line> lines = subwayRepository.findLines();
        final Station start = subwayRepository.findStationById(startStationId);
        final Station end = subwayRepository.findStationById(endStationId);

        final ShortestWayCalculator calculator = new ShortestWayCalculator().calculate(start, end, lines);
        final int fee = feeCalculator.calculateFee(calculator.getDistance());

        return new ShortestWayResponse(fee, StationResponse.of(calculator.getWay()));
    }
}
