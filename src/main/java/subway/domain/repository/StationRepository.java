package subway.domain.repository;

import subway.domain.Station;

import java.util.List;

public interface StationRepository {
	long createStation(final Station station);

	List<Station> findAll();

	Station findById(Long stationIdRequest);

	boolean updateStation(long stationId, Station station);

	boolean deleteById(Long stationId);

	Station findStationWithId(Station station);
}
