package subway.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LineCreateRequest {

    @NotBlank(message = "노선을 입력해주세요.")
    private String lineName;

    @NotBlank(message = "상행역 입력해주세요.")
    private String upTerminalName;

    @NotBlank(message = "하행역 입력해주세요.")
    private String downTerminalName;

    @NotNull(message = "역간의 거리를 입력해주세요.")
    private Integer distance;

    @NotNull(message = "추가 요금을 입력해주세요")
    private Integer additionalFee;

    private LineCreateRequest() {
    }

    public LineCreateRequest(String lineName, String upTerminalName, String downTerminalName, Integer distance,
                             Integer additionalFee) {
        this.lineName = lineName;
        this.upTerminalName = upTerminalName;
        this.downTerminalName = downTerminalName;
        this.distance = distance;
        this.additionalFee = additionalFee;
    }

    public String getLineName() {
        return lineName;
    }

    public String getUpTerminalName() {
        return upTerminalName;
    }

    public String getDownTerminalName() {
        return downTerminalName;
    }

    public Integer getDistance() {
        return distance;
    }

    public Integer getAdditionalFee() {
        return additionalFee;
    }
}
