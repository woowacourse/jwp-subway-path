package subway.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SectionsDomainTest {
    @DisplayName("이미 존재하는 구간을 추가할 경우 예외가 발생한다.")
    @Test
    void throwExceptionWhenSectionDuplicated() {
        // given
        final StationDomain 잠실역 = new StationDomain(1L, "잠실");
        final StationDomain 잠실새내역 = new StationDomain(2L, "잠실새내");
        final Distance 거리10 = new Distance(10);
        final SectionDomain 잠실역_잠실새내역_구간 = SectionDomain.startFrom(잠실역, 잠실새내역, 거리10);

        final SectionsDomain 구간_목록 = SectionsDomain.from(List.of(잠실역_잠실새내역_구간));
        구간_목록.addSection(잠실역_잠실새내역_구간);

        // expect
        assertThatThrownBy(() -> 구간_목록.addSection(잠실역_잠실새내역_구간))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("구간이 하나 이상 존재하는 노선에 새로운 구간을 등록할 때 기준이 되는 지하철역이 존재하지 않는다면 예외가 발생한다.")
    @Test
    void throwExceptionWhenStationBaseIsNotExists() {
        // given
        final StationDomain 잠실역 = new StationDomain(1L, "잠실");
        final StationDomain 잠실새내역 = new StationDomain(2L, "잠실새내");
        final Distance 거리10 = new Distance(10);
        final SectionDomain 잠실역_잠실새내역_구간 = SectionDomain.startFrom(잠실역, 잠실새내역, 거리10);

        final SectionsDomain 구간_목록 = SectionsDomain.from(List.of(잠실역_잠실새내역_구간));
        구간_목록.addSection(잠실역_잠실새내역_구간);

        final StationDomain 창동역 = new StationDomain(3L, "창동");
        final StationDomain 녹천역 = new StationDomain(4L, "녹천");

        // expect
        assertThatThrownBy(() -> 구간_목록.addSection(잠실역_잠실새내역_구간))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 구간을_구성하는_역을_모은다() {
        // given
        final StationDomain 잠실역 = new StationDomain("잠실");
        final StationDomain 잠실새내역 = new StationDomain("잠실새내");
        final StationDomain 창동역 = new StationDomain("창동");
        final StationDomain 녹천역 = new StationDomain("녹천");

        final Distance 거리10 = new Distance(10);
        final SectionDomain 잠실역_잠실새내역_구간 = SectionDomain.startFrom(잠실역, 잠실새내역, 거리10);
        final SectionDomain 잠실새내역_창동역_구간 = SectionDomain.notStartFrom(잠실새내역, 창동역, 거리10);
        final SectionDomain 창동역_녹천역_구간 = SectionDomain.notStartFrom(창동역, 녹천역, 거리10);

        final SectionsDomain 구간_목록 = SectionsDomain.from(List.of(잠실역_잠실새내역_구간, 잠실새내역_창동역_구간, 창동역_녹천역_구간));

        // when
        final List<StationDomain> stations = 구간_목록.collectAllStations();

        // then
        assertThat(stations)
                .contains(잠실역, 잠실새내역, 창동역, 녹천역);
    }
}
