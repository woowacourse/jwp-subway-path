package subway.domain;

public class Section {
    private final Station upStation;
    private final Station downStation;
    private final Distance distance;

    public Section(Station upStation, Station downStation, Distance distance) {
        validate(upStation, downStation);

        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validate(Station upStation, Station downStation) {
        validateBothEmptyStation(upStation, downStation);
        validateSameStations(upStation, downStation);
    }

    private void validateBothEmptyStation(Station upStation, Station downStation) {
        if (upStation.equals(Station.emptyStation) && downStation.equals(Station.emptyStation)) {
            throw new IllegalArgumentException("상행역과 하행역이 모두 비어있을 수 없습니다");
        }
    }

    private void validateSameStations(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException("상행역과 하행역은 같은 이름을 가질 수 없습니다.");
        }
    }

    public boolean isIncludeEmptyStation() {
        if (upStation.equals(Station.emptyStation) || downStation.equals(Station.emptyStation)) {
            return true;
        }
        return false;
    }
}
