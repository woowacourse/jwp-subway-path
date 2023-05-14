package subway.application;

import org.springframework.stereotype.Service;
import subway.domain.Line;
import subway.domain.ShortestWayCalculator;
import subway.domain.Station;
import subway.dto.ShortestWayResponse;
import subway.dto.StationResponse;
import subway.persistence.repository.SubwayRepository;

import java.util.List;

@Service
public class FeeService {

    private final SubwayRepository subwayRepository;
    private final FeePolicy feePolicy;

    public FeeService(final SubwayRepository subwayRepository, final FeePolicy feePolicy) {
        this.subwayRepository = subwayRepository;
        this.feePolicy = feePolicy;
    }

    public ShortestWayResponse showShortestWay(final Long startStationId, final Long endStationId) {
        final List<Line> lines = subwayRepository.findLines();
        final Station start = subwayRepository.findStationById(startStationId);
        final Station end = subwayRepository.findStationById(endStationId);
        
        final ShortestWayCalculator calculator = ShortestWayCalculator.from(start, end, lines);
        final int fee = feePolicy.calculateFee(calculator.getDistance());

        return new ShortestWayResponse(fee, StationResponse.of(calculator.getWay()));
    }
}
