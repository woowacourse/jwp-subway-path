package subway.station.dto;

import lombok.*;
import subway.section.domain.Section;
import subway.station.domain.Station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class InitAddStationRequest {
    @NotBlank (message = "역 이름은 null 또는 빈값이 올 수 없습니다.")
    private String firstStation;
    @NotBlank (message = "역 이름은 null 또는 빈값이 올 수 없습니다.")
    private String secondStation;
    @NotNull(message = "거리는 null일 수 없습니다.")
    @Positive(message = "거리는 양수여야 합니다.")
    private Long distance;
    @NotNull(message = "lineId는 null일 수 없습니다.")
    private Long lineId;
    
    public List<Station> toEntities() {
        return List.of(new Station(firstStation), new Station(secondStation));
    }
    
    public Section toSectionEntity() {
        return new Section(firstStation, secondStation, distance);
    }
}
