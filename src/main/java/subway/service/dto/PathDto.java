package subway.service.dto;

public class PathDto {

    public final String sourceStation;
    public final String targetStation;

    public PathDto(final String sourceStation, final String targetStation) {
        this.sourceStation = sourceStation;
        this.targetStation = targetStation;
    }

    public String getSourceStation() {
        return sourceStation;
    }

    public String getTargetStation() {
        return targetStation;
    }
}
