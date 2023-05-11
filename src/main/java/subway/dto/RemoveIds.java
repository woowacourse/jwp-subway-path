package subway.dto;

import java.util.ArrayList;
import java.util.List;

public class RemoveIds {
    List<Long> ids;

    public RemoveIds() {
        ids = new ArrayList<>();
    }

    public void insert(Long id) {
        ids.add(id);
    }

    public List<Long> getIds() {
        return ids;
    }
}
