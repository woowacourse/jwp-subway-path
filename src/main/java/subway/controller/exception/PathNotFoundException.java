package subway.controller.exception;

public class PathNotFoundException extends IllegalArgumentException {
    public PathNotFoundException() {
        super("두 역 사이의 경로가 존재하지 않습니다");
    }
}
