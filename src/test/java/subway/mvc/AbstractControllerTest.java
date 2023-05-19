package subway.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.line.port.in.LineAddInterStationUseCase;
import subway.application.line.port.in.LineCreateUseCase;
import subway.application.line.port.in.LineDeleteUseCase;
import subway.application.line.port.in.LineFindAllUseCase;
import subway.application.line.port.in.LineFindByIdUseCase;
import subway.application.line.port.in.LineUpdateInfoUseCase;
import subway.application.route.port.in.find.RouteFindUseCase;
import subway.application.station.port.in.StationCreateUseCase;
import subway.application.station.port.in.StationDeleteUseCase;
import subway.application.station.port.in.StationFindAllUseCase;
import subway.application.station.port.in.StationFindByIdUseCase;
import subway.application.station.port.in.StationUpdateInfoUseCase;

@WebMvcTest
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected StationUpdateInfoUseCase stationUpdateInfoUseCase;

    @MockBean
    protected StationFindByIdUseCase stationFindByIdUseCase;

    @MockBean
    protected StationFindAllUseCase stationFindAllUseCase;

    @MockBean
    protected StationCreateUseCase stationCreateUseCase;

    @MockBean
    protected StationDeleteUseCase stationDeleteUseCase;

    @MockBean
    protected LineCreateUseCase lineCreateUseCase;

    @MockBean
    protected LineUpdateInfoUseCase lineUpdateInfoUseCase;

    @MockBean
    protected LineDeleteUseCase lineDeleteUseCase;

    @MockBean
    protected LineFindAllUseCase lineFindAllUseCase;

    @MockBean
    protected LineFindByIdUseCase lineFindByIdUseCase;

    @MockBean
    protected LineAddInterStationUseCase lineAddInterStationUseCase;

    @MockBean
    protected RouteFindUseCase routeFindUseCase;
}
