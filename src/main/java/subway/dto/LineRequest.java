package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineRequest {
    @NotBlank(message = "노선 이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "노선 색깔을 입력해주세요.")
    private String color;
}
