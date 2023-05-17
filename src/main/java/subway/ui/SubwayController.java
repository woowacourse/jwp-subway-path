package subway.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import subway.dto.PathResponse;

@RestController
@RequestMapping("/subway")
public class SubwayController {

    //TODO: 최단경로 정보와 운임 body에 담아 전달하기
    @GetMapping("/path")
    public ResponseEntity<PathResponse> findPath() {
        return ResponseEntity.ok().build();
    }
}
