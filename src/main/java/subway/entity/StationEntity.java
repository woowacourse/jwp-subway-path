package subway.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StationEntity {
    private Long id;
    private String name;

    public StationEntity(String name) {
        this(null, name);
    }
}
