package subway.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.dto.request.SectionSaveRequest;
import subway.dto.response.StationResponse;
import subway.exception.business.InvalidSectionLengthException;
import subway.exception.business.SectionNotFoundException;
import subway.exception.business.StationNotFoundException;
import subway.integration.IntegrationTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Sql("/setUpStation.sql")
public class SectionServiceTest extends IntegrationTest {

    @Autowired
    SectionService sectionService;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Sql("/setUpStation.sql")
    @Test
    @DisplayName("최초로 구간을 등록한다.")
    void addInitialSection_success() {
        //when
        SectionSaveRequest request1_2 = new SectionSaveRequest(1L, 2L, 10);
        sectionService.addSection(1L, request1_2);

        //then
        List<StationResponse> stations = sectionService.findByLineId(1L);
        assertThat(stations.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("구간 삭제 - 역이 2개일 경우 역 제거 시도 시 해당 노선의 모든 역을 삭제한다.")
    void deleteSection_when_only_2_station_success() {
        //given
        initializeSections();
        sectionService.removeStation(3L, 1L);

        //when 현재상태: 1-2
        sectionService.removeStation(2L, 1L);

        assertThatThrownBy(() -> sectionService.findByLineId(1L))
                .isInstanceOf(SectionNotFoundException.class);
    }

    @Test
    @DisplayName("특정 노선의 정렬된 역들을 반환한다.")
    void findSortedSections_success() {
        //given 상태:1-2-3
        initializeSections();

        //when
        List<StationResponse> sortedStations = sectionService.findByLineId(1L);

        // then
        List<String> sortedNames = sortedStations.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());
        assertThat(sortedNames).isEqualTo(List.of("st1", "st2", "st3"));
    }

    @ParameterizedTest
    @MethodSource("provideRequestAndExpected")
    @DisplayName("노선에 역을 등록한다.")
    void add_Section_success(SectionSaveRequest request, List<String> expected) {
        //given 상태:1-2-3
        initializeSections();

        //when
        sectionService.addSection(1L, request);

        //then
        List<StationResponse> sortedStations = sectionService.findByLineId(1L);
        List<String> sortedNames = sortedStations.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(sortedNames).isEqualTo(expected);
    }

    private static Stream<Arguments> provideRequestAndExpected() {
        return Stream.of(
                Arguments.of(new SectionSaveRequest(3L, 4L, 5), List.of("st1", "st2", "st3", "st4")),
                Arguments.of(new SectionSaveRequest(4L, 1L, 5), List.of("st4", "st1", "st2", "st3")),
                Arguments.of(new SectionSaveRequest(4L, 3L, 5), List.of("st1", "st2", "st4", "st3")),
                Arguments.of(new SectionSaveRequest(1L, 4L, 5), List.of("st1", "st4", "st2", "st3"))
        );
    }

    @Test
    @DisplayName("구간 중간 - 길이가 기존 구간 이상인 구간을 중간에 등록하려하면 실패한다.")
    void addMiddleSection_distance_too_big_fail() {
        //given 상태:1-2-3
        initializeSections();

        //when
        SectionSaveRequest middleSectionRequest = new SectionSaveRequest(4L, 2L, 10);

        assertThatThrownBy(() -> sectionService.addSection(1L, middleSectionRequest))
                .isInstanceOf(InvalidSectionLengthException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"1, st2, st3", "3, st1, st2", "2, st1, st3"})
    @DisplayName("구간의 역 삭제")
    void delete_section_station_success(ArgumentsAccessor argumentsAccessor) {
        //given 상태:1-2-3
        initializeSections();

        //when
        sectionService.removeStation(argumentsAccessor.getLong(0), 1L);

        //then
        List<StationResponse> sortedStations = sectionService.findByLineId(1L);
        List<String> sortedNames = sortedStations.stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        assertThat(sortedNames).isEqualTo(List.of(argumentsAccessor.getString(1), argumentsAccessor.getString(2)));
    }

    @Test
    @DisplayName("구간 삭제 - 노선에 존재하지 않는 역을 삭제한다.")
    void delete_station_when_not_exist_fail() {
        //given 상태:1-2-3
        initializeSections();

        //when
        assertThatThrownBy(() -> sectionService.removeStation(100L, 1L))
                .isInstanceOf(StationNotFoundException.class);
    }

    private void initializeSections() {
        sectionService.addSection(1L, new SectionSaveRequest(1L, 2L, 10));
        sectionService.addSection(1L, new SectionSaveRequest(2L, 3L, 10));

    }
}
