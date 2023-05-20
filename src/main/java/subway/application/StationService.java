package subway.application;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import subway.dao.SectionDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Sections;
import subway.domain.Station;
import subway.dto.request.StationUpdateRequest;
import subway.dto.response.StationResponse;

@Service
@Transactional
public class StationService {
	private final StationDao stationDao;
	private final SectionDao sectionDao;

	public StationService(final StationDao stationDao, final SectionDao sectionDao) {
		this.stationDao = stationDao;
		this.sectionDao = sectionDao;
	}

	public StationResponse createStation(final StationUpdateRequest stationUpdateRequest) {
		final List<StationResponse> stations = findAll();
		for (StationResponse station : stations) {
			if (station.getName().equals(stationUpdateRequest.getName())) {
				throw new IllegalArgumentException("이미 존재하는 역입니다");
			}
		}
		final Station station = new Station(stationUpdateRequest.getName());

		final long stationId = stationDao.createStation(station);
		return new StationResponse(stationId, station.getName());
	}

	public List<StationResponse> findAll() {
		return StationResponse.of(stationDao.findAll());
	}

	public StationResponse findById(final Long stationId) {
		final Station station = stationDao.findById(stationId);
		return new StationResponse(stationId, station.getName());
	}

	public StationResponse updateStation(final long stationId, final StationUpdateRequest request) {
		final boolean isUpdated = stationDao.updateStation(stationId, new Station(request.getName()));

		if (!isUpdated) {
			throw new IllegalStateException("역 갱신에 실패했습니다");
		}

		return new StationResponse(stationId, request.getName());
	}

	public long deleteById(final Long stationId) {
		final Station station = stationDao.findById(stationId);
		final boolean isDelete = stationDao.deleteById(stationId);

		if (!isDelete) {
			throw new NullPointerException("삭제할 역이 존재하지 않습니다");
		}

		final Sections sections = new Sections(sectionDao.findAll());
		final Map<Line, List<Section>> mergedSections = sections.remove(station);
		mergedSections.keySet()
			.forEach(line -> sectionDao.createSection(line.getName(), mergedSections.get(line)));

		return stationId;
	}
}
