package subway.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.dto.AddResultDto;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {
    @Test
    void 동일한_역으로_추가하려고_하면_예외() {
        Sections sections = new Sections(new ArrayList<>());
        Station jamsil1 = new Station(1L, "잠실");
        Station jamsil2 = new Station(2L, "잠실");
        Line second = new Line("2호선", "green");
        Distance distance = new Distance(10);
        assertThatThrownBy(() -> sections.add(jamsil1, jamsil2, distance, second))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");
    }

    @Test
    void 노선에_역이_없을_때_새로운_역을_추가한다() {
        //given
        Sections sections = new Sections(new ArrayList<>());
        Station jamsil = new Station(1L, "잠실");
        Station seonleung = new Station(2L, "선릉");
        Line second = new Line(1L, "2호선", "green");
        Distance distance = new Distance(10);

        //when
        AddResultDto addResult = sections.add(jamsil, seonleung, distance, second);

        //then
        List<Section> addedResults = addResult.getAddedResults();
        List<Section> deletedResults = addResult.getDeletedResults();
        List<Station> addedStation = addResult.getAddedStation();
        Section newSection = addedResults.get(0);

        Assertions.assertAll(
                () -> assertThat(addedResults).hasSize(1),
                () -> assertThat(newSection.getLine()).isEqualTo(second),
                () -> assertThat(newSection.getUpStation()).isEqualTo(jamsil),
                () -> assertThat(newSection.getDownStation()).isEqualTo(seonleung),
                () -> assertThat(newSection.getDistance()).isEqualTo(distance),
                () -> assertThat(deletedResults).hasSize(0),
                () -> assertThat(addedStation).hasSize(2),
                () -> assertThat(addedStation).containsExactlyInAnyOrder(jamsil, seonleung)
        );
    }
}
