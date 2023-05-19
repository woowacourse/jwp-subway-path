package subway.application.dto.section;

public class SectionDeleteDto {
    private Long id;
    private String stationName;

    public SectionDeleteDto(Long id, String stationName) {
        this.id = id;
        this.stationName = stationName;
    }

    public Long getId() {
        return id;
    }

    public String getStationName() {
        return stationName;
    }
}
