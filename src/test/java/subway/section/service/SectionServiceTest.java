package subway.section.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.domain.line.dao.SectionDao;
import subway.domain.line.dto.SectionRequest;
import subway.domain.line.entity.SectionEntity;
import subway.domain.line.service.SectionService;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
public class SectionServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private SectionService sectionService;

    @BeforeEach
    void init() {
        SectionDao sectionDao = new SectionDao(jdbcTemplate, dataSource);
        sectionService = new SectionService(sectionDao);
    }

    @DisplayName("라인에 역 삭제시 역이 존재하지 않는 경우 테스트")
    @Test
    void 라인에_역_삭제시_역이_존재하지_않는_경우_테스트() {
        assertThatThrownBy(() -> sectionService.deleteSection(1L, 8L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되어있지 않은 역은 지울 수 없습니다.");
    }

    @DisplayName("라인에 역 삭제시 라인이 존재하지 않는 경우 테스트")
    @Test
    void 라인에_역_삭제시_라인이_존재하지_않는_경우_테스트() {
        assertThatThrownBy(() -> sectionService.deleteSection(3L, 8L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("등록되어있지 않은 역은 지울 수 없습니다.");
    }


    @DisplayName("첫번째 역 삭제 테스트")
    @Test
    void 첫번째_역_삭제_테스트() {
        sectionService.deleteSection(1L, 1L);
        List<SectionEntity> sections = sectionService.findByLineId(1L);
        assertThat(sections.get(0)).isEqualTo(
                new SectionEntity(2L, 1L, 2L, 3L, 5)
        );
    }

    @DisplayName("첫번째 역 삭제 테스트")
    @Test
    void 마지막_역_삭제_테스트() {
        sectionService.deleteSection(1L, 7L);
        List<SectionEntity> sections = sectionService.findByLineId(1L);
        assertThat(sections.get(sections.size() - 1)).isEqualTo(
                new SectionEntity(5L, 1L, 5L, 6L, 5)
        );
    }

    @DisplayName("중간 역 삭제 테스트")
    @Test
    void 중간_역_삭제_테스트1() {
        sectionService.deleteSection(1L, 2L);
        List<SectionEntity> sections = sectionService.findByLineId(1L);
        assertThat(sections.get(0)).isEqualTo(
                new SectionEntity(1L, 1L, 1L, 3L, 10)
        );
    }

    @DisplayName("중간 역 삭제 테스트")
    @Test
    void 중간_역_삭제_테스트2() {
        sectionService.deleteSection(1L, 5L);
        List<SectionEntity> sections = sectionService.findByLineId(1L);
        assertThat(sections.get(3)).isEqualTo(
                new SectionEntity(4L, 1L, 4L, 6L, 10)
        );
    }

    @DisplayName("첫번째 역 앞에 역 추가")
    @Test
    void 첫번째_역_앞에_역_추가() {
        SectionRequest request = new SectionRequest(1L, 1L, 8L, 5);
        final List<SectionEntity> sectionEntities = sectionService.createSection(request);
        assertThat(sectionEntities.get(0)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 1L, 8L, 5));
    }

    @DisplayName("마지막 역 뒤에 역 추가")
    @Test
    void 마지막_역_뒤에_역_추가() {
        SectionRequest request = new SectionRequest(1L, 8L, 7L, 5);
        final List<SectionEntity> sectionEntities = sectionService.createSection(request);
        assertThat(sectionEntities.get(0)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 8L, 7L, 5));
    }

    @DisplayName("역 중간에 역 추가")
    @Test
    void 역_중간에_역_추가() {
        SectionRequest request = new SectionRequest(1L, 2L, 8L, 3);
        final List<SectionEntity> sections = sectionService.createSection(request);

        assertThat(sections.get(0)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 2L, 8L, 3));
        assertThat(sections.get(1)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 8L, 3L, 2));
    }

    @DisplayName("역 중간에 역 추가2")
    @Test
    void 역_중간에_역_추가2() {
        SectionRequest request = new SectionRequest(1L, 8L, 3L, 3);
        final List<SectionEntity> sections = sectionService.createSection(request);

        assertThat(sections.get(1)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 2L, 8L, 2));
        assertThat(sections.get(0)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 8L, 3L, 3));
    }

    @DisplayName("역 중간에 역 추가2")
    @Test
    void 역_중간에_역_추가3() {
        SectionRequest request = new SectionRequest(1L, 9L, 5L, 3);
        final List<SectionEntity> sections = sectionService.createSection(request);

        assertThat(sections.get(1)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 4L, 9L, 2));
        assertThat(sections.get(0)).usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(new SectionEntity(null, 1L, 9L, 5L, 3));
    }
}
