package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.SubwayMapRepository;
import subway.domain.SubwayMap;
import subway.dto.*;

@Service
public class LineStationService {

    private final SubwayMapRepository subwayMapRepository;

    public LineStationService(SubwayMapRepository subwayMapRepository) {
        this.subwayMapRepository = subwayMapRepository;
    }

    public void connectInitStations(InitConnectDto dto) {
        final SubwayMap subwayMap = subwayMapRepository.find();

        subwayMap.connectInitStations(dto.getLineId(), dto.getSourceStationId(), dto.getTargetStationId(), dto.getDistance());

        subwayMapRepository.save(subwayMap);
    }

    public void connectUpEndpoint(ConnectEndpointDto dto) {
        final SubwayMap subwayMap = subwayMapRepository.find();

        subwayMap.connectUpEndpoint(dto.getLineId(), dto.getStationId(), dto.getDistance());

        subwayMapRepository.save(subwayMap);
    }

    public void connectDownEndpoint(ConnectEndpointDto dto) {
        final SubwayMap subwayMap = subwayMapRepository.find();

        subwayMap.connectDownEndpoint(dto.getLineId(), dto.getStationId(), dto.getDistance());

        subwayMapRepository.save(subwayMap);
    }

    public void connectMidStation(Long lineId, Long stationId, ConnectRequest request) {
        // TODO dto 변경하기
        final SubwayMap subwayMap = subwayMapRepository.find();

        subwayMap.connectMidStation(lineId, stationId, request.getPrevStationId(), request.getNextStationId(), request.getDistance());

        subwayMapRepository.save(subwayMap);
    }
}
