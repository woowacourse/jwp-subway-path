package subway.application;

import org.springframework.stereotype.Service;
import subway.application.dto.AddStationToLineCommand;
import subway.application.dto.LineCreateCommand;

@Service
public class LineService {


    public Long create(final LineCreateCommand command) {
        return null;
    }

    public void addStation(final AddStationToLineCommand command) {

    }
}
