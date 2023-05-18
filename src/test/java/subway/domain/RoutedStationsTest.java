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
import subway.domain.exception.AbnormalRoutedStationsException;

class RoutedStationsTest {

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

        RoutedStations result = RoutedStations.from(sections);

        for (Section section : sections) {
            assertThat(result.containsEdge(section.getLeft(), section.getRight()))
                    .isTrue();
            assertThat(result.getEdgeWeight(result.getEdge(section.getLeft(), section.getRight())))
                    .isEqualTo(section.getDistance().getValue());
        }
    }

    @DisplayName("동일한 두 점을 연결하는 구간이 2개 이상이면 역 연결 그래프를 생성할 수 없다")
    @Test
    void createFailSectionsDuplicated() {
        List<Section> sections = List.of(
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_ST1_ST2,
                LINE1_SECTION_ST3_ST4,
                LINE1_SECTION_ST4_ST5,
                LINE1_SECTION_ST5_ST6
        );

        assertThatThrownBy(() -> RoutedStations.from(sections))
                .isInstanceOf(AbnormalRoutedStationsException.class)
                .hasMessageContaining("동일한 두 점을 연결하는 구간이 존재합니다.");
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

        assertThatThrownBy(() -> RoutedStations.from(sections))
                .isInstanceOf(AbnormalRoutedStationsException.class)
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

        assertThatThrownBy(() -> RoutedStations.from(sections))
                .isInstanceOf(AbnormalRoutedStationsException.class)
                .hasMessageContaining("상행 종점은 1개여야 합니다.");
    }
}
