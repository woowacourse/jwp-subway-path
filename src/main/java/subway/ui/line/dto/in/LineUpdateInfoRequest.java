package subway.ui.line.dto.in;

import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineUpdateInfoRequest {

    @NotBlank
    private String name;
    @NotBlank
    private String color;
}
