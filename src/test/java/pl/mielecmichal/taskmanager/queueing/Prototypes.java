package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.Process;
import pl.mielecmichal.taskmanager.SimpleProcess;

class Prototypes {
    public static final Process MEDIUM_PROCESS_1 = new SimpleProcess(1, Process.Priority.MEDIUM);
    public static final Process MEDIUM_PROCESS_2 = new SimpleProcess(2, Process.Priority.MEDIUM);
    public static final Process MEDIUM_PROCESS_3 = new SimpleProcess(3, Process.Priority.MEDIUM);
    public static final Process HIGH_PROCESS_4 = new SimpleProcess(4, Process.Priority.HIGH);
    public static final Process LOW_PROCESS_5 = new SimpleProcess(5, Process.Priority.LOW);
    public static final Process LOW_PROCESS_6 = new SimpleProcess(6, Process.Priority.LOW);
}
