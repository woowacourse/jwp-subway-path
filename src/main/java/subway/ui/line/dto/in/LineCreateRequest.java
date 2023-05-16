package subway.ui.line.dto.in;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineCreateRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
    @Positive
    @NotNull
    private Long upStationId;
    @Positive
    @NotNull
    private Long downStationId;
    @Positive
    private Long distance;
}
