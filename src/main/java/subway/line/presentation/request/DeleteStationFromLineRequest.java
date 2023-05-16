package subway.line.presentation.request;

import javax.validation.constraints.NotBlank;
import subway.line.application.dto.DeleteStationFromLineCommand;

public class DeleteStationFromLineRequest {

    @NotBlank(message = "노선 이름을 입력해야 합니다")
    private String lineName;

    @NotBlank(message = "제거할 역 이름을 입력해야 합니다")
    private String deleteStationName;

    private DeleteStationFromLineRequest() {
    }

    public DeleteStationFromLineRequest(final String lineName, final String deleteStationName) {
        this.lineName = lineName;
        this.deleteStationName = deleteStationName;
    }

    public DeleteStationFromLineCommand toCommand() {
        return new DeleteStationFromLineCommand(lineName, deleteStationName);
    }

    public String getLineName() {
        return lineName;
    }

    public String getDeleteStationName() {
        return deleteStationName;
    }
}
