package subway.common.webmvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.line.application.LineCommandService;
import subway.line.application.LineQueryService;
import subway.route.application.RouteQueryService;
import subway.station.application.StationCommandService;
import subway.station.application.StationQueryService;

@WebMvcTest
public abstract class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected StationQueryService stationQueryService;

    @MockBean
    protected StationCommandService stationCommandService;

    @MockBean
    protected RouteQueryService routeQueryService;

    @MockBean
    protected LineQueryService lineQueryService;

    @MockBean
    protected LineCommandService lineCommandService;
}
