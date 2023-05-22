package subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.subway.Station;
import subway.dto.request.StationCreateRequest;
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
}
