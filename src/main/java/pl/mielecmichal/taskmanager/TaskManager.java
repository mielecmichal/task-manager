package pl.mielecmichal.taskmanager;

import java.util.Collection;

public interface TaskManager {

    void addProcess(Process process);

    Collection<Process> listProcesses(Sorting sorting);

    void killProcess(long pid);

    void killGroup(Process.Priority priority);

    void killAll();

    enum Sorting {
        BY_PID,
        BY_PRIORITY,
        BY_TIMESTAMP,
    }
}
