package subway.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import subway.application.LineService;
import subway.dao.LineDao;
import subway.dao.SectionDao;
import subway.dao.StationDao;

@ExtendWith(MockitoExtension.class)
public class LineServiceTest {
    @Mock
    private LineDao lineDao;

    @Mock
    private StationDao stationDao;

    @Mock
    private SectionDao sectionDao;

    @InjectMocks
    private LineService lineService;
}
