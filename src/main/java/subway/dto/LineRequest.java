package subway.dto;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LineRequest {
    @NotBlank(message = "호선명은 빈 문자열일 수 없습니다.")
    private String name;

    @NotBlank(message = "호선의 색상은 빈 문자열일 수 없습니다.")
    private String color;
}
