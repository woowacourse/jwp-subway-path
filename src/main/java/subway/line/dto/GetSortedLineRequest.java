package subway.line.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GetSortedLineRequest {
    @NotNull(message = "lineId는 null일 수 없습니다.")
    private Long lineId;
}
