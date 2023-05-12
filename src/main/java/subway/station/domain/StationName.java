package subway.station.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public class StationName {
    private final String name;
    
    public StationName(final String name) {
        validateNullOrEmpty(name);
        this.name = name;
    }
    
    private void validateNullOrEmpty(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 역 이름이 비어있습니다. name : " + name);
        }
    }
}
