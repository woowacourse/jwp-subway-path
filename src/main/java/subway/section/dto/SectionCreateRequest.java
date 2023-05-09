package subway.section.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SectionCreateRequest {

    private Long lineId;
    private Long baseId;
    private Long addedId;
    private Boolean direction;
    private Integer distance;
}
