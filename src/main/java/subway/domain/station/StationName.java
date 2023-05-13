package subway.domain.station;


import java.util.Objects;
import org.springframework.util.StringUtils;
import subway.exception.station.StationNameException;

public class StationName {

    private static final int MAX_STATION_NAME_LENGTH = 20;

    private final String value;

    public StationName(final String value) {
        validate(value);
        this.value = value;
    }

    private void validate(final String value) {
        if (!StringUtils.hasText(value)) {
            throw new StationNameException("역 이름이 공백입니다. 글자를 입력해주세요");
        }
        if (value.length() > MAX_STATION_NAME_LENGTH) {
            throw new StationNameException("역 이름이 " + MAX_STATION_NAME_LENGTH + "글자를 초과했습니다");
        }
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final StationName that = (StationName) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "StationName{" +
            "value='" + value + '\'' +
            '}';
    }
}
