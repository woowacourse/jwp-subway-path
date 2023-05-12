package subway.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class AddStationRequest {

    @NotNull
    private Boolean isEnd;

    @NotBlank
    private String lineName;

    @NotBlank
    private String frontStation;

    @NotBlank
    private String backStation;

    @NotBlank
    private String stationName;

    @NotNull
    @Positive
    private Long distance;
}
