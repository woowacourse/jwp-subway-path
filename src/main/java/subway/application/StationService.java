package subway.application;

import org.springframework.stereotype.Service;

import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.ui.dto.StationCreateRequest;
import subway.ui.dto.StationResponse;

import java.util.List;

@Service
public class StationService {
	private final StationRepository stationRepository;

	public StationService(final StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public StationResponse createStation(final StationCreateRequest stationCreateRequest) {
		final List<StationResponse> stations = findAll();
		for (StationResponse station : stations) {
			if (station.getName().equals(stationCreateRequest.getName())) {
				throw new IllegalArgumentException("이미 존재하는 역입니다");
			}
		}
		final Station station = new Station(stationCreateRequest.getName());

		final long stationId = stationRepository.createStation(station);
		return new StationResponse(stationId, station.getName());
	}

	public List<StationResponse> findAll() {
		return StationResponse.of(stationRepository.findAll());
	}

	public StationResponse findById(final Long stationIdRequest) {
		final Station station = stationRepository.findById(stationIdRequest);
		return new StationResponse(stationIdRequest, station.getName());
	}

	public void deleteById(final Long stationIdRequest) {
		stationRepository.deleteById(stationIdRequest);
	}
}
