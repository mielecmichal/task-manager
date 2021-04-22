package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.TaskManager;

import static pl.mielecmichal.taskmanager.queueing.OverflowStrategy.*;

public class TaskManagers {

    private TaskManagers() {
    }

    public static TaskManager createFixed(int size){
        return new ConcurrentTaskManager(new QueueingTaskManager(size, FIXED));
    }

    public static TaskManager createFifo(int size){
        return new ConcurrentTaskManager(new QueueingTaskManager(size, FIFO));
    }

    public static TaskManager createPrioritized(int size){
        return new ConcurrentTaskManager(new QueueingTaskManager(size, PRIORITY));
    }
}
