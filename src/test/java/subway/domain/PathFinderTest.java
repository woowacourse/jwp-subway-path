package subway.domain;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathFinderTest {

    @Test
    @DisplayName("모든 구간 정보를 가지고 있는 경로 조회 객체를 생성한다.")
    void createPathFinder() {
        // given
        Sections sections = new Sections(List.of(
                new Section(1L, new Station("미금역"), new Station("정자역"), 3),
                new Section(2L, new Station("정자역"), new Station("수내역"), 4),
                new Section(3L, new Station("수내역"), new Station("서현역"), 5),
                new Section(4L, new Station("서현역"), new Station("이매역"), 4)
        ));

        // when, then
        assertDoesNotThrow(() -> PathFinder.from(sections));
    }
}