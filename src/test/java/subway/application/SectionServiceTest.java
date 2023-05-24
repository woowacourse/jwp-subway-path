package subway.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import subway.SubwayJdbcFixture;
import subway.application.strategy.delete.SectionDeleter;
import subway.application.strategy.insert.SectionInserter;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.dao.entity.StationEntity;
import subway.domain.Distance;
import subway.domain.section.Section;
import subway.dto.SectionRequest;
import subway.repository.SectionRepository;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@ImportSectionService
class SectionServiceTest extends SubwayJdbcFixture {

    @Autowired
    private SectionService sectionService;
    @Autowired
    private SubwayReadService subwayReadService;
    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private SectionInserter sectionInserter;
    @Autowired
    private SectionDeleter sectionDeleter;

    @BeforeEach
    void init() {
        sectionService = new SectionService(subwayReadService, sectionInserter, sectionDeleter);
    }

    @Nested
    class 종점에_구간을_추가한다 {

        @Test
        void 하행역_종점에_구간을_추가한다() {
            // given
            final Long 역삼역 = 역을_추가한다("역삼역");

            // when
            final Long id = sectionService.insertSection(이호선, new SectionRequest(3, 선릉역, 역삼역));

            // then
            assertThat(id).isNotNull();
        }

        @Test
        void 상행역_종점에_구간을_추가한다() {
            // given
            final Long 잠실나루역 = 역을_추가한다("잠실나루역");

            // when
            final Long id = sectionService.insertSection(이호선, new SectionRequest(3, 잠실나루역, 잠실역));

            // 4 -> 1 -> 2 -> 3
            assertThat(id).isNotNull();
        }
    }


    @Nested
    class 중간_지점에_구간을_추가한다 {

        @Test
        void 상행역_기준인_경우_성공한다() {
            // given
            final Long 하행역 = 역을_추가한다("하행역");

            // when
            final Long id = sectionService.insertSection(이호선, new SectionRequest(3, 잠실역, 하행역));

            // then
            assertThat(id).isNotNull();
        }

        @ParameterizedTest(name = "거리 : {0}")
        @ValueSource(ints = {10, 11})
        void 상행역_기준으로_추가하려는_거리는_기존_거리보다_클_수_없다(final int distance) {
            // given
            final Long 삽입역 = 역을_추가한다("삽입역");
            final SectionRequest request = new SectionRequest(distance, 잠실역, 삽입역);

            // when, then
            assertThatThrownBy(() -> sectionService.insertSection(이호선, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 하행역_기준인_경우_성공한다() {
            // given
            final Long 상행역 = 역을_추가한다("상행역");

            // when
            final Long id = sectionService.insertSection(이호선, new SectionRequest(3, 상행역, 잠실새내역));

            // then
            assertThat(id).isNotNull();
        }

        @ParameterizedTest(name = "거리 : {0}")
        @ValueSource(ints = {20, 21})
        void 하행역_기준으로_추가하려는_거리는_기존_거리보다_클_수_없다(final int distance) {
            // given
            final Long 삽입역 = 역을_추가한다("삽입역");
            final SectionRequest request = new SectionRequest(distance, 삽입역, 잠실새내역);

            // when, then
            assertThatThrownBy(() -> sectionService.insertSection(이호선, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void 같은_역_두_개를_등록할_수_없다() {
        // given
        final Long 같은역 = 역을_추가한다("같은역");
        final SectionRequest request = new SectionRequest(3, 같은역, 같은역);

        // when, then
        assertThatThrownBy(() -> sectionService.insertSection(이호선, request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Nested
    class 구간을_등록할_수_없는_경우 {

        @Test
        void 동일한_구간은_등록할_수_없다() {
            // given
            final SectionRequest request = new SectionRequest(3, 잠실역, 잠실새내역);

            // when, then
            assertThatThrownBy(() -> sectionService.insertSection(이호선, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 역이_존재하지_않으면_등록할_수_없다() {
            // given
            final SectionRequest request = new SectionRequest(3, 잠실역, 50L);

            // when, then
            assertThatThrownBy(() -> sectionService.insertSection(이호선, request))
                    .isInstanceOf(NoSuchElementException.class);
        }


        @Test
        void 노선이_없으면_등록할_수_없다() {
            // given
            final SectionRequest request = new SectionRequest(1, 잠실역, 선릉역);

            // when, then
            assertThatThrownBy(() -> sectionService.insertSection(10L, request))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private Long 역을_추가한다(String name) {
        return stationDao.insert(new StationEntity(name)).getId();
    }

    @Nested
    class 구간을_삭제하는_경우 {

        @Test
        void 구간이_한_개이면_구간과_노선이_모두_삭제된다() {
            //given
            final Long 삼호선 = lineDao.insert(new LineEntity("3호선", "노란색"));
            sectionDao.insert(new SectionEntity(10, 잠실역, 선릉역, 삼호선));

            //when
            sectionService.deleteStation(삼호선, 잠실역);

            //then
            assertAll(
                    () -> assertThat(lineDao.findById(삼호선)).isEmpty(),
                    () -> assertThat(sectionStationDao.findAllByLineId(삼호선)).isEmpty()
            );
        }

        @Test
        void 구간이_두_개_이상이고_상행_종점이면_해당_구간만_삭제한다() {
            //when
            sectionService.deleteStation(이호선, 잠실역);

            //then
            assertThat(sectionStationDao.findAllByLineId(이호선)).hasSize(2);
        }

        @Test
        void 구간이_두_개_이상이고_하행_종점이면_해당_구간만_삭제한다() {
            //when
            sectionService.deleteStation(이호선, 선릉역);

            //then
            assertThat(sectionStationDao.findAllByLineId(이호선)).hasSize(2);
        }

        @Test
        void 구간이_두_개_이상이고_중간이면_겹치는_구간을_삭제하고_이어주기를_한다() {
            //when
            sectionService.deleteStation(이호선, 잠실새내역);

            //then
            final List<Section> sections = sectionRepository.findAllByLineId(이호선).getSections();
            assertAll(() -> assertThat(sections).hasSize(2),
                    () -> assertThat(sections.get(0).getUpStation().getId()).isEqualTo(잠실역),
                    () -> assertThat(sections.get(0).getDownStation().getId()).isEqualTo(삼성역),
                    () -> assertThat(sections.get(0).getDistance()).isEqualTo(Distance.from(30))
            );
        }
    }
}
