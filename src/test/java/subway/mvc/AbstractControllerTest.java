package subway.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.line.application.port.in.addinterstation.LineAddInterStationUseCase;
import subway.line.application.port.in.create.LineCreateUseCase;
import subway.line.application.port.in.delete.LineDeleteUseCase;
import subway.line.application.port.in.findById.LineFindByIdUseCase;
import subway.line.application.port.in.findall.LineFindAllUseCase;
import subway.line.application.port.in.update.LineUpdateInfoUseCase;
import subway.route.application.port.in.find.RouteFindUseCase;
import subway.station.application.port.in.StationCreateUseCase;
import subway.station.application.port.in.StationDeleteUseCase;
import subway.station.application.port.in.StationFindAllUseCase;
import subway.station.application.port.in.StationFindByIdUseCase;
import subway.station.application.port.in.StationUpdateInfoUseCase;

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
