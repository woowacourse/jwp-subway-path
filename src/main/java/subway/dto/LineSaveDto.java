package subway.dto;

public class LineSaveDto {
    private String lineName;

    private LineSaveDto() {
    }

    public LineSaveDto(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

}
