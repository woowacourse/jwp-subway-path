package subway.domain.section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import subway.domain.station.Station;

public class Section {

    private static final String NOT_CONNECTED_STATION_MESSAGE = "인접하지 않은 역 입니다.";
    private static final int END_STATION_PATH_SIZE = 1;

    private final Map<Station, SectionInfo> sectionInfos;

    public Section(final Map<Station, SectionInfo> sectionInfos) {
        this.sectionInfos = sectionInfos;
    }

    public static Section create() {
        return new Section(new HashMap<>());
    }

    public void add(final Station station, final Distance distance, final Direction direction) {
        sectionInfos.put(station, SectionInfo.of(distance, direction));
    }

    public void delete(final Station station) {
        validateConnectedStation(station);

        sectionInfos.remove(station);
    }

    public Distance calculateMiddleDistance(final Station targetStation, final Distance distance) {
        validateConnectedStation(targetStation);
        final SectionInfo targetSectionInfo = sectionInfos.get(targetStation);

        return targetSectionInfo.calculateMiddleDistance(distance);
    }

    public Direction findDirectionByStation(final Station station) {
        validateConnectedStation(station);

        return sectionInfos.get(station)
                .direction();
    }

    public Distance findDistanceByStation(final Station station) {
        validateConnectedStation(station);

        return sectionInfos.get(station)
                .getDistance();
    }

    private void validateConnectedStation(final Station station) {
        if (!isConnect(station)) {
            throw new IllegalArgumentException(NOT_CONNECTED_STATION_MESSAGE);
        }
    }

    public Direction findEndStationPathDirection() {
        if (!isTerminalStation()) {
            throw new IllegalArgumentException("해당 역은 종점역이 아닙니다.");
        }

        return sectionInfos.values().stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 노선에 등록된 역이 없습니다."))
                .direction();
    }

    public Optional<Station> findStationByDirection(final Direction direction) {
        return sectionInfos.keySet()
                .stream()
                .filter(station -> sectionInfos.get(station).matchesByDirection(direction))
                .findAny();
    }

    public List<Station> findAllStation() {
        return new ArrayList<>(sectionInfos.keySet());
    }

    public boolean isTerminalStation() {
        return sectionInfos.keySet().size() == END_STATION_PATH_SIZE;
    }

    public boolean isConnect(final Station station) {
        return sectionInfos.containsKey(station);
    }
}
