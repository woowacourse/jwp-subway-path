package subway.business.domain.transfer;

import subway.business.domain.line.Station;

public class Transfer {
    private final Long id;
    private final Station firstStation;
    private final Station lastStation;

    public Transfer(Station station1, Station station2) {
        this(null, station1, station2);
    }

    public Transfer(Long id, Station station1, Station station2) {
        this.id = id;
        validateSameStation(station1, station2);
        if (station1.getId() < station2.getId()) {
            this.firstStation = station1;
            this.lastStation = station2;
            return;
        }
        this.firstStation = station2;
        this.lastStation = station1;
    }

    private void validateSameStation(Station station1, Station station2) {
        if (station1.equals(station2)) {
            throw new IllegalArgumentException(String.format(
                    "같은 역끼리 환승 구간으로 지정할 수 없습니다. "
                            + "(입력한 역 : %s)", station1.getName()));
        }
    }

    public Long getId() {
        return id;
    }

    public Station getFirstStation() {
        return firstStation;
    }

    public Station getLastStation() {
        return lastStation;
    }
}
