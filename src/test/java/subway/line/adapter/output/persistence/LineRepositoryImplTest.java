package subway.line.adapter.output.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import subway.line.adapter.output.persistence.LineDao;
import subway.line.adapter.output.persistence.LineEntity;
import subway.line.adapter.output.persistence.LineRepositoryImpl;
import subway.section.adapter.output.persistence.SectionDao;
import subway.section.adapter.output.persistence.SectionEntity;
import subway.line.domain.Line;
import subway.line.application.port.output.LineRepository;
import subway.section.domain.Sections;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LineRepositoryImplTest {

    @Mock
    private LineDao lineDao;
    @Mock
    private SectionDao sectionDao;

    private LineRepository lineRepository;

    @BeforeEach
    void setUp() {
        lineRepository = new LineRepositoryImpl(lineDao, sectionDao);
    }

    @Test
    void 노선_조회_테스트() {
        //given
        final Long lineId = 1L;
        given(lineDao.findById(anyLong())).willReturn(new LineEntity("1호선", "파랑"));
        given(sectionDao.findByLineId(anyLong())).willReturn(List.of(
                new SectionEntity("강남", "역삼", 3, lineId),
                new SectionEntity("역삼", "선릉", 2, lineId)
        ));

        //when
        final Line line = lineRepository.findById(lineId);

        //then
        assertThat(line.getLineName()).isEqualTo("1호선");
        final Sections sections = line.getSections();
        assertThat(sections.getSections()).hasSize(2);
    }

}
