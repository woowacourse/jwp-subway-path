package subway.domain.line;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.exception.InvalidDistanceException;
import subway.exception.InvalidSectionException;

class LineTest {

    private Line line;
    private Station upward;
    private Station downward;

    @BeforeEach
    void setUp() {
        upward = new Station(1L, "잠실역");
        downward = new Station(2L, "종합운동장역");
        final List<Section> sections = List.of(
                new Section(upward, downward, 10)
        );
        line = new Line(1L, "2호선", "초록색", 300, new ArrayList<>(sections));
    }


    @Nested
    @DisplayName("구간 정보를 바탕으로 노선을 생성 시 ")
    class Of {

        @Test
        @DisplayName("유효한 구간 정보라면 정상적으로 생성한다.")
        void of() {
            //given
            final LineEntity lineEntity = new LineEntity(1L, "2호선", "초록색", 300);
            final List<SectionEntity> sectionEntities = List.of(
                    new SectionEntity(1L, 1L, 1L, "잠실역", 2L, "잠실새내역", 10),
                    new SectionEntity(3L, 1L, 3L, "종합운동장역", 4L, "선릉역", 10),
                    new SectionEntity(2L, 1L, 2L, "잠실새내역", 3L, "종합운동장역", 10)
            );

            //when
            final Line line = generateLine(lineEntity, sectionEntities);

            //then
            assertAll(
                    () -> assertThat(line.getId()).isEqualTo(lineEntity.getId()),
                    () -> assertThat(line.getName()).isEqualTo(lineEntity.getName()),
                    () -> assertThat(line.getColor()).isEqualTo(lineEntity.getColor()),
                    () -> assertThat(line.getExtraFare()).isEqualTo(lineEntity.getExtraFare()),
                    () -> assertThat(line.getStations()).extracting(Station::getName)
                            .containsExactly("잠실역", "잠실새내역", "종합운동장역", "선릉역")
            );
        }

        @Test
        @DisplayName("잘못된 구간 정보를 불러오는 경우 예외를 던진다.")
        void makeLineWithInvalidSections() {
            //given
            final LineEntity lineEntity = new LineEntity(1L, "2호선", "초록색", 300);
            final List<SectionEntity> sectionEntities = List.of(
                    new SectionEntity(1L, 1L, 1L, "잠실역", 2L, "잠실새내역", 10),
                    new SectionEntity(3L, 1L, 5L, "사당역", 7L, "서울역", 10)
            );

            //when
            //then
            assertThatThrownBy(() -> generateLine(lineEntity, sectionEntities))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("구간 정보가 올바르지 않습니다.");
        }
    }


    @Nested
    @DisplayName("노선 역 추가 시 ")
    class AddSection {

        @Test
        @DisplayName("노선에 역을 최초 추가한다.")
        void addFirstSection() {
            //given
            final Station upward = new Station(1L, "잠실역");
            final Station downward = new Station(2L, "종합운동장역");

            final Line line = new Line(1L, "2호선", "초록색", 300);

            //when
            line.addSection(upward, downward, 10);

            //then
            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(10)
            );
        }

        @Test
        @DisplayName("중간에 상행역을 추가한다.")
        void addUpwardSection() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(additionStation, downward, 5);

            //then
            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, additionStation, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(5, 5)
            );
        }

        @Test
        @DisplayName("중간에 하행역을 추가한다.")
        void addDownwardSection() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(upward, additionStation, 5);

            //then
            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, additionStation, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(5, 5)
            );
        }

        @Test
        @DisplayName("맨 앞에 역을 추가한다.")
        void addSectionAtFirst() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(additionStation, upward, 5);

            //then
            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(additionStation, upward, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(5, 10)
            );
        }

        @Test
        @DisplayName("맨 뒤에 역을 추가한다.")
        void addSectionAtLast() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            line.addSection(downward, additionStation, 5);

            //then
            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, downward, additionStation),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(10, 5)
            );
        }

        @Test
        @DisplayName("역이 둘다 존재한다면 예외를 던진다.")
        void addSectionWithExistStations() {
            //given
            //when
            //then
            assertThatThrownBy(() -> line.addSection(upward, downward, 5))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("두 역이 이미 노선에 존재합니다.");
        }

        @Test
        @DisplayName("역이 둘다 존재하지 않으면 예외를 던진다.")
        void addSectionWithoutExistStations() {
            //given
            final Station newUpward = new Station(3L, "잠실새내역");
            final Station newDownward = new Station(4L, "사당역");

            //when
            //then
            assertThatThrownBy(() -> line.addSection(newUpward, newDownward, 5))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("연결할 역 정보가 없습니다.");
        }

        @Test
        @DisplayName("역이 둘다 존재하지 않으면 예외를 던진다.")
        void addSectionWithInvalidRangeDistance() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");

            //when
            //then
            assertThatThrownBy(() -> line.addSection(upward, additionStation, 10))
                    .isInstanceOf(InvalidDistanceException.class)
                    .hasMessage("추가될 역의 거리는 추가될 위치의 두 역사이의 거리보다 작아야합니다.");
        }
    }

    @Nested
    @DisplayName("노선에서 역 제거할 시 ")
    class DeleteStation {

        @Test
        @DisplayName("역이 2개일 때 역을 제거한다.")
        void deleteStationAtInitialState() {
            //given
            //when
            line.deleteStation(upward);

            //then
            final List<Station> result = line.getStations();
            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("역이 2개가 아닐 때 맨 앞의 역을 제거한다.")
        void deleteStationAtFirst() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");
            line.addSection(upward, additionStation, 3);

            //when
            line.deleteStation(upward);

            //then
            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(additionStation, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(7)
            );
        }

        @Test
        @DisplayName("역이 2개가 아닐 때 중간의 역을 제거한다.")
        void deleteStationBetweenStations() {
            //given
            final Station additionStation = new Station(3L, "잠실새내역");
            line.addSection(upward, additionStation, 3);

            //when
            line.deleteStation(additionStation);

            //then
            final List<Station> result = line.getStations();
            assertAll(
                    () -> assertThat(result).containsExactly(upward, downward),
                    () -> assertThat(line.getSections()).extracting(Section::getDistance).containsExactly(10)
            );
        }

        @Test
        @DisplayName("역이 존재하지 않을 때 예외를 던진다.")
        void deleteStationWithNotExistStation() {
            //given
            //when
            //then
            assertThatThrownBy(() -> line.deleteStation(new Station(3L, "잠실새내역")))
                    .isInstanceOf(InvalidSectionException.class)
                    .hasMessage("노선에 해당 역이 존재하지 않습니다.");
        }
    }

    private Line generateLine(final LineEntity lineEntity, final List<SectionEntity> sectionEntities) {
        return new Line(
                lineEntity.getId(),
                lineEntity.getName(),
                lineEntity.getColor(),
                lineEntity.getExtraFare(),
                generateSections(sectionEntities)
        );
    }

    private List<Section> generateSections(final List<SectionEntity> sectionEntities) {
        return sectionEntities.stream()
                .map(Section::from)
                .collect(Collectors.toList());
    }
}
