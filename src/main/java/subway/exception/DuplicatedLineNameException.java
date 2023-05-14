package subway.exception;

public class DuplicatedLineNameException extends RuntimeException {

    public DuplicatedLineNameException(String name) {
        super("이미 같은이름의 역이 있습니다. 입력된 역 : " + name);
    }

}
