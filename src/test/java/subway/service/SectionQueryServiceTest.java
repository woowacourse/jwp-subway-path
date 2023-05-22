package subway.service;

import helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.section.Section;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SectionQueryServiceTest extends IntegrationTestHelper {

    @Autowired
    private SectionQueryService sectionQueryService;

    @Test
    @DisplayName("searchSectionsByLineId() : 해당 line에 속한 모든 섹션들을 조회할 수 있다.")
    void test_searchSectionsByLineId() throws Exception {
        //given
        final long lineId = 1L;

        //when
        final List<Section> sections = sectionQueryService.searchSectionsByLineId(lineId);

        //then
        assertEquals(3, sections.size());
    }
}
