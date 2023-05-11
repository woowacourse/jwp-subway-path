package subway.domain;

public class FinalStation {

    final String finalUpStationName;
    final String finalDownStationName;

    private FinalStation(final String finalUpStationName, final String finalDownStationName) {
        this.finalUpStationName = finalUpStationName;
        this.finalDownStationName = finalDownStationName;
    }

    public static FinalStation of(final String finalUpStationName, final String finalDownStationName) {
        return new FinalStation(finalUpStationName, finalDownStationName);
    }

    public boolean isNotFinalStation(final String stationName) {
        return !finalUpStationName.equals(stationName) && !finalDownStationName.equals(stationName);
    }

    public boolean isFinalUpStation(final String stationName) {
        return finalUpStationName.equals(stationName);
    }
}
