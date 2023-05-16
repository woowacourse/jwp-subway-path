package subway.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import static subway.fixture.DistanceFixture.거리;
import static subway.fixture.LineFixture.*;
import static subway.fixture.MoneyFixture.비용;
import static subway.fixture.SectionFixture.*;
import static subway.fixture.SectionsFixture.구간_목록;
import static subway.fixture.StationFixture.*;

@DisplayNameGeneration(ReplaceUnderscores.class)
class SubwayGraphTest {

    @Test
    void 출발역A에서_도착역B의_최단경로목록이_AB일_경우() {
        // given
        final Section 구간_AB = 구간(거리(10), 역("A"), 역("B"));
        final Sections 구간_목록 = 구간_목록(List.of(구간_AB));
        final Line 노선1 = 노선("1", "파랑", 구간_목록);

        // when
        final SubwayGraph 지하철_그래프 = SubwayGraph.newInstance(List.of(노선1));

        final Route 경로 = 지하철_그래프.findRouteBy(역("A"), 역("B"));
        final Distance 최단_거리 = 경로.getTotalDistance();
        final List<Section> 최단_구간_목록 = 경로.getSections();
        final Station 출발역 = 경로.getFrom();
        final Station 도착역 = 경로.getTo();

        // then
        assertAll(
                () -> assertThat(최단_거리).isEqualTo(거리(10)),
                () -> assertThat(최단_구간_목록).containsExactly(상행_종점_구간(거리(10), 역("A"), 역("B"))),
                () -> assertThat(출발역).isEqualTo(역("A")),
                () -> assertThat(도착역).isEqualTo(역("B"))
        );
    }

    @Test
    void 출발역A에서_도착역F의_최단경로목록이_AF일_경우() {
        // given
        final Section 구간_AB = 상행_종점_구간(거리(10), 역("A"), 역("B"));
        final Section 구간_BC = 구간(거리(10), 역("B"), 역("C"));
        final Section 구간_CD = 구간(거리(10), 역("C"), 역("D"));
        final Section 구간_DE = 구간(거리(10), 역("D"), 역("E"));
        final Line 노선1 = 노선("1", "파랑", 구간_목록(List.of(구간_AB, 구간_BC, 구간_CD, 구간_DE)));

        final Section 구간_AF = 상행_종점_구간(거리(10), 역("A"), 역("F"));
        final Line 노선2 = 노선("2", "초록", 구간_목록(List.of(구간_AF)));


        // when
        final SubwayGraph 지하철_그래프 = SubwayGraph.newInstance(List.of(노선1, 노선2));

        final Route 경로 = 지하철_그래프.findRouteBy(역("A"), 역("F"));
        final Distance 최단_거리 = 경로.getTotalDistance();
        final List<Section> 최단_구간_목록 = 경로.getSections();
        final Station 출발역 = 경로.getFrom();
        final Station 도착역 = 경로.getTo();

        // then
        assertAll(
                () -> assertThat(최단_거리).isEqualTo(거리(10)),
                () -> assertThat(최단_구간_목록).containsExactly(상행_종점_구간(거리(10), 역("A"), 역("F"))),
                () -> assertThat(출발역).isEqualTo(역("A")),
                () -> assertThat(도착역).isEqualTo(역("F"))
        );
    }

    @Test
    void 출발역A에서_도착역F의_최단경로목록이_AB_BF_일_경우() {
        // given
        final Section 구간_AB = 상행_종점_구간(거리(10), 역("A"), 역("B"));
        final Section 구간_BC = 구간(거리(10), 역("B"), 역("C"));
        final Section 구간_CD = 구간(거리(10), 역("C"), 역("D"));
        final Section 구간_DE = 구간(거리(10), 역("D"), 역("E"));
        final Line 노선1 = 노선("1", "파랑", 구간_목록(List.of(구간_AB, 구간_BC, 구간_CD, 구간_DE)));

        final Section 구간_BF = 상행_종점_구간(거리(1), 역("B"), 역("F"));
        final Line 노선2 = 노선("2", "초록", 구간_목록(List.of(구간_BF)));


        // when
        final SubwayGraph 지하철_그래프 = SubwayGraph.newInstance(List.of(노선1, 노선2));

        final Route 경로 = 지하철_그래프.findRouteBy(역("A"), 역("F"));
        final Distance 최단_거리 = 경로.getTotalDistance();
        final List<Section> 최단_구간_목록 = 경로.getSections();
        final Station 출발역 = 경로.getFrom();
        final Station 도착역 = 경로.getTo();

        // then
        assertAll(
                () -> assertThat(최단_거리).isEqualTo(거리(11)),
                () -> assertThat(최단_구간_목록).containsExactly(
                        상행_종점_구간(거리(10), 역("A"), 역("B")),
                        상행_종점_구간(거리(1), 역("B"), 역("F"))
                ),
                () -> assertThat(출발역).isEqualTo(역("A")),
                () -> assertThat(도착역).isEqualTo(역("F"))
        );
    }

    @Test
    void 출발역A에서_도착역F의_최단경로목록이_AB_BD_DE_EF_일_경우() {
        // given
        final Section 구간_AB = 상행_종점_구간(거리(1), 역("A"), 역("B"));
        final Section 구간_BC = 구간(거리(10), 역("B"), 역("C"));
        final Section 구간_CD = 구간(거리(10), 역("C"), 역("D"));
        final Line 노선1 = 노선("1", "파랑", 구간_목록(List.of(구간_AB, 구간_BC, 구간_CD)));

        final Section 구간_BD = 상행_종점_구간(거리(2), 역("B"), 역("D"));
        final Section 구간_DE = 구간(거리(3), 역("D"), 역("E"));
        final Line 노선2 = 노선("2", "초록", 구간_목록(List.of(구간_BD, 구간_DE)));

        final Section 구간_EF = 상행_종점_구간(거리(4), 역("E"), 역("F"));
        final Line 노선3 = 노선("3", "주황", 구간_목록(List.of(구간_EF)));


        // when
        final SubwayGraph 지하철_그래프 = SubwayGraph.newInstance(List.of(노선1, 노선2, 노선3));

        final Route 경로 = 지하철_그래프.findRouteBy(역("A"), 역("F"));
        final Distance 최단_거리 = 경로.getTotalDistance();
        final List<Section> 최단_구간_목록 = 경로.getSections();
        final Station 출발역 = 경로.getFrom();
        final Station 도착역 = 경로.getTo();

        // then
        assertAll(
                () -> assertThat(최단_거리).isEqualTo(거리(10)),
                () -> assertThat(최단_구간_목록).containsExactly(
                        상행_종점_구간(거리(1), 역("A"), 역("B")),
                        상행_종점_구간(거리(2), 역("B"), 역("D")),
                        상행_종점_구간(거리(3), 역("D"), 역("E")),
                        상행_종점_구간(거리(4), 역("E"), 역("F"))
                ),
                () -> assertThat(출발역).isEqualTo(역("A")),
                () -> assertThat(도착역).isEqualTo(역("F"))
        );
    }

    @Test
    void 출발역F에서_도착역A의_최단경로목록이_FE_ED_DB_BA_일_경우() {
        // given
        final Section 구간_AB = 상행_종점_구간(거리(1), 역("A"), 역("B"));
        final Section 구간_BC = 구간(거리(10), 역("B"), 역("C"));
        final Section 구간_CD = 구간(거리(10), 역("C"), 역("D"));
        final Line 노선1 = 노선("1", "파랑", 구간_목록(List.of(구간_AB, 구간_BC, 구간_CD)));

        final Section 구간_BD = 상행_종점_구간(거리(2), 역("B"), 역("D"));
        final Section 구간_DE = 구간(거리(3), 역("D"), 역("E"));
        final Line 노선2 = 노선("2", "초록", 구간_목록(List.of(구간_BD, 구간_DE)));

        final Section 구간_EF = 상행_종점_구간(거리(4), 역("E"), 역("F"));
        final Line 노선3 = 노선("3", "주황", 구간_목록(List.of(구간_EF)));


        // when
        final SubwayGraph 지하철_그래프 = SubwayGraph.newInstance(List.of(노선1, 노선2, 노선3));

        final Route 경로 = 지하철_그래프.findRouteBy(역("F"), 역("A"));
        final Station 출발역 = 경로.getFrom();
        final Station 도착역 = 경로.getTo();
        final List<Station> 환승역_목록 = 경로.getTransfers();
        final List<Section> 최단_구간_목록 = 경로.getSections();
        final Money 비용 = 경로.getTotalPrice();
        final Distance 최단_거리 = 경로.getTotalDistance();

        // then
        assertAll(
                () -> assertThat(출발역).isEqualTo(역("F")),
                () -> assertThat(도착역).isEqualTo(역("A")),
                () -> assertThat(환승역_목록).containsExactly(
                        역("E"), 역("B")
                ),
                () -> assertThat(최단_구간_목록).containsExactly(
                        // EF -> DE -> BD -> AB
                        상행_종점_구간(거리(4), 역("F"), 역("E")),
                        상행_종점_구간(거리(3), 역("E"), 역("D")),
                        상행_종점_구간(거리(2), 역("D"), 역("B")),
                        상행_종점_구간(거리(1), 역("B"), 역("A"))
                ),
                () -> assertThat(비용).isEqualTo(비용(1250)),
                () -> assertThat(최단_거리).isEqualTo(거리(10))
        );
    }
}
