package subway.shortestpathfinder.dto;

import subway.shortestpathfinder.domain.AgeGroupFeeCalculator;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class GetShortestPathRequest {
    @NotBlank(message = "startStationName은 null 또는 empty일 수 없습니다.")
    private String startStationName;
    @NotBlank(message = "endStationName은 null 또는 empty일 수 없습니다.")
    private String endStationName;
    @NotNull(message = "연령대는 CHILD, TEENAGER, ADULT 만 입력 가능합니다.")
    private AgeGroupFeeCalculator ageGroupFeeCalculator;
    
    public GetShortestPathRequest() {}
    
    public GetShortestPathRequest(
            final String startStationName,
            final String endStationName,
            final AgeGroupFeeCalculator ageGroupFeeCalculator
    ) {
        this.startStationName = startStationName;
        this.endStationName = endStationName;
        this.ageGroupFeeCalculator = ageGroupFeeCalculator;
    }
    
    public String getStartStationName() {
        return startStationName;
    }
    
    public String getEndStationName() {
        return endStationName;
    }
    
    public AgeGroupFeeCalculator getAgeGroupFeeCalculator() {
        return ageGroupFeeCalculator;
    }
    
    @Override
    public String toString() {
        return "GetShortestPathRequest{" +
                "startStationName='" + startStationName + '\'' +
                ", endStationName='" + endStationName + '\'' +
                ", ageGroupFeeCalculator=" + ageGroupFeeCalculator +
                '}';
    }
}
