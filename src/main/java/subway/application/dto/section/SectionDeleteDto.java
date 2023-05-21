package subway.application.dto.section;

public class SectionDeleteDto {
    private long id;
    private String stationName;

    public SectionDeleteDto(long id, String stationName) {
        this.id = id;
        this.stationName = stationName;
    }

    public long getId() {
        return id;
    }

    public String getStationName() {
        return stationName;
    }
}
