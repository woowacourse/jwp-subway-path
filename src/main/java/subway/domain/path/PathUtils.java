package subway.domain.path;

import java.util.List;

final class PathUtils {

    private PathUtils() {
    }

    public static boolean isContinuous(final Path up, final Path down) {
        if (up.equals(down)) {
            return false;
        }

        return up.getDown().equals(down.getUp());
    }

    public static boolean isOverlapped(final Path path1, final Path path2) {
        final boolean upStationEquals = path1.isUpStation(path2.getUp());
        final boolean downStationEquals = path1.isDownStation(path2.getDown());

        return upStationEquals || downStationEquals;
    }

    public static Path merge(final Path path1, final Path path2) {
        if (isContinuous(path1, path2)) {
            return new Path(path1.getUp(), path2.getDown(), path1.getDistance() + path2.getDistance());
        }
        if (isContinuous(path2, path1)) {
            return new Path(path2.getUp(), path1.getDown(), path2.getDistance() + path1.getDistance());
        }

        throw new IllegalStateException("두 경로를 합칠 수 없습니다.");
    }

    public static List<Path> divide(final Path original, final Path divisor) {
        if (original.getDistance() <= divisor.getDistance()) {
            throw new IllegalArgumentException("기존의 거리보다 길 수 없습니다.");
        }
        if (!isOverlapped(original, divisor)) {
            throw new IllegalArgumentException("두 경로가 겹치지 않습니다.");
        }

        if (original.getUp().equals(divisor.getUp())) {
            return divideByUpPath(original, divisor);
        }
        return divideByDownPath(original, divisor);
    }

    private static List<Path> divideByUpPath(final Path original, final Path up) {
        final int downDistance = original.getDistance() - up.getDistance();
        final Path down = new Path(up.getDown(), original.getDown(), downDistance);

        return List.of(up, down);
    }

    private static List<Path> divideByDownPath(final Path original, final Path down) {
        final int upDistance = original.getDistance() - down.getDistance();
        final Path up = new Path(original.getUp(), down.getUp(), upDistance);

        return List.of(up, down);
    }
}
