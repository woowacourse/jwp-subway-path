package subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static subway.integration.TestFixture.잠실나루역_잠실역;
import static subway.integration.TestFixture.잠실역_잠실새내역;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.domain.Line;
import subway.domain.Line2;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class LineRepositoryTest {

    private static final long LINE_ID = 1L;
    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "green";
    @InjectMocks
    private LineRepository lineRepository;
    @Mock
    LineDao lineDao;
    @Mock
    private SectionRepository sectionRepository;

    private Line2 line;

    @BeforeEach
    void setUp() {
        Line2 line = new Line2(LINE_NAME, LINE_COLOR);
        line.add(잠실나루역_잠실역);
        line.add(잠실역_잠실새내역);
        this.line = line;
    }

    @DisplayName("호선을 저장한다")
    @Test
    void save() {
        doReturn(new Line(LINE_ID, LINE_NAME, LINE_COLOR)).when(lineDao).insert(any());
        doNothing().when(sectionRepository).saveAll(anyLong(), anyList());

        lineRepository.save(line);

        verify(sectionRepository, times(1)).saveAll(anyLong(), eq(line.getSections()));
    }

    @DisplayName("호선을 수정한다")
    @Test
    void update() {
        doNothing().when(sectionRepository).saveAll(anyLong(), anyList());
        Line2 savedLine = new Line2(LINE_ID, LINE_NAME, LINE_COLOR, line.getSections());

        lineRepository.save(savedLine);

        verify(lineDao, never()).insert(any());
        verify(sectionRepository, times(1)).saveAll(anyLong(), eq(line.getSections()));
    }

    @DisplayName("id로 호선을 조회한다")
    @Test
    void findById() {
        doReturn(new Line(LINE_ID, LINE_NAME, LINE_COLOR)).when(lineDao).findById(any());
        doReturn(List.of(
                잠실나루역_잠실역,
                잠실역_잠실새내역
        )).when(sectionRepository).findAllBy(anyLong());

        Line2 found = lineRepository.findBy(LINE_ID);

        verify(sectionRepository, times(1)).findAllBy(eq(LINE_ID));
        assertThat(found.getSections()).containsExactly(
                잠실나루역_잠실역,
                잠실역_잠실새내역
        );
    }
}
