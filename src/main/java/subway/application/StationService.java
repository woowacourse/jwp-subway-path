package subway.application;

import org.springframework.stereotype.Service;

import subway.domain.Station;
import subway.domain.repository.StationRepository;
import subway.ui.dto.request.StationRequest;
import subway.ui.dto.response.StationResponse;

import java.util.List;

@Service
public class StationService {
	private final StationRepository stationRepository;

	public StationService(final StationRepository stationRepository) {
		this.stationRepository = stationRepository;
	}

	public StationResponse createStation(final StationRequest stationRequest) {
		final List<StationResponse> stations = findAll();
		for (StationResponse station : stations) {
			if (station.getName().equals(stationRequest.getName())) {
				throw new IllegalArgumentException("이미 존재하는 역입니다");
			}
		}
		final Station station = new Station(stationRequest.getName());

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

	public StationResponse updateStation(final long stationId, final StationRequest request) {
		final boolean isUpdated = stationRepository.updateStation(stationId, new Station(request.getName()));

		if (!isUpdated) {
			throw new IllegalStateException("역 갱신에 실패했습니다");
		}

		return new StationResponse(stationId, request.getName());
	}

	public long deleteById(final Long stationIdRequest) {
		final boolean isDelete = stationRepository.deleteById(stationIdRequest);

		if (!isDelete) {
			throw new NullPointerException("역 삭제에 실패했습니다");
		}
		return stationIdRequest;
	}
}
