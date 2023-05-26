package subway.line.application;

import static org.junit.jupiter.api.Assertions.assertEquals;

import helper.IntegrationTestHelper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import subway.line.domain.Section;

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
