package subway.service.dto;

public class SectionCreateDto {

    private final Integer distance;
    private final String firstStation;
    private final String lastStation;

    public SectionCreateDto(final Integer distance, final String firstStation, final String lastStation) {
        this.distance = distance;
        this.firstStation = firstStation;
        this.lastStation = lastStation;
    }

    public Integer getDistance() {
        return distance;
    }

    public String getFirstStation() {
        return firstStation;
    }

    public String getLastStation() {
        return lastStation;
    }
}
