package subway.section.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionRequest {

    private Long lineId;
    private Long defaultId;
    private Long updateId;
    private Boolean direction;
    private Integer distance;
}
