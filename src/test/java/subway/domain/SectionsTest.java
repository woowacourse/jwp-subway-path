package subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Test;
import subway.exception.DuplicateException;
import subway.exception.ErrorMessage;

@DisplayNameGeneration(ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {
    private Station 역을_생성한다(final Long 역_ID, final String 역_이름) {
        return new Station(역_ID, 역_이름);
    }

    private Section 구간을_생성한다(final Station 시작역, final Station 도착역, final int 거리) {
        return new Section(시작역, 도착역, 거리);
    }

    @Test
    void Sections를_from_메서드로_생성한다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        // expected
        assertAll(
                () -> assertThat(구간들.getSections().get(0)).isEqualTo(첫번째_구간),
                () -> assertThat(구간들.getSections().get(1)).isEqualTo(두번째_구간)
        );
    }

    @Test
    void 노선의_모든_역을_반환한다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        // when
        List<Station> stations = 구간들.getStations();

        // expected
        assertThat(stations).containsExactly(첫번째_역, 두번째_역, 세번째_역);
    }

    @Test
    void 새로운_구간을_추가한다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        // when
        Station 새로운_도착역 = new Station(4L, "삼성역");
        Section 새로운_구간 = new Section(세번째_역, 새로운_도착역, 2);
        구간들.addSection(새로운_구간);

        // expected
        assertAll(
                () -> assertThat(구간들.getStations()).hasSize(4),
                () -> assertThat(구간들.getSections().get(2)).isEqualTo(새로운_구간)
        );
    }

    @Test
    void 새로운_역들이_노선에_이미_존재하는_경우_예외가_발생한다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        Section 새로운_구간 = new Section(첫번째_역, 세번째_역, 5);

        // expected
        assertThatThrownBy(() -> 구간들.addSection(새로운_구간))
                .isInstanceOf(DuplicateException.class)
                .hasMessage(ErrorMessage.DUPLICATE_STATION.getErrorMessage());
    }

    @Test
    void 새로운_역이_상행_종점으로_추가된다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        Station 새로운_상행_종점_역 = new Station(4L, "새로운상행종점역");
        Section 새로운_구간 = new Section(새로운_상행_종점_역, 첫번째_역, 10);

        // when
        구간들.addSection(새로운_구간);

        // expected
        assertThat(구간들.getSections().get(0)).isEqualTo(새로운_구간);
    }

    @Test
    void 새로운_역이_하행_종점으로_추가된다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        Station 새로운_하행_종점_역 = new Station(4L, "베로역");
        Section 새로운_구간 = new Section(세번째_역, 새로운_하행_종점_역, 10);

        // when
        구간들.addSection(새로운_구간);

        // expected
        assertThat(구간들.getSections().get(2)).isEqualTo(새로운_구간);
    }

    @Test
    void 중간_역을_삭제하면_연결된_구간이_삭제된다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        // when
        구간들.deleteStation(두번째_역);

        // expected
        assertAll(
                () -> assertThat(구간들.getStations()).hasSize(2),
                () -> assertThat(구간들.getSections()).hasSize(1)
        );
    }

    @Test
    void 종점을_삭제한다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");
        Station 세번째_역 = 역을_생성한다(3L, "종합운동장역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);
        Section 두번째_구간 = 구간을_생성한다(두번째_역, 세번째_역, 20);

        Sections 구간들 = Sections.from(List.of(첫번째_구간, 두번째_구간));

        // when
        구간들.deleteStation(첫번째_역);

        // expected
        assertAll(
                () -> assertThat(구간들.getStations()).hasSize(2),
                () -> assertThat(구간들.getSections()).hasSize(1)
        );
    }

    @Test
    void 노선에_역이_두_개_존재할_때_모든_노선이_삭제된다() {
        // given
        Station 첫번째_역 = 역을_생성한다(1L, "잠실역");
        Station 두번째_역 = 역을_생성한다(2L, "잠실새내역");

        Section 첫번째_구간 = 구간을_생성한다(첫번째_역, 두번째_역, 10);

        Sections 구간들 = Sections.from(List.of(첫번째_구간));

        // when
        구간들.deleteStation(첫번째_역);

        // expected
        assertAll(
                () -> assertThat(구간들.getStations()).hasSize(0),
                () -> assertThat(구간들.getSections()).hasSize(0)
        );
    }
}
