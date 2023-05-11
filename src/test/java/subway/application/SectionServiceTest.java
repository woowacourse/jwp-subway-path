package subway.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static subway.fixture.SectionRequestFixture.*;
import static subway.fixture.StationFixture.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.dao.LineDao;
import subway.dao.StationDao;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.integration.IntegrationTest;

class SectionServiceTest extends IntegrationTest {

    public static final Line line1 = new Line("line1", "black");
    @Autowired
    SectionService sectionService;

    @Autowired
    StationDao stationDao;

    @Autowired
    LineDao lineDao;

    @Test
    @DisplayName("최초로 구간을 등록한다.")
    void addInitialSection_success() {
        long id = initInsert();
        assertThat(id).isEqualTo(1L);
    }

    @Test
    @DisplayName("하행종점에 구간을 등록한다.")
    void addDownEndSection_success() {
        //given
        initInsert();

        //when
        long id = sectionService.addSection(1L, SECTION_REQUEST2_3);
        assertThat(id).isEqualTo(2L);
    }

    @Test
    @DisplayName("상행종점에 구간을 등록한다.")
    void addUpEndSection_success() {
        //given
        initInsert();

        //when
        long id = sectionService.addSection(1L, SECTION_REQUEST5_1);
        assertThat(id).isEqualTo(2L);
    }


    @Test
    @DisplayName("구간 중간 - 새로운 길이가 더 큰 구간을 등록하려하면 실패한다.")
    void addMiddleSection_fail_distance_too_big() {
        //given
        initInsert();

        //when
        assertThatThrownBy(() -> sectionService.addSection(1L, SECTION_REQUEST3_2))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("구간 중간 - 오른쪽에 새로운 구간을 등록한다.")
    void addMiddleSection_innerRight_success() {
        //given
        initInsert();

        //when
        long id = sectionService.addSection(1L, SECTION_REQUEST3_2);
        assertThat(id).isEqualTo(2L);
    }


    @Test
    @DisplayName("구간 중간 - 왼쪽에 새로운 구간을 등록한다.")
    void addMiddleSection_innerLeft_success() {
        //given
        initInsert();

        //when
        long id = sectionService.addSection(1L, SECTION_REQUEST1_3);
        assertThat(id).isEqualTo(2L);
    }

    @Test
    @DisplayName("구간 삭제 - 역이 2개일 경우 역 제거 시도 시 해당 노선의 모든 역을 삭제한다.")
    void deleteSection_when_only_2_station_success() {
        //given
        initInsert();

        //when
        sectionService.removeStation(1L, 1L);
        assertThat(sectionService.findAllByLindId(1L).size()).isEqualTo(0);
    }

    @Test
    @DisplayName("구간 삭제 - 상행 종점을 삭제한다.")
    void deleteEndSection_UpEnd_success() {
        //given
        initInsert();
        sectionService.addSection(1L, SECTION_REQUEST2_3);

        //when
        sectionService.removeStation(1L, 1L);
        assertThat(sectionService.findAllByLindId(1L).getSections().get(0))
                .usingRecursiveComparison()
                .comparingOnlyFields("upStation")
                .comparingOnlyFields("downStation")
                .isEqualTo(new Section(station2, station3, 10));
    }

    @Test
    @DisplayName("구간 삭제 - 하행 종점을 삭제한다.")
    void deleteEndSection_downEnd_success() {
        //given
        initInsert();
        sectionService.addSection(1L, SECTION_REQUEST2_3);

        //when
        sectionService.removeStation(3L, 1L);
        assertThat(sectionService.findAllByLindId(1L).getSections().get(0))
                .usingRecursiveComparison()
                .comparingOnlyFields("upStation")
                .comparingOnlyFields("downStation")
                .isEqualTo(new Section(station1, station2, 10));
    }

    @Test
    @DisplayName("구간 삭제 - 구간 중간의 역을 삭제한다.")
    void deleteSection_middle_success() {
        //given
        initInsert();
        sectionService.addSection(1L, SECTION_REQUEST2_3);

        //when
        sectionService.removeStation(2L, 1L);
        assertThat(sectionService.findAllByLindId(1L).getSections().get(0))
                .usingRecursiveComparison()
                .comparingOnlyFields("upStation")
                .comparingOnlyFields("downStation")
                .isEqualTo(new Section(station1, station3, 20));
    }

    @Test
    @DisplayName("구간 삭제 - 노선에 존재하지 않는 역을 삭제한다.")
    void deleteSection_when_not_exist() {
        //given
        initInsert();
        sectionService.addSection(1L, SECTION_REQUEST2_3);

        //when
        assertThatThrownBy(() -> sectionService.removeStation(5L, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("특정 노선의 정렬된 전체 구간을 반환한다.")
    void findSortedSections_success() {
        //given
        initInsert();
        sectionService.addSection(1L, SECTION_REQUEST2_3);
        sectionService.addSection(1L, SECTION_REQUEST3_4);

        //when
        List<Station> sortedSections = sectionService.findSortedAllStationsByLindId(1L);

        // then
        assertThat(sortedSections)
                .usingRecursiveComparison()
                .comparingOnlyFields("name")
                .isEqualTo(List.of(station1, station2, station3, station4));
    }

    private long initInsert() {
        lineDao.insert(line1);
        stationDao.insert(station1);
        stationDao.insert(station2);
        stationDao.insert(station3);
        stationDao.insert(station4);
        stationDao.insert(station5);

        return sectionService.addSection(1L, SECTION_REQUEST1_2);
    }
}
