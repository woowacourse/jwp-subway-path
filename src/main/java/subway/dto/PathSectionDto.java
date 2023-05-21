package subway.dto;

public class PathSectionDto {

    private final StationFindDto currentStation;
    private final StationFindDto nextStation;

    private final boolean isTransferSection;
    private final int distance;

    public PathSectionDto(final StationFindDto currentStation, final StationFindDto nextStation,
                          final boolean isTransferSection, final int distance) {
        this.currentStation = currentStation;
        this.nextStation = nextStation;
        this.isTransferSection = isTransferSection;
        this.distance = distance;
    }

    public StationFindDto getCurrentStation() {
        return currentStation;
    }

    public StationFindDto getNextStation() {
        return nextStation;
    }

    public boolean isTransferSection() {
        return isTransferSection;
    }

    public int getDistance() {
        return distance;
    }
}
