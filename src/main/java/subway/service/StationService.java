package subway.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.subway.Station;
import subway.dto.request.StationCreateRequest;
import subway.dto.request.StationUpdateRequest;
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

	@Transactional(readOnly = true)
	public StationResponse findStationResponseById(final Long id) {
		Station station = stationRepository.findByStationId(id);
		return StationResponse.from(station);
	}

	@Transactional
	public void updateStation(final Long id, final StationUpdateRequest stationUpdateRequest) {
		Station station = stationRepository.findByStationId(id);
		station.update(stationUpdateRequest.getName());
		stationRepository.update(id, station);
	}

	@Transactional
	public void deleteStationById(final Long id) {
		stationRepository.deleteById(id);
	}
}
