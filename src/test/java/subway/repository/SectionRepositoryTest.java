package subway.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import subway.domain.section.Section;
import subway.domain.station.Station;
import subway.integration.IntegrationTest;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static subway.common.fixture.DomainFixture.디노;
import static subway.common.fixture.DomainFixture.디노_조앤;
import static subway.common.fixture.DomainFixture.조앤;
import static subway.common.fixture.DomainFixture.후추;
import static subway.common.fixture.DomainFixture.후추_디노;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class SectionRepositoryTest extends IntegrationTest {

    @Autowired
    private SectionRepository sectionRepository;
    @Autowired
    private DataSource dataSource;

    private SimpleJdbcInsert stationSimpleInsert;
    private SimpleJdbcInsert sectionSimpleInsert;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        stationSimpleInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("station")
                .usingGeneratedKeyColumns("id");
        sectionSimpleInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("section")
                .usingGeneratedKeyColumns("id");
    }

    @Test
    void 수정된_구간_정보를_저장한다() {
        //given
        insertStation(후추);
        insertStation(디노);
        insertStation(조앤);
        insertSection(1L, 후추_디노);
        insertSection(1L, 디노_조앤);

        //when
        final List<Section> sections = sectionRepository.saveUpdatedSections(List.of(new Section(후추, 조앤, 후추_디노.getDistanceValue() + 디노_조앤.getDistanceValue())), 1L);
        
        //then
        assertSoftly(softly -> {
            softly.assertThat(sections).hasSize(1);
            final Section section = sections.get(0);
            softly.assertThat(section.getFrom()).isEqualTo(후추);
            softly.assertThat(section.getTo()).isEqualTo(조앤);
            softly.assertThat(section.getDistanceValue()).isEqualTo(11);
        });
    }

    private void insertStation(final Station station) {
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("name", station.getName());
        stationSimpleInsert.execute(param);
    }

    private void insertSection(final Long lineId, final Section section) {
        final SqlParameterSource param = new MapSqlParameterSource()
                .addValue("from_id", section.getFrom().getId())
                .addValue("to_id", section.getTo().getId())
                .addValue("distance", section.getDistanceValue())
                .addValue("line_id", lineId);
        sectionSimpleInsert.execute(param);
    }
}
