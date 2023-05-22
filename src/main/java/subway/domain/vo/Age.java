package subway.domain.vo;

import subway.exception.BusinessException;

public class Age {

    private final int value;

    public Age(final int value) {
        validate(value);
        this.value = value;
    }

    public static Age from(final int value) {
        return new Age(value);
    }

    private void validate(final int value) {
        if (value < 0) {
            throw new BusinessException("나이는 음수가 될 수 없습니다.");
        }
    }

    public boolean isLessThan(final Age age) {
        return value <= age.value;
    }

    public boolean isMoreThan(final Age age) {
        return value >= age.value;
    }
}
