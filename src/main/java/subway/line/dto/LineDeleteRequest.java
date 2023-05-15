package subway.line.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LineDeleteRequest {
    @NotNull(message = "lineId는 null일 수 없습니다.")
    private Long lineId;
}
