package subway.presentation.request;

import javax.validation.constraints.NotBlank;
import subway.application.dto.DeleteStationFromLineCommand;

public class DeleteStationFromLineRequest {

    @NotBlank(message = "노선을 입력해주세요.")
    private String lineName;

    @NotBlank(message = "역을 입력해주세요.")
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
