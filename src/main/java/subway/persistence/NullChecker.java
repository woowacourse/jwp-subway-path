package subway.persistence;

public class NullChecker {

    public static boolean isNull(Object object) {
        if (object != null) {
            return true;
        }
        throw new IllegalArgumentException("[ERROR] 영속화 계층 명령 수행 시 필요한 데이터가 null입니다.");
    }
}
