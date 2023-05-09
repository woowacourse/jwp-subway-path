package subway.entity;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationEntity {

    private final Long id;
    private final String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StationEntity stationEntity = (StationEntity) o;
        return id.equals(stationEntity.id) && name.equals(stationEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
