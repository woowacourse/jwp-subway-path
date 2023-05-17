package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.domain.SectionFixture.LINE1_SECTION_ST1_ST2;
import static subway.domain.SectionFixture.LINE1_SECTION_ST2_ST3;
import static subway.domain.SectionFixture.LINE1_SECTION_ST3_ST4;
import static subway.domain.SectionFixture.LINE1_SECTION_ST4_ST5;
import static subway.domain.SectionFixture.LINE1_SECTION_ST5_ST6;
import static subway.domain.StationFixture.FIXTURE_STATION_4;
import static subway.domain.StationFixture.FIXTURE_STATION_6;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoutedStationsFactoryTest {

    @DisplayName("구간 목록을 전달받아 하행 기준으로 연결된 역 연결 정보를 생성한다")
    @Test
    void create() {
        List<Section> sections = List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_ST2_ST3,
                LINE1_SECTION_ST3_ST4,
                LINE1_SECTION_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        );

        RoutedStations result = RoutedStationsFactory.create(sections);

        for (Section section : sections) {
            assertThat(result.containsEdge(section.getLeft(), section.getRight()))
                    .isTrue();
            assertThat(result.getEdgeWeight(result.getEdge(section.getLeft(), section.getRight())))
                    .isEqualTo(section.getDistance().getValue());
        }
    }

    @DisplayName("중간에 연결이 끊기는 구간 목록일 경우 예외를 발생한다")
    @Test
    void createFailDisconnected() {
        List<Section> sections = List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_ST3_ST4,
                LINE1_SECTION_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        );

        assertThatThrownBy(() -> RoutedStationsFactory.create(sections))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("하행 종점은 1개여야 합니다.");
    }

    @DisplayName("갈래길을 가지는 구간 목록일 경우 예외를 발생한다")
    @Test
    void createFailForked() {
        List<Section> sections = List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_ST2_ST3,
                LINE1_SECTION_ST3_ST4,
                LINE1_SECTION_ST4_ST5,
                new Section(FIXTURE_STATION_4, FIXTURE_STATION_6, new Distance(10))
        );

        assertThatThrownBy(() -> RoutedStationsFactory.create(sections))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("상행 종점은 1개여야 합니다.");
    }
}
