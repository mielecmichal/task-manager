package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.TaskManager;

class TaskManagerPrioritizedModeTest implements
        AddingPrioritizedTest,
        AddingIncorrectTest,
        KillingTest,
        ListingTest,
        ConcurrencyTest,
        MaintainingCapacityTest {

    @Override
    public TaskManager provideTaskManager(int size) {
        return TaskManagers.createPrioritized(size);
    }
}
