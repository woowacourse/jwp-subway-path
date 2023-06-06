package subway.domain.subwaymap;

import java.util.Arrays;

public enum SectionDirection {
    UP("상행"),
    DOWN("하행");

    private final String message;

    SectionDirection(final String message) {
        this.message = message;
    }

    public static SectionDirection get(final String find) {
        return Arrays.stream(values())
            .filter((sectionDirection -> sectionDirection.message.equals(find)))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("구간 진행 방향이 존재하지 않습니다. ( " + find + " )"));
    }
}
