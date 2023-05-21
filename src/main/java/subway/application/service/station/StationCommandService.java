package subway.application.service.station;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import subway.application.port.in.station.CreateStationUseCase;
import subway.application.port.in.station.DeleteStationUseCase;
import subway.application.port.in.station.UpdateStationUseCase;
import subway.application.port.in.station.dto.command.CreateStationCommand;
import subway.application.port.in.station.dto.command.UpdateStationCommand;
import subway.application.port.out.station.LoadStationPort;
import subway.application.port.out.station.PersistStationPort;
import subway.common.exception.NoSuchStationException;
import subway.common.exception.SubwayIllegalArgumentException;
import subway.domain.Station;

@Service
@Transactional
public class StationCommandService implements CreateStationUseCase, UpdateStationUseCase, DeleteStationUseCase {

    private final LoadStationPort loadStationPort;
    private final PersistStationPort persistStationPort;

    public StationCommandService(final LoadStationPort loadStationPort,
            final PersistStationPort persistStationPort) {
        this.loadStationPort = loadStationPort;
        this.persistStationPort = persistStationPort;
    }

    @Override
    public long createStation(final CreateStationCommand command) {
        loadStationPort.findByName(command.getName())
                .ifPresent(it -> {
                    throw new SubwayIllegalArgumentException("기존 역과 중복된 이름입니다.");
                });
        return persistStationPort.create(new Station(command.getName()));
    }


    @Override
    public void updateStation(final UpdateStationCommand command) {
        validateStationId(command.getStationId());

        persistStationPort.update(new Station(command.getStationId(), command.getName()));
    }

    // TODO: Section의 해당 station 전부 지워주기
    @Override
    public void deleteStation(final long stationId) {
        validateStationId(stationId);

        persistStationPort.deleteById(stationId);
    }

    private void validateStationId(final long stationId) {
        loadStationPort.findById(stationId)
                .orElseThrow(NoSuchStationException::new);
    }
}
