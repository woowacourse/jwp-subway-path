package subway.application.strategy.delete;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import subway.application.strategy.StrategyFixture;
import subway.dao.SectionDao;
import subway.dao.SectionStationDao;
import subway.dao.entity.LineEntity;
import subway.dao.entity.SectionEntity;
import subway.domain.Distance;
import subway.domain.Station;
import subway.domain.section.Section;
import subway.domain.section.SingleLineSections;
import subway.repository.SectionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SuppressWarnings("NonAsciiCharacters")
@Import({
        SectionDao.class, SectionStationDao.class, SectionRepository.class,
        DeleteInitialSection.class, DeleteUpTerminal.class, DeleteDownTerminal.class, DeleteBetweenStation.class}
)
class DeleteStationStrategyTest extends StrategyFixture {

    private SectionDeleter sectionDeleter;

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private DeleteInitialSection deleteInitialSection;
    @Autowired
    private DeleteUpTerminal deleteUpTerminal;
    @Autowired
    private DeleteDownTerminal deleteDownTerminal;
    @Autowired
    private DeleteBetweenStation deleteBetweenStation;
    private SingleLineSections 이호선_구간;

    @BeforeEach
    void init() {
        final List<DeleteStationStrategy> strategies = List.of(deleteInitialSection, deleteUpTerminal, deleteDownTerminal, deleteBetweenStation);
        sectionDeleter = new SectionDeleter(strategies);
         이호선_구간 = sectionRepository.findAllByLineId(이호선);
    }

    @Test
    void 구간이_한_개이면_구간과_노선이_모두_삭제된다() {
        // given
        final Station 광교중앙역 = createStation("광교중앙역");
        final Station 상현역 = createStation("상현역");
        final Long 신분당선 = lineDao.insert(new LineEntity("신분당선", "빨강색"));

        final Long sectionId = sectionDao.insert(new SectionEntity(10, 광교중앙역.getId(), 상현역.getId(), 신분당선));
        final List<Section> sections = List.of(new Section(sectionId, Distance.from(10), 광교중앙역, 상현역, 신분당선));
        final SingleLineSections singleLineSections = SingleLineSections.from(sections);

        // when
        sectionDeleter.delete(singleLineSections, 상현역);

        // then
        assertAll(
                () -> assertThat(lineDao.findById(신분당선)).isNotPresent(),
                () -> assertThat(sectionStationDao.findAllByLineId(신분당선)).isEmpty()
        );
    }

    @Test
    void 구간이_두_개_이상이고_상행_종점이면_해당_구간만_삭제한다() {
        // when
        sectionDeleter.delete(이호선_구간, 잠실역);

        // then
        assertThat(sectionStationDao.findAllByLineId(이호선)).hasSize(2);
    }

    @Test
    void 구간이_두_개_이상이고_하행_종점이면_해당_구간만_삭제한다() {
        // when
        sectionDeleter.delete(이호선_구간, 선릉역);

        // then
        assertThat(sectionStationDao.findAllByLineId(이호선)).hasSize(2);
    }

    @Test
    void 구간이_두_개_이상이고_중간이면_겹치는_구간을_삭제하고_이어주기를_한다() {
        //when
        sectionDeleter.delete(이호선_구간, 잠실새내역);

        //then
        final List<Section> sections = sectionRepository.findAllByLineId(이호선).getSections();
        assertAll(() -> assertThat(sections).hasSize(2),
                () -> assertThat(sections.get(0).getUpStation()).isEqualTo(잠실역),
                () -> assertThat(sections.get(0).getDownStation()).isEqualTo(삼성역),
                () -> assertThat(sections.get(0).getDistance()).isEqualTo(Distance.from(30))
        );
    }
}
