package subway.mvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
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
}
