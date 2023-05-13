package subway.domain.station;

import java.util.Objects;
import lombok.Getter;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import subway.exception.BusinessException;

@Getter
public class Station {

    private static final int MAX_STATION_NAME_LENGTH = 20;

    private final Long id;
    private final String name;

    public Station(@Nullable final Long id, final String name) {
        validateName(name);
        this.id = id;
        this.name = name;
    }

    public Station(final String name) {
        this(null, name);
    }

    private void validateName(final String name) {
        if (!StringUtils.hasText(name)) {
            throw new BusinessException("역 이름이 공백입니다. 글자를 입력해주세요");
        }
        if (name.length() > MAX_STATION_NAME_LENGTH) {
            throw new BusinessException("역 이름이 " + MAX_STATION_NAME_LENGTH + "글자를 초과했습니다");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Station station = (Station) o;
        return Objects.equals(getId(), station.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
