package subway.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator.ReplaceUnderscores;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import subway.dto.request.AddStationToLineRequest;
import subway.dto.request.DeleteStationFromLineRequest;
import subway.service.LineService;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(ReplaceUnderscores.class)
@DisplayName("LineStationController 은(는)")
@WebMvcTest(LineStationController.class)
class LineStationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LineService lineService;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    class 노선에_역을_추가_할떄 {

        @Test
        void 성공한다() throws Exception {
            // given
            AddStationToLineRequest request = new AddStationToLineRequest("1호선", "잠실역", "잠실나루역", 10);

            // when
            MvcResult mvcResult = 노선_역_추가_요청(request);

            // then
            MockHttpServletResponse response = mvcResult.getResponse();
            verify(lineService, times(1)).addStation(any());
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 호선이_널이나_공백이면_예외(String nullAndEmpty) throws Exception {
            // given
            AddStationToLineRequest request = new AddStationToLineRequest(nullAndEmpty, "잠실역", "잠실나루역", 10);

            // when
            MvcResult mvcResult = 노선_역_추가_요청(request);

            //then
            MockHttpServletResponse response = mvcResult.getResponse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 상행선이_널이나_공백이면_예외(String nullAndEmpty) throws Exception {
            // given
            AddStationToLineRequest request = new AddStationToLineRequest("1호선", nullAndEmpty, "잠실나루역", 10);

            // when
            MvcResult mvcResult = 노선_역_추가_요청(request);

            //then
            MockHttpServletResponse response = mvcResult.getResponse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 하행선_널이나_공백이면_예외(String nullAndEmpty) throws Exception {
            // given
            AddStationToLineRequest request = new AddStationToLineRequest("1호선", "잠실역", nullAndEmpty, 10);

            // when
            MvcResult mvcResult = 노선_역_추가_요청(request);

            //then
            MockHttpServletResponse response = mvcResult.getResponse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        }

        @Test
        void 거리가_널이면_예외() throws Exception {
            // given
            AddStationToLineRequest request = new AddStationToLineRequest("1호선", "잠실역", "잠실나루역", null);

            // when
            MvcResult mvcResult = 노선_역_추가_요청(request);

            //then
            MockHttpServletResponse response = mvcResult.getResponse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        }

        private MvcResult 노선_역_추가_요청(AddStationToLineRequest request) throws Exception {
            return mockMvc.perform(post("/lines/stations")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andReturn();
        }
    }

    @Nested
    class 노선에_역을_제거_요청할_떄 {

        @Test
        void 성공한다() throws Exception {
            // given
            DeleteStationFromLineRequest request = new DeleteStationFromLineRequest("1호선", "잠실역");

            // when
            MvcResult mvcResult = 노선에_역_제거_요청(request);

            // then
            MockHttpServletResponse response = mvcResult.getResponse();
            verify(lineService, times(1)).removeStation(any());
            assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 노선이_널이거나_공백이면_예외(String nullAndEmpty) throws Exception {
            // given
            DeleteStationFromLineRequest request = new DeleteStationFromLineRequest(nullAndEmpty, "잠실역");

            // when
            MvcResult mvcResult = 노선에_역_제거_요청(request);

            // then
            MockHttpServletResponse response = mvcResult.getResponse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        }

        @ParameterizedTest
        @NullAndEmptySource
        void 역이_널이거나_공백이면_예외(String nullAndEmpty) throws Exception {
            // given
            DeleteStationFromLineRequest request = new DeleteStationFromLineRequest("1호선", nullAndEmpty);

            // when
            MvcResult mvcResult = 노선에_역_제거_요청(request);

            // then
            MockHttpServletResponse response = mvcResult.getResponse();
            assertThat(response.getStatus()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
        }


        private MvcResult 노선에_역_제거_요청(DeleteStationFromLineRequest request) throws Exception {
            return mockMvc.perform(delete("/lines/stations")
                            .contentType(APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andDo(print())
                    .andReturn();
        }
    }
}
