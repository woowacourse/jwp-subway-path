package subway.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import subway.fixture.StationFixture;
import subway.service.section.domain.Distance;
import subway.service.section.domain.Section;
import subway.service.section.domain.Sections;
import subway.service.section.dto.AddResult;
import subway.service.section.dto.DeleteResult;
import subway.service.station.domain.Station;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.fixture.StationFixture.GANGNAM;
import static subway.fixture.StationFixture.JAMSIL;
import static subway.fixture.StationFixture.SEONLEUNG;
import static subway.fixture.StationFixture.YUKSAM;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {
    @Nested
    class 역_추가 {
        @Test
        void 동일한_역으로_추가하려고_하면_예외() {
            Sections sections = new Sections(new ArrayList<>());
            Distance distance = new Distance(10);
            assertThatThrownBy(() -> sections.add(JAMSIL, JAMSIL, distance))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("동일한 역 2개가 입력으로 들어왔습니다. 이름을 다르게 설정해주세요.");
        }

        @Test
        void 노선에_역이_없을_때_새로운_역을_추가한다() {
            //given
            Sections sections = new Sections(new ArrayList<>());
            Distance distance = new Distance(10);

            //when
            AddResult addResult = sections.add(StationFixture.JAMSIL, StationFixture.SEONLEUNG, distance);

            //then
            List<Section> addedResults = addResult.getAddedResults();
            List<Section> deletedResults = addResult.getDeletedResults();
            List<Station> addedStation = addResult.getAddedStation();
            Section newSection = addedResults.get(0);

            Assertions.assertAll(
                    () -> assertThat(addedResults).hasSize(1),
                    () -> assertThat(newSection.getUpStation()).isEqualTo(JAMSIL),
                    () -> assertThat(newSection.getDownStation()).isEqualTo(SEONLEUNG),
                    () -> assertThat(newSection.getDistance()).isEqualTo(distance),
                    () -> assertThat(deletedResults).hasSize(0),
                    () -> assertThat(addedStation).hasSize(2),
                    () -> assertThat(addedStation).containsExactlyInAnyOrder(JAMSIL, SEONLEUNG)
            );
        }

        @Test
        void 노선에_역이_존재할_때_새로운_역_2개를_추가하려고_하면_예외() {
            Section section = new Section(JAMSIL, SEONLEUNG, new Distance(10));
            Sections sections = new Sections(List.of(section));

            assertThatThrownBy(() -> sections.add(GANGNAM, YUKSAM, new Distance(5)))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 노선에 역들이 존재하기 때문에 한 번에 새로운 역 2개를 추가할 수 없습니다.");
        }

        @Test
        void 상행_종점을_추가한다() {
            //given
            Distance gandnamYuksamDistance = new Distance(10);
            Section section = new Section(YUKSAM, GANGNAM, gandnamYuksamDistance);
            Sections sections = new Sections(List.of(section));

            Distance jamsilYuksamDistance = new Distance(5);

            //when
            AddResult addUpEndStationResult = sections.add(JAMSIL, YUKSAM, jamsilYuksamDistance);

            //then
            List<Section> addedResults = addUpEndStationResult.getAddedResults();
            List<Section> deletedResults = addUpEndStationResult.getDeletedResults();
            List<Station> addedStation = addUpEndStationResult.getAddedStation();

            Section newSection = addedResults.get(0);

            Assertions.assertAll(
                    () -> assertThat(addedResults).hasSize(1),
                    () -> assertThat(newSection.getUpStation()).isEqualTo(JAMSIL),
                    () -> assertThat(newSection.getDownStation()).isEqualTo(YUKSAM),
                    () -> assertThat(newSection.getDistance()).isEqualTo(jamsilYuksamDistance),
                    () -> assertThat(deletedResults).hasSize(0),
                    () -> assertThat(addedStation).hasSize(1),
                    () -> assertThat(addedStation).containsExactly(JAMSIL)
            );
        }

        @Test
        void 하행_종점을_추가한다() {
            Distance jamsilGangnamDistance = new Distance(10);
            Section section = new Section(JAMSIL, GANGNAM, jamsilGangnamDistance);
            Sections sections = new Sections(List.of(section));

            Distance gangnamYuksamDistance = new Distance(4);

            AddResult addDownEndStation = sections.add(GANGNAM, YUKSAM, gangnamYuksamDistance);

            List<Section> addedResults = addDownEndStation.getAddedResults();
            List<Section> deletedResults = addDownEndStation.getDeletedResults();
            List<Station> addedStation = addDownEndStation.getAddedStation();

            Section newSection = addedResults.get(0);

            Assertions.assertAll(
                    () -> assertThat(addedResults).hasSize(1),
                    () -> assertThat(newSection.getUpStation()).isEqualTo(GANGNAM),
                    () -> assertThat(newSection.getDownStation()).isEqualTo(YUKSAM),
                    () -> assertThat(newSection.getDistance()).isEqualTo(gangnamYuksamDistance),
                    () -> assertThat(deletedResults).hasSize(0),
                    () -> assertThat(addedStation).hasSize(1),
                    () -> assertThat(addedStation).containsExactly(YUKSAM)
            );
        }

        @Test
        void 새롭게_추가되는_경로가_기존_경로보다_거리가_길면_예외() {
            Distance jamsilGangnamDistance = new Distance(10);
            Section jamsilGangnamSection = new Section(JAMSIL, GANGNAM, jamsilGangnamDistance);
            Sections sections = new Sections(List.of(jamsilGangnamSection));

            Distance jamsilSeonleungDistance = new Distance(10);

            assertThatThrownBy(() -> sections.add(JAMSIL, SEONLEUNG, jamsilSeonleungDistance))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("새로운 경로의 거리는 기존 경로보다 클 수 없습니다.");
        }

        @Test
        void 새롭게_추가되는_경로가_기존_경로보다_작으면_정상수행() {
            Distance jamsilGangnamDistance = new Distance(10);
            Section jamsilGangnamSection = new Section(JAMSIL, GANGNAM, jamsilGangnamDistance);
            Sections sections = new Sections(List.of(jamsilGangnamSection));

            Distance jamsilSeonleungDistance = new Distance(9);

            assertDoesNotThrow(() -> sections.add(JAMSIL, SEONLEUNG, jamsilSeonleungDistance));
        }

        @Test
        void 상행역이_존재하고_새로운_하행역을_추가한다() {
            // given
            Distance jamsilGangnamDistance = new Distance(10);
            Section jamsilGangnamSection = new Section(JAMSIL, GANGNAM, jamsilGangnamDistance);
            Sections sections = new Sections(List.of(jamsilGangnamSection));

            Distance jamsilSeonleungDistance = new Distance(4);

            // when
            AddResult addDownwardInMiddleResult = sections.add(JAMSIL, SEONLEUNG, jamsilSeonleungDistance);

            List<Section> addedResults = addDownwardInMiddleResult.getAddedResults();
            List<Section> deletedResults = addDownwardInMiddleResult.getDeletedResults();
            List<Station> addedStation = addDownwardInMiddleResult.getAddedStation();

            // 새롭게 추가된 SEONLEUNG -> JAMSIM섹션의 거리
            Section jamsilSeonleungSection = addedResults.stream()
                    .filter(section -> section.getUpStation().equals(JAMSIL) && section.getDownStation().equals(SEONLEUNG))
                    .findAny()
                    .get();

            // SEONLEUNG -> JAMSIM섹션 추가로 인해 생긴 GANGNAM -> SEONLEUNG섹션의 거리
            Section seonleungGangNameSection = addedResults.stream()
                    .filter(section -> section.getUpStation().equals(SEONLEUNG) && section.getDownStation().equals(GANGNAM))
                    .findAny()
                    .get();

            // 상행 역 조회
            List<Station> upStations = addedResults.stream()
                    .map(Section::getUpStation)
                    .collect(Collectors.toList());
            // 하행 역 조회
            List<Station> downStations = addedResults.stream()
                    .map(Section::getDownStation)
                    .collect(Collectors.toList());

            // 상행 역과 하행 역을 합쳐 노선의 역들 추출
            upStations.addAll(downStations);
            List<Station> stations = upStations.stream()
                    .distinct()
                    .collect(Collectors.toList());
            Assertions.assertAll(
                    () -> assertThat(addedResults).hasSize(2),
                    () -> assertThat(deletedResults).hasSize(1),
                    () -> assertThat(addedStation).hasSize(1),
                    () -> assertThat(addedStation).containsExactly(SEONLEUNG),
                    () -> assertThat(stations).containsExactlyInAnyOrder(JAMSIL, SEONLEUNG, GANGNAM),
                    () -> assertThat(jamsilSeonleungSection.getDistance()).isEqualTo(jamsilSeonleungDistance),
                    // 새롭게 추가된 노선으로 인해 기존 노선의 변경 확인
                    () -> assertThat(seonleungGangNameSection.getDistance()).isEqualTo(jamsilGangnamSection.calculateNewSectionDistance(jamsilSeonleungDistance))
            );

        }

        @Test
        void 하행역이_존재하고_새로운_상행역을_추가한다() {
            //given
            Distance jamsilGangnamDistance = new Distance(10);
            Section jamsilGangnamSection = new Section(JAMSIL, GANGNAM, jamsilGangnamDistance);
            Sections sections = new Sections(List.of(jamsilGangnamSection));

            Distance yuksamGangnamDistance = new Distance(4);

            //when
            AddResult addUpwardInMiddleTest = sections.add(YUKSAM, GANGNAM, yuksamGangnamDistance);
            List<Section> addedResults = addUpwardInMiddleTest.getAddedResults();
            List<Section> deletedResults = addUpwardInMiddleTest.getDeletedResults();
            List<Station> addedStation = addUpwardInMiddleTest.getAddedStation();

            // 새롭게 추가된 GANGNAM -> YUKSAM섹션의 거리
            Section yuksamGangnamSection = addedResults.stream()
                    .filter(section -> section.getUpStation().equals(YUKSAM) && section.getDownStation().equals(GANGNAM))
                    .findAny()
                    .get();

            // YUKSAM -> JAMSIL섹션 추가로 인해 생긴 GANGNAM -> SEONLEUNG섹션의 거리
            Section jamsilYuksamSection = addedResults.stream()
                    .filter(section -> section.getUpStation().equals(JAMSIL) && section.getDownStation().equals(YUKSAM))
                    .findAny()
                    .get();

            // 상행 역 조회
            List<Station> upStations = addedResults.stream()
                    .map(Section::getUpStation)
                    .collect(Collectors.toList());
            // 하행 역 조회
            List<Station> downStations = addedResults.stream()
                    .map(Section::getDownStation)
                    .collect(Collectors.toList());

            // 상행 역과 하행 역을 합쳐 노선의 역들 추출
            upStations.addAll(downStations);
            List<Station> stations = upStations.stream()
                    .distinct()
                    .collect(Collectors.toList());

            //then
            Assertions.assertAll(
                    () -> assertThat(addedResults).hasSize(2),
                    () -> assertThat(deletedResults).hasSize(1),
                    () -> assertThat(addedStation).hasSize(1),
                    () -> assertThat(addedStation).containsExactly(YUKSAM),
                    () -> assertThat(stations).containsExactlyInAnyOrder(JAMSIL, YUKSAM, GANGNAM),
                    () -> assertThat(yuksamGangnamSection.getDistance()).isEqualTo(yuksamGangnamDistance),
                    // 새롭게 추가된 노선으로 인해 기존 노선의 변경 확인
                    () -> assertThat(jamsilYuksamSection.getDistance()).isEqualTo(jamsilGangnamSection.calculateNewSectionDistance(yuksamGangnamDistance))
            );

        }
    }

    @Nested
    class 역_제거 {

        @Test
        void 노선에_존재하지_않는_역_삭제_시_예외() {

            Section jamsilSeonleungSection = new Section(JAMSIL, SEONLEUNG, new Distance(10));
            Section gangnamToSeonleung = new Section(SEONLEUNG, GANGNAM, new Distance(20));
            Sections sections = new Sections(List.of(jamsilSeonleungSection, gangnamToSeonleung));

            assertThatThrownBy(() -> sections.deleteSection(YUKSAM))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("현재 노선에 존재하지 않는 역을 삭제할 수 없습니다.");
        }

        @Test
        void 하행종점역_삭제() {
            Section jamsilSeonleungSection = new Section(JAMSIL, SEONLEUNG, new Distance(10));
            Section gangnameSeonleungSection = new Section(SEONLEUNG, GANGNAM, new Distance(3));
            Sections sections = new Sections(List.of(jamsilSeonleungSection, gangnameSeonleungSection));

            DeleteResult deleteResult = sections.deleteSection(GANGNAM);

            List<Section> deletedSections = deleteResult.getDeletedSections();
            List<Section> addedSections = deleteResult.getAddedSections();

            assertAll(
                    () -> assertThat(addedSections).isEmpty(),
                    () -> assertThat(deletedSections).containsExactly(gangnameSeonleungSection),
                    () -> assertThat(deleteResult.isLastSection()).isFalse()
            );
        }

        @Test
        void 상행종점역_삭제() {
            Section jamsilSeonleungSection = new Section(JAMSIL, SEONLEUNG, new Distance(10));
            Section gangnameSeonleungSection = new Section(SEONLEUNG, GANGNAM, new Distance(3));
            Sections sections = new Sections(List.of(jamsilSeonleungSection, gangnameSeonleungSection));

            DeleteResult deleteResult = sections.deleteSection(JAMSIL);

            List<Section> deletedSections = deleteResult.getDeletedSections();
            List<Section> addedSections = deleteResult.getAddedSections();

            assertAll(
                    () -> assertThat(addedSections).isEmpty(),
                    () -> assertThat(deletedSections).containsExactly(jamsilSeonleungSection),
                    () -> assertThat(deleteResult.isLastSection()).isFalse()
            );
        }

        @Test
        void 양옆에_역이_존재하는_중간_역_삭제() {
            Section jamsilSeonleungSection = new Section(JAMSIL, SEONLEUNG, new Distance(10));
            Section gangnameSeonleungSection = new Section(SEONLEUNG, GANGNAM, new Distance(3));
            Sections sections = new Sections(List.of(jamsilSeonleungSection, gangnameSeonleungSection));

            DeleteResult deleteResult = sections.deleteSection(SEONLEUNG);

            List<Section> deletedSections = deleteResult.getDeletedSections();
            Section addedSection = deleteResult.getAddedSections().get(0);

            Section newSection = new Section(JAMSIL, GANGNAM, jamsilSeonleungSection.calculateCombineDistance(gangnameSeonleungSection));

            assertAll(
                    () -> assertThat(deletedSections).containsExactlyInAnyOrder(jamsilSeonleungSection, gangnameSeonleungSection),
                    () -> assertThat(addedSection.getDistance()).isEqualTo(newSection.getDistance()),
                    () -> assertThat(addedSection.getDownStation()).isEqualTo(newSection.getDownStation()),
                    () -> assertThat(addedSection.getUpStation()).isEqualTo(newSection.getUpStation()),
                    () -> assertThat(deleteResult.isLastSection()).isFalse()
            );
        }
    }

    @Test
    void 라인에_맞는_지하철_역_조회() {
        Section jamsilSeonleungSection = new Section(JAMSIL, SEONLEUNG, new Distance(10));
        Section gangnamToSeonleung = new Section(YUKSAM, GANGNAM, new Distance(20));
        Section seonleungYuksam = new Section(SEONLEUNG, YUKSAM, new Distance(15));
        Sections sections = new Sections(List.of(jamsilSeonleungSection, gangnamToSeonleung, seonleungYuksam));

        List<Station> stations = sections.orderStations();
        assertThat(stations).containsExactly(JAMSIL, SEONLEUNG, YUKSAM, GANGNAM);
    }
}
