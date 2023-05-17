package subway.path.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GetShortestPathRequest {
    private String startStationName;
    private String endStationName;
}
