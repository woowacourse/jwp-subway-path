package subway.dto;

public enum CreateType {
    INIT("init"),
    UP("up"),
    DOWN("down"),
    MID("mid");

    private final String value;

    CreateType(String value) {
        this.value = value;
    }
}
