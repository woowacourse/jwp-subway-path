package subway.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import subway.application.service.FeeService;
import subway.application.service.LineService;
import subway.application.service.PathService;

@WebMvcTest({LineController.class, PathController.class, FeeController.class})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected LineService lineService;

    @MockBean
    protected PathService pathService;

    @MockBean
    protected FeeService feeService;
}
