package subway.domain;

public interface CostPolicy {
    
    long calculate(Path path);
}
