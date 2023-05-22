package subway.domain;

import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.fare.Fare;

public class LineInfo {

    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_COLOR_LENGTH = 20;
    private static final int DEFAULT_SURCHARGE = 0;

    private final String name;
    private final String color;
    private final Fare surcharge;

    public LineInfo(final String name, final String color, Integer surcharge) {
        validateName(name);
        validateColor(color);
        if (surcharge == null) {
            surcharge = DEFAULT_SURCHARGE;
        }
        validateSurcharge(surcharge);
        this.name = name.strip();
        this.color = color.strip();
        this.surcharge = new Fare(surcharge);
    }

    private void validateName(final String name) {
        if (name == null || name.isBlank()) {
            throw new SubwayIllegalArgumentException("이름은 비어있을 수 없습니다.");
        }
        if (name.strip().length() > MAX_NAME_LENGTH) {
            throw new SubwayIllegalArgumentException("이름은 " + MAX_NAME_LENGTH + "자 이하여야합니다.");
        }
    }

    private void validateColor(final String color) {
        if (color == null || color.isBlank()) {
            throw new SubwayIllegalArgumentException("색상은 비어있을 수 없습니다.");
        }
        if (color.strip().length() > MAX_COLOR_LENGTH) {
            throw new SubwayIllegalArgumentException("색상은 " + MAX_COLOR_LENGTH + "자 이하여야합니다.");
        }
    }

    private void validateSurcharge(final int surcharge) {
        if (surcharge < DEFAULT_SURCHARGE) {
            throw new SubwayIllegalArgumentException("노선 추가 요금은 " + DEFAULT_SURCHARGE + "원 이상이어야합니다.");
        }
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Fare getSurcharge() {
        return surcharge;
    }
}
