package subway.domain.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static subway.exception.ErrorCode.SECTION_ADD_STATION_NOT_EXISTS;
import static subway.exception.ErrorCode.SECTION_ALREADY_ADD;
import static subway.exception.ErrorCode.SECTION_TOO_FAR_DISTANCE;
import static subway.fixture.StationFixture.강남역;
import static subway.fixture.StationFixture.선릉역;
import static subway.fixture.StationFixture.신림역;
import static subway.fixture.StationFixture.잠실역;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import subway.domain.station.Station;
import subway.exception.BadRequestException;

class SubwayLineTest {

    @Test
    @DisplayName("노선에 아무 역도 존재하지 않으면 검증하지 않는다.")
    void validateSections_empty() {
        // given
        final SubwayLine subwayLine = new SubwayLine(new ArrayList<>());
        final Section requestSection = new Section(신림역, 강남역, 10);

        // expected
        assertDoesNotThrow(() -> subwayLine.validateSections(requestSection));
    }

    @Test
    @DisplayName("추가 요청받은 역 모두 다 노선에 존재하지 않으면 예외가 발생한다.")
    void validateSections_not_exist() {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);
        final Section requestSection = new Section(신림역, 강남역, 10);

        // expected
        assertThatThrownBy(() -> subwayLine.validateSections(requestSection))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(SECTION_ADD_STATION_NOT_EXISTS);
    }

    @ParameterizedTest(name = "추가 요청받은 역 모두 다 노선에 존재하면 예외가 발생한다.")
    @CsvSource(value = {"잠실역:선릉역", "선릉역:잠실역"}, delimiter = ':')
    void validateSections_exist(final String sourceStationName, final String targetStationName) {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Station sourceStation = Station.create(sourceStationName);
        final Station targetStation = Station.create(targetStationName);
        final Section requestSection = new Section(sourceStation, targetStation, 10);

        // expected
        assertThatThrownBy(() -> subwayLine.validateSections(requestSection))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(SECTION_ALREADY_ADD);
    }

    @Test
    @DisplayName("노선에 존재하는 역이 없다면, 요청받은 구간은 새로운 구간으로 판단한다.")
    void isNewSection_empty() {
        // given
        final SubwayLine subwayLine = new SubwayLine(new ArrayList<>());
        final Section requestSection = new Section(잠실역, 선릉역, 10);

        // expected
        assertThat(subwayLine.isNewSection(requestSection))
            .isTrue();
    }

    @ParameterizedTest(name = "요청받은 출발역이 하행 종점이거나 도착역이 상행 종점이라면 새로운 구간으로 판단한다.")
    @CsvSource(value = {"산성역:잠실역:true", "선릉역:산성역:true", "잠실역:산성역:false", "산성역:선릉역:false"}, delimiter = ':')
    void isNewSection(final String sourceStationName, final String targetStationName, final boolean expected) {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Station sourceStation = Station.create(sourceStationName);
        final Station targetStation = Station.create(targetStationName);
        final Section requestSection = new Section(sourceStation, targetStation, 10);

        // expected
        assertThat(subwayLine.isNewSection(requestSection))
            .isSameAs(expected);
    }

    @Test
    @DisplayName("요청받은 시작역이 노선 구간의 시작역에 없으면 빈 값을 반환한다.")
    void getExistsSectionOfSource_empty() {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Section requestSection = new Section(신림역, 잠실역, 10);

        // expected
        assertThat(subwayLine.getExistsSectionOfSource(requestSection)).isEmpty();
    }

    @ParameterizedTest(name = "요청받은 시작역이 노선 구간의 시작역에 있지만, 요청받은 거리가 노선 구간 사이의 거리보다 더 크거나 같다면 예외가 발생한다.")
    @ValueSource(ints = {10, 11})
    void getExistsSectionOfSource_distance_exception(final int distance) {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Section requestSection = new Section(잠실역, 신림역, distance);

        // expected
        assertThatThrownBy(() -> subwayLine.getExistsSectionOfSource(requestSection))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(SECTION_TOO_FAR_DISTANCE);
    }

    @Test
    @DisplayName("요청받은 시작역이 노선 구간의 시작역에 있고, 요청받은 거리가 노선 구간 사이의 거리보다 작다면 해당 구간을 반환한다.")
    void getExistsSectionOfSource() {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Section requestSection = new Section(잠실역, 신림역, 8);

        // expected
        assertThat(subwayLine.getExistsSectionOfSource(requestSection))
            .isEqualTo(Optional.ofNullable(section));
    }

    @Test
    @DisplayName("요청받은 끝역이 노선 구간의 끝역에 없으면 빈 값을 반환한다.")
    void getExistsSectionOfTarget_empty() {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Section requestSection = new Section(신림역, 잠실역, 10);

        // expected
        assertThat(subwayLine.getExistsSectionOfTarget(requestSection))
            .isEmpty();
    }

    @ParameterizedTest(name = "요청받은 끝역이 노선 구간의 끝역에 있지만, 요청받은 거리가 노선 구간 사이의 거리보다 더 크거나 같다면 예외가 발생한다.")
    @ValueSource(ints = {10, 11})
    void getExistsSectionOfTarget_distance_exception(final int distance) {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Section requestSection = new Section(신림역, 선릉역, distance);

        // expected
        assertThatThrownBy(() -> subwayLine.getExistsSectionOfTarget(requestSection))
            .isInstanceOf(BadRequestException.class)
            .extracting("errorCode")
            .isEqualTo(SECTION_TOO_FAR_DISTANCE);
    }

    @Test
    @DisplayName("요청받은 끝이 노선 구간의 끝역에 있고, 요청받은 거리가 노선 구간 사이의 거리보다 작다면 해당 구간을 반환한다.")
    void getExistsSectionOfTarget() {
        // given
        final Section section = new Section(잠실역, 선릉역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(section);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        final Section requestSection = new Section(신림역, 선릉역, 8);

        // expected
        assertThat(subwayLine.getExistsSectionOfTarget(requestSection))
            .isEqualTo(Optional.ofNullable(section));
    }

    @Test
    @DisplayName("입력받은 역이 구간의 시작점일 때 빈 값을 반환한다.")
    void combineSection_upward() {
        // given
        final Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
        final Section 선릉_강남 = new Section(선릉역, 강남역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(잠실_선릉);
        savedSections.add(선릉_강남);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        // when
        final Optional<Section> newSection = subwayLine.combineSection(잠실역);

        // then
        assertThat(newSection)
            .isEmpty();
    }

    @Test
    @DisplayName("입력받은 역이 구간의 끝점일 때 빈 값을 반환한다.")
    void combineSection_downward() {
        // given
        final Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
        final Section 선릉_강남 = new Section(선릉역, 강남역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(잠실_선릉);
        savedSections.add(선릉_강남);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        // when
        final Optional<Section> newSection = subwayLine.combineSection(강남역);

        // then
        assertThat(newSection)
            .isEmpty();
    }

    @Test
    @DisplayName("두 구간을 합쳐서 새로운 거리를 가진 구간을 생성한다.")
    void combineSection() {
        // given
        final Section 잠실_선릉 = new Section(잠실역, 선릉역, 10);
        final Section 선릉_강남 = new Section(선릉역, 강남역, 10);
        final List<Section> savedSections = new ArrayList<>();
        savedSections.add(잠실_선릉);
        savedSections.add(선릉_강남);
        final SubwayLine subwayLine = new SubwayLine(savedSections);

        // when
        final Optional<Section> newSection = subwayLine.combineSection(선릉역);

        // then
        assertAll(
            () -> assertThat(newSection.get().source().equals(잠실역)),
            () -> assertThat(newSection.get().target().equals(강남역)),
            () -> assertThat(newSection.get().distance().distance() == 20)
        );
    }
}
