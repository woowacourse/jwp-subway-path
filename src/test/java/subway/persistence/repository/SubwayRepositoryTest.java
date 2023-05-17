package subway.persistence.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import subway.domain.Line;
import subway.domain.Station;
import subway.exception.DuplicatedNameException;
import subway.persistence.dao.DaoTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SubwayRepositoryTest extends DaoTest {

    @Test
    void 노선을_조회한다() {
        final Line line = subwayRepository.findLine(1L);

        Assertions.assertAll(
                () -> assertThat(line.getName()).isEqualTo("1호선"),
                () -> assertThat(line.getColor()).isEqualTo("파랑"),
                () -> assertThat(line.getPaths()).hasSize(1)
        );
    }

    @Test
    void 모든_노선을_조회한다() {
        final List<Line> lines = subwayRepository.findLines();

        assertThat(lines).hasSize(3);
    }

    @Test
    void 노선을_삭제한다() {
        subwayRepository.deleteLineById(1L);

        final List<Line> lines = subwayRepository.findLines();

        assertThat(lines).hasSize(2);
    }

    @Test
    void 중복된_이름의_노선을_추가한다() {
        assertThatThrownBy(() -> subwayRepository.addLine(new Line("1호선", "아무색")))
                .isInstanceOf(DuplicatedNameException.class);
    }

    @Test
    void 중복된_이름의_역을_추가한다() {
        assertThatThrownBy(() -> subwayRepository.addStation(new Station("수원")))
                .isInstanceOf(DuplicatedNameException.class);
    }
}
