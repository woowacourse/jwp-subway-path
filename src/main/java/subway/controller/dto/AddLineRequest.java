package subway.controller.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AddLineRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @NotBlank
    private String frontStationName;

    @NotBlank
    private String backStationName;

    @Positive
    private Long distance;
}
