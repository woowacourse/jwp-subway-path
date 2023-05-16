package subway.domain.station;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.util.StringUtils;
import subway.domain.station.exception.StationNameException;

@Getter
@ToString
@EqualsAndHashCode(of = "value")
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
}
