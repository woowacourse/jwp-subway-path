package subway.domain;

public class PairStation {
    private Station preStation;
    private Station Station;

    public PairStation(subway.domain.Station preStation, subway.domain.Station station) {
        this.preStation = preStation;
        Station = station;
    }

    public subway.domain.Station getPreStation() {
        return preStation;
    }

    public subway.domain.Station getStation() {
        return Station;
    }
}
