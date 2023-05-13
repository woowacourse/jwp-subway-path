package subway.application.dto;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import subway.domain.Line;
import subway.domain.Lines;
import subway.domain.Section;
import subway.domain.payment.PaymentLines;

public class ShortestRouteResponse {

    private final List<SectionInfo> sectionInfos;
    private final List<String> transferStations;
    private final int transferCount;
    private final int totalDistance;
    private final int totalFee;

    public ShortestRouteResponse(final List<SectionInfo> sectionInfos,
                                 final List<String> transferStations,
                                 final int totalDistance,
                                 final int totalFee) {
        this.sectionInfos = new ArrayList<>(sectionInfos);
        this.transferStations = new ArrayList<>(transferStations);
        this.totalDistance = totalDistance;
        this.transferCount = transferStations.size();
        this.totalFee = totalFee;
    }

    public static ShortestRouteResponse empty() {
        return new ShortestRouteResponse(
                emptyList(),
                emptyList(),
                0,
                0);
    }

    public static ShortestRouteResponse from(final PaymentLines paymentLines) {
        final Lines lines = paymentLines.lines();
        if (lines.isEmpty()) {
            return empty();
        }
        final List<SectionInfo> sectionInfoList = lines.lines().stream()
                .flatMap(it -> SectionInfo.from(it).stream())
                .collect(toList());
        return new ShortestRouteResponse(
                sectionInfoList,
                toTransferStations(lines),
                lines.totalDistance(),
                paymentLines.calculateFee());
    }

    private static List<String> toTransferStations(final Lines lines) {
        return lines.lines().stream()
                .map(it -> it.downTerminal().name())
                .limit(lines.size() - 1)
                .collect(toList());
    }

    public List<SectionInfo> getSectionInfos() {
        return sectionInfos;
    }

    public List<String> getTransferStations() {
        return transferStations;
    }

    public int getTransferCount() {
        return transferCount;
    }

    public int getTotalDistance() {
        return totalDistance;
    }

    public int getTotalFee() {
        return totalFee;
    }

    public static class SectionInfo {
        private final String line;
        private final String fromStation;
        private final String toStation;
        private final int distance;

        public SectionInfo(final String line, final String fromStation, final String toStation, final int distance) {
            this.line = line;
            this.fromStation = fromStation;
            this.toStation = toStation;
            this.distance = distance;
        }

        public static SectionInfo of(final String line, final Section section) {
            return new SectionInfo(line, section.up().name(), section.down().name(), section.distance());
        }

        public static List<SectionInfo> from(final Line line) {
            return line.sections()
                    .stream()
                    .map(it -> SectionInfo.of(line.name(), it))
                    .collect(toList());
        }

        public String getLine() {
            return line;
        }

        public String getFromStation() {
            return fromStation;
        }

        public String getToStation() {
            return toStation;
        }

        public int getDistance() {
            return distance;
        }
    }
}
