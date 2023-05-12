package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import subway.SubwayJdbcFixture;
import subway.dao.StationDao;
import subway.dao.entity.SectionEntity;
import subway.domain.Distance;
import subway.domain.Line;
import subway.domain.Section;
import subway.domain.Station;
import subway.dto.SectionDeleteRequest;
import subway.dto.SectionRequest;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
class SectionServiceTest extends SubwayJdbcFixture {

    private SectionService sectionService;
    private SectionRepository sectionRepository;

    @BeforeEach
    void init() {
        final StationDao stationDao = new StationDao(jdbcTemplate, dataSource);
        sectionRepository = new SectionRepository(sectionDao);
        sectionService = new SectionService(lineDao, stationDao, sectionRepository);
    }

    @Test
    void 하행역_종점에_구간을_추가한다() {
        // given
        final Long 역삼역 = stationDao.insert(new Station("역삼역")).getId();

        // when
        final Long id = sectionService.insertSection(new SectionRequest(3, 선릉역, 역삼역, 이호선));

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void 상행역_종점에_구간을_추가한다() {
        // given
        final Long 잠실나루역 = stationDao.insert(new Station("잠실나루역")).getId();

        // when
        final Long id = sectionService.insertSection(new SectionRequest(3, 잠실나루역, 잠실역, 이호선));

        // 4 -> 1 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 중간에_추가하는데_상행역_기준인_경우() {
        // given
        final Long 하행역 = stationDao.insert(new Station("하행역")).getId();

        // when
        final Long id = sectionService.insertSection(new SectionRequest(3, 잠실역, 하행역, 이호선));

        // 1 -> 4 -> 2 -> 3
        assertThat(id).isNotNull();
    }

    @Test
    void 중간에_추가하는데_하행역_기준인_경우() {
        // given
        final Long 상행역 = stationDao.insert(new Station("상행역")).getId();

        // when
        final Long id = sectionService.insertSection(new SectionRequest(3, 상행역, 잠실새내역, 이호선));

        // then
        assertThat(id).isNotNull();
    }

    @Test
    void 같은_역_두_개를_등록할_수_없다() {
        // given
        final Long 같은역 = stationDao.insert(new Station("같은역")).getId();
        final SectionRequest request = new SectionRequest(3, 같은역, 같은역, 이호선);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 동일한_구간은_등록할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(3, 잠실역, 잠실새내역, 이호선);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 역이_존재하지_않으면_추가할_수_있다() {
        // given
        final SectionRequest request = new SectionRequest(3, 잠실역, 50L, 이호선);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void 추가하려는_거리는_기존_거리보다_클_수_없다() {
        // given
        final Long 삽입역 = stationDao.insert(new Station("삽입역")).getId();
        final SectionRequest request = new SectionRequest(100, 잠실역, 삽입역, 이호선);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 노선이_없으면_추가할_수_없다() {
        // given
        final SectionRequest request = new SectionRequest(1, 잠실역, 선릉역, 3L);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 구간이_한개이면_구간과_노선이_모두_삭제된다() {
        //given
        final Long 삼호선 = lineDao.insert(new Line("3호선", "노란색"));
        sectionDao.insert(new SectionEntity(10, 잠실역, 선릉역, 삼호선));

        //when
        sectionService.deleteStation(잠실역, new SectionDeleteRequest(삼호선));

        //then
        assertAll(
                () -> assertThat(lineDao.findById(삼호선)).isEmpty(),
                () -> assertThat(sectionDao.findAllByLineId(삼호선)).isEmpty()
        );
    }

    @Test
    void 구간이_두개_이상이고_종점이면_해당구간만_삭제한다() {
        //when
        sectionService.deleteStation(잠실역, new SectionDeleteRequest(이호선));

        //then
        assertThat(sectionDao.findAllByLineId(이호선)).hasSize(2);
    }

    @Test
    void 구간이_두개_이상이고_중간이면_겹치는구간_삭제와_이어주기를_한다() {
        //when
        sectionService.deleteStation(잠실새내역, new SectionDeleteRequest(이호선));

        //then
        final List<Section> sections = sectionRepository.findAllByLineId(이호선).getSections();
        assertAll(() -> assertThat(sections).hasSize(2),
                () -> assertThat(sections.get(0).getUpStation().getId()).isEqualTo(잠실역),
                () -> assertThat(sections.get(0).getDownStation().getId()).isEqualTo(삼성역),
                () -> assertThat(sections.get(0).getDistance()).isEqualTo(new Distance(30))
        );
    }
}
