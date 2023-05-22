package subway.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.subway.Station;
import subway.dto.request.StationCreateRequest;
import subway.dto.response.StationResponse;
import subway.dto.response.StationsResponse;
import subway.repository.StationRepository;

@Service
public class StationService {

	private final StationRepository stationRepository;

	public StationService(final StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	@Transactional
	public long saveStation(final StationCreateRequest stationCreateRequest) {
		return stationRepository.insertStation(new Station(stationCreateRequest.getName()));
	}

	@Transactional(readOnly = true)
	public StationsResponse findAllStationResponses() {
		return stationRepository.findAll().stream()
			.map(StationResponse::from)
			.collect(Collectors.collectingAndThen(Collectors.toList(), StationsResponse::from));
	}

}
