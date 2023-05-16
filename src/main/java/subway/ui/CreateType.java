package subway.ui;

import java.util.Arrays;

public enum CreateType {
    INIT("init"),
    UP("up"),
    DOWN("down"),
    MID("mid");

    private final String value;

    CreateType(String value) {
        this.value = value;
    }

    public static CreateType from(String type) {
        return Arrays.stream(values())
                .filter(createType -> createType.value.equals(type))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 생성 타입입니다."));
    }
}
