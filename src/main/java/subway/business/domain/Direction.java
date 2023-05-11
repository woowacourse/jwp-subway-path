package subway.business.domain;

import java.util.Arrays;

public enum Direction {
    //TODO Presentation 계층에서 Mapping하는 책임 가지고, text 없애도록 수정
    UPWARD("상행"),
    DOWNWARD("하행");

    private final String text;

    Direction(String text) {
        this.text = text;
    }

    public static Direction from(String text) {
        return Arrays.stream(Direction.values())
                .filter(direction -> direction.text.equals(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(String.format(
                        "Direction은 상행 또는 하행만 입력할 수 있습니다." + System.lineSeparator() +
                                "입력한 Direction : %s", text)
                ));
    }
}
