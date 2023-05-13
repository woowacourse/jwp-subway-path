package subway.station.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
public class Station {
    private final String name;
    
    public Station(final String name) {
        validateName(name);
        this.name = name;
    }
    
    private void validateName(final String name) {
        validateNullOrEmpty(name);
        validateForm(name);
    }
    
    private void validateNullOrEmpty(final String name) {
        if (Objects.isNull(name) || name.isBlank()) {
            throw new IllegalArgumentException("역 이름이 비어있습니다. name : " + name);
        }
    }
    
    private void validateForm(final String name) {
        if (!name.endsWith("역")) {
            throw new IllegalArgumentException("역 이름은 \"역\"으로 끝나야 합니다.");
        }
    }
}
