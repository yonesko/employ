package gleb.data;

import gleb.model.Task;

import java.util.Map;
import java.util.UUID;

public class DigestTaskRepo {
    private Map<UUID, Map<Integer, Task>> usersTasks;
}
