package subway.path.application.dto;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import subway.path.domain.Path;
import subway.payment.domain.discount.DiscountResult;

public class ShortestRouteResponse {

    private final List<SectionInfo> sectionInfos;
    private final List<FeeInfo> feeInfos;
    private final List<String> transferStations;
    private final int transferCount;
    private final int totalDistance;

    public ShortestRouteResponse(final List<SectionInfo> sectionInfos,
                                 List<FeeInfo> feeInfos,
                                 final List<String> transferStations,
                                 final int totalDistance) {
        this.sectionInfos = new ArrayList<>(sectionInfos);
        this.feeInfos = new ArrayList<>(feeInfos);
        this.transferStations = new ArrayList<>(transferStations);
        this.totalDistance = totalDistance;
        this.transferCount = transferStations.size();
    }

    public static ShortestRouteResponse from(final Path path, final DiscountResult discountResult) {
        return new ShortestRouteResponse(
                toSectionInfos(path),
                toFeeInfos(discountResult),
                toTransferStations(path),
                path.totalDistance());
    }

    private static List<SectionInfo> toSectionInfos(final Path path) {
        return path.lines().stream()
                .flatMap(it -> SectionInfo.from(it).stream())
                .collect(toList());
    }

    private static List<FeeInfo> toFeeInfos(final DiscountResult discountResult) {
        return discountResult.feeByDiscountInfo().entrySet()
                .stream()
                .map(it -> new FeeInfo(it.getKey(), it.getValue()))
                .collect(toList());
    }

    private static List<String> toTransferStations(final Path path) {
        return path.lines().stream()
                .map(it -> it.downTerminal().name())
                .limit(path.size() - 1)
                .collect(toList());
    }

    public List<SectionInfo> getSectionInfos() {
        return sectionInfos;
    }

    public List<FeeInfo> getFeeInfos() {
        return feeInfos;
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
}
