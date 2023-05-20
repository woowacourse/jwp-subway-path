package subway.domain.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class JgraphtPathTest {

    private JgraphtPath path;
    private Line firstLine = new Line(1L, "2호선", "green");
    private Line secondLine = new Line(2L, "8호선", "pink");
    private Station jamsil = new Station(1L, "잠실");
    private Station cheonho = new Station(2L, "천호");
    private Station source = new Station(3L, "source");
    private Station target = new Station(4L, "Target");
    private Section sourceToJamsil = new Section(1L, source.getId(), jamsil.getId(), firstLine.getId(), 10);
    private Section sourceToCheonHo = new Section(2L, source.getId(), cheonho.getId(), secondLine.getId(), 50);
    private Section jamsilToTarget = new Section(3L, jamsil.getId(), target.getId(), firstLine.getId(), 40);
    private Section cheohoToTarget = new Section(4L, cheonho.getId(), target.getId(), secondLine.getId(), 20);

    @BeforeEach
    void setUp() {
        path = new JgraphtPath();
        path.addStation(jamsil);
        path.addStation(cheonho);
        path.addStation(source);
        path.addStation(target);
        path.addSectionEdge(source, jamsil, new SectionEdge(sourceToJamsil));
        path.addSectionEdge(source, cheonho, new SectionEdge(sourceToCheonHo));
        path.addSectionEdge(jamsil, target, new SectionEdge(jamsilToTarget));
        path.addSectionEdge(cheonho, target, new SectionEdge(cheohoToTarget));
    }

    @DisplayName("그래프에서 가장 최소 길이를 반환한다.")
    @Test
    void getShortestPath() {
        // given
        Station[] expected = {source, jamsil, target};

        // when
        List<Station> shortestStationPath = path.getShortestStationPath(source, target);

        // then
        assertThat(shortestStationPath).containsExactly(expected);
    }

    @DisplayName("두 지하철 역이 이어져있지 않다면, 빈 Station 리스트를 반환한다.")
    @Test
    void getShortestPathBetweenUnconnectedVertices() {
        // given
        Station newStation = new Station("연결되지않은역");
        path.addStation(newStation);

        // when
        List<Station> shortestStationPath = path.getShortestStationPath(source, newStation);

        // then
        assertThat(shortestStationPath).isEmpty();
    }
}
