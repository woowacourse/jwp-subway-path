package subway.application.core.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.StationFixture;
import subway.application.core.domain.Distance;
import subway.application.core.domain.Line;
import subway.application.core.domain.LineProperty;
import subway.application.core.domain.Section;
import subway.application.core.domain.Station;
import subway.application.core.exception.StationAlreadyExistsException;
import subway.application.core.exception.StationConnectException;
import subway.application.core.exception.StationNotExistsException;
import subway.application.core.exception.StationTooFarException;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

class LineTest {

    private Line line;

    @BeforeEach
    void setting() {
        line = new Line(new LineProperty(null, "name", "red"), new ArrayList<>());
    }

    @Test
    @DisplayName("노선에 역이 존재할 때, 입력된 두 역의 정보가 존재하지 않는다면 예외를 던진다")
    void addSection_exception_nonExists() {
        //given
        line.addSection(new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("테오"),
                new Distance(3)
        ));

        Section section = new Section(
                StationFixture.ofNullId("시카"),
                StationFixture.ofNullId("제이온"),
                new Distance(1)
        );

        //when, then
        assertThatThrownBy(() -> line.addSection(section))
                .isInstanceOf(StationConnectException.class);
    }

    @Test
    @DisplayName("이미 존재하는 노선이라면 예외를 던진다")
    void addSection_exception_alreadyExist() {
        //given
        line.addSection(new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("테오"),
                new Distance(3)
        ));

        line.addSection(new Section(
                StationFixture.ofNullId("테오"),
                StationFixture.ofNullId("제이온"),
                new Distance(1)
        ));

        Section section = new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("제이온"),
                new Distance(1)
        );

        //when, then
        assertThatThrownBy(() -> line.addSection(section))
                .isInstanceOf(StationAlreadyExistsException.class);
    }

    @Test
    @DisplayName("새로운 노선의 거리가 기존 노선의 거리를 넘는다면 예외를 던진다")
    void addSection_exception_overLength() {
        //given
        line.addSection(new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("테오"),
                new Distance(1)
        ));
        Section overLengthSection = new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("시카"),
                new Distance(100)
        );

        //when, then
        assertThatThrownBy(() -> line.addSection(overLengthSection))
                .isInstanceOf(StationTooFarException.class);
    }

    @Test
    @DisplayName("새로운 노선의 거리가 기존 노선의 거리를 넘지 않는다면 정상 작동한다")
    void addSection_insideSection() {
        //given
        line.addSection(new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("테오"),
                new Distance(100)
        ));
        Section newSection = new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("시카"),
                new Distance(1)
        );

        assertThatCode(() -> line.addSection(newSection))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("종착역에 대해서는 거리에 관계없이 항상 노선을 추가할 수 있다")
    void addSection_endPoint() {
        //given
        line.addSection(new Section(
                StationFixture.ofNullId("푸우"),
                StationFixture.ofNullId("테오"),
                new Distance(100)
        ));
        Section overLengthSection = new Section(
                StationFixture.ofNullId("테오"),
                StationFixture.ofNullId("시카"),
                new Distance(1000)
        );

        //when, then
        assertThatCode(() -> line.addSection(overLengthSection))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("아무 역이 존재하지 않을 때 삭제하려고 하면 예외를 던진다")
    void deleteSection_exception_whenEmpty() {
        //given
        Station station = StationFixture.ofNullId("테오");

        //when, then
        assertThatThrownBy(() -> line.deleteStation(station))
                .isInstanceOf(StationNotExistsException.class);
    }

    @Test
    @DisplayName("해당 역이 존재하지 않을 때 삭제하려고 하면 예외를 던진다")
    void deleteSection_exception_whenNotExists() {
        //given
        Section section = new Section(
                StationFixture.ofNullId("테오"),
                StationFixture.ofNullId("시카"),
                new Distance(1000)
        );
        line.addSection(section);

        Station station = StationFixture.ofNullId("푸우");

        //when, then
        assertThatThrownBy(() -> line.deleteStation(station))
                .isInstanceOf(StationNotExistsException.class);
    }

    @Test
    @DisplayName("노선 중간에 있는 역을 삭제할 수 있다")
    void deleteSection_middle() {
        //given
        line.addSection(new Section(
                StationFixture.ofNullId("테오"),
                StationFixture.ofNullId("시카"),
                new Distance(1000)
        ));
        line.addSection(new Section(
                StationFixture.ofNullId("시카"),
                StationFixture.ofNullId("제이온"),
                new Distance(1000)
        ));

        Station station = StationFixture.ofNullId("시카");

        // when, then
        assertThatCode(() -> line.deleteStation(station))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("종착역을 삭제할 수 있다")
    void deleteSection_endPoint() {
        //given
        line.addSection(new Section(
                StationFixture.ofNullId("테오"),
                StationFixture.ofNullId("시카"),
                new Distance(1000)
        ));
        line.addSection(new Section(
                StationFixture.ofNullId("시카"),
                StationFixture.ofNullId("제이온"),
                new Distance(1000)
        ));

        Station station = StationFixture.ofNullId("테오");

        //when, then
        assertThatCode(() -> line.deleteStation(station))
                .doesNotThrowAnyException();
    }
}
