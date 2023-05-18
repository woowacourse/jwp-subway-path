package subway.application;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.domain.repository.SectionRepository;
import subway.domain.repository.StationRepository;
import subway.ui.dto.request.StationUpdateRequest;
import subway.ui.dto.response.StationResponse;

@Service
@Transactional
public class StationService {
	private final StationRepository stationRepository;
	private final SectionRepository sectionRepository;

	public StationService(final StationRepository stationRepository, final SectionRepository sectionRepository) {
		this.stationRepository = stationRepository;
		this.sectionRepository = sectionRepository;
	}

	public StationResponse createStation(final StationUpdateRequest stationUpdateRequest) {
		final List<StationResponse> stations = findAll();
		for (StationResponse station : stations) {
			if (station.getName().equals(stationUpdateRequest.getName())) {
				throw new IllegalArgumentException("이미 존재하는 역입니다");
			}
		}
		final Station station = new Station(stationUpdateRequest.getName());

		final long stationId = stationRepository.createStation(station);
		return new StationResponse(stationId, station.getName());
	}

	public List<StationResponse> findAll() {
		return StationResponse.of(stationRepository.findAll());
	}

	public StationResponse findById(final Long stationId) {
		final Station station = stationRepository.findById(stationId);
		return new StationResponse(stationId, station.getName());
	}

	public StationResponse updateStation(final long stationId, final StationUpdateRequest request) {
		final boolean isUpdated = stationRepository.updateStation(stationId, new Station(request.getName()));

		if (!isUpdated) {
			throw new IllegalStateException("역 갱신에 실패했습니다");
		}

		return new StationResponse(stationId, request.getName());
	}

	public long deleteById(final Long stationId) {
		final Station station = stationRepository.findById(stationId);
		final boolean isDelete = stationRepository.deleteById(stationId);

		if (!isDelete) {
			throw new NullPointerException("삭제할 역이 존재하지 않습니다");
		}

		final Sections sections = new Sections(sectionRepository.findAll());
		final Map<Line, List<Section>> mergedSections = sections.remove(station);
		mergedSections.keySet()
			.forEach(line -> sectionRepository.createSection(line.getName(), mergedSections.get(line)));

		return stationId;
	}
}
