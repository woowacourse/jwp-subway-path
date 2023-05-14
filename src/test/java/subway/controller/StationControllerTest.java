//package subway.ui;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import subway.application.StationService;
//import subway.dto.StationCreateRequest;
//import subway.dto.StationResponse;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(StationController.class)
//class StationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @MockBean
//    private StationService stationService;
//
//    @Test
//    @DisplayName("역을 추가한다.")
//    void create_station() throws Exception {
//        final StationCreateRequest request = new StationCreateRequest("2호선", "종합운동장역", "잠실새내역", false, 2.3);
//        final String content = objectMapper.writeValueAsString(request);
//        final StationResponse response = new StationResponse(1L, "종합운동장역");
//
//        when(stationService.saveStation(any())).thenReturn(response);
//
//        mockMvc.perform(post("/stations")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON_VALUE))
//                .andDo(print())
//                .andExpect(status().isCreated());
//    }
//}
