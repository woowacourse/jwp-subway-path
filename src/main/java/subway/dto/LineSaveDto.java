package subway.dto;

public class LineSaveDto {
    private String lineName;

    public LineSaveDto() {
    }

    public LineSaveDto(String lineName) {
        this.lineName = lineName;
    }

    public String getLineName() {
        return lineName;
    }

}
