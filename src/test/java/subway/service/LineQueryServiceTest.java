package subway.service;

import helper.IntegrationTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import subway.domain.line.Line;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LineQueryServiceTest extends IntegrationTestHelper {

    @Autowired
    private LineQueryService lineQueryService;

    @Test
    @DisplayName("searchLines() : lineName이 존재하지 않으면 모든 line을 조회할 수 있다.")
    void test_searchLines_all() throws Exception {
        //given
        final String lineName = null;

        //when
        final List<Line> lines = lineQueryService.searchLines(lineName);

        //then
        assertEquals(2, lines.size());
    }

    @Test
    @DisplayName("searchLines() : lineName을 통해 해당 line을 조회할 수 있다.")
    void test_searchLines() throws Exception {
        //given
        final String lineName = "1호선";

        //when
        final List<Line> lines = lineQueryService.searchLines(lineName);

        //then
        assertAll(
                () -> assertEquals(1, lines.size()),
                () -> assertEquals(lines.get(0).getName(), lineName)
        );
    }

    @Test
    @DisplayName("searchAllLine() : 모든 Line을 조회할 수 있다.")
    void test_searchAllLine() throws Exception {
        //when
        final List<Line> lines = lineQueryService.searchAllLine();

        //then
        assertEquals(2, lines.size());
    }

    @Test
    @DisplayName("searchByLineName() : lineName을 통해 해당 line을 조회할 수 있다.")
    void test_searchByLineName() throws Exception {
        //given
        final String lineName = "1호선";

        //when
        final Line line = lineQueryService.searchByLineName(lineName);

        //then
        assertEquals(line.getName(), lineName);
    }

    @Test
    @DisplayName("searchByLineId() : line id를 통해 해당 line을 조회할 수 있다.")
    void test_searchByLineId() throws Exception {
        //given
        final Long lineId = 1L;

        //when
        final Line line = lineQueryService.searchByLineId(lineId);

        //then
        assertEquals(line.getId(), lineId);
    }

    @ParameterizedTest
    @CsvSource({
            "1호선, true",
            "2호선, true",
            "3호선, false"
    })
    @DisplayName("isExistLine() : lineName을 통해 해당 line이 존재하는지 알 수 있다.")
    void test_isExistLine(final String lineName, final boolean isExist) throws Exception {
        //then
        assertEquals(isExist, lineQueryService.isExistLine(lineName));
    }
}
