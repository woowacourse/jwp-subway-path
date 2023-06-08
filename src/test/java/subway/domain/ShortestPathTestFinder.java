package subway.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.exception.InvalidException;
import subway.exception.NoSuchPath;
import subway.repository.ShortestPathFinder;

@SuppressWarnings("NonAsciiCharacters")
class ShortestPathTest {
    private PathFinder pathFinder;
    private Station 잠실새내역;
    private Station 잠실역;
    private Station 잠실나루역;


    @BeforeEach
    public void setUp() {
        잠실새내역 = new Station(1L, "잠실새내역");
        잠실역 = new Station(2L, "잠실역");
        잠실나루역 = new Station(3L, "잠실나루역");

        Section section1 = new Section(잠실새내역, 잠실역, 100);
        Section section2 = new Section(잠실역, 잠실나루역, 1);
        Section section3 = new Section(잠실나루역, 잠실새내역, 1);

        List<Section> sections = List.of(section1, section2, section3);

        pathFinder = ShortestPathFinder.of(sections);
    }

    @DisplayName("최단 경로에 구하고 포함된 역들을 반환한다.")
    @Test
    void findPath() {
        // when
        Path shortestPath = pathFinder.findPath(잠실새내역, 잠실역);

        // then
        assertThat(shortestPath.getPath()).isEqualTo(List.of(잠실새내역, 잠실나루역, 잠실역));
    }

    @DisplayName("경로를 구할 수 없는 경우 예외가 발생한다.")
    @Test
    void findPathNoSuchPath() {
        // given
        Station 찰리역 = new Station(4L, "찰리역");
        Section section1 = new Section(잠실역, 잠실새내역, 10);
        Section section2 = new Section(찰리역, 잠실나루역, 10);
        List<Section> sections = List.of(section1, section2);
        pathFinder = ShortestPathFinder.of(sections);

        // then
        assertThatThrownBy(() -> pathFinder.findPath(잠실역, 찰리역))
                .isInstanceOf(NoSuchPath.class)
                .hasMessage("잠실역 - 찰리역은 존재하지 않는 경로입니다.");
    }

    @DisplayName("시작역과 도착역이 같은 경우 예외가 발생한다")
    @Test
    void findPathWithSameStation() {
        // then
        assertThatThrownBy(() -> pathFinder.findPath(잠실역, 잠실역))
                .isInstanceOf(InvalidException.class)
                .hasMessage("시작역과 도착역은 같을 수 없습니다.");
    }

    @DisplayName("최단 경로에 구하고 거리를 반환한다.")
    @Test
    void getDistance() {
        // when
        Path path = pathFinder.findPath(잠실새내역, 잠실역);

        // then
        assertThat(path.getDistance()).isEqualTo(2);
    }
}
