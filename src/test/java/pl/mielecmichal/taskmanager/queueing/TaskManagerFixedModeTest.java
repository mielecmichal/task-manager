package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.TaskManager;

class TaskManagerFixedModeTest implements
        AddingFixedTest,
        AddingIncorrectTest,
        KillingTest,
        ListingTest,
        ConcurrencyTest,
        MaintainingCapacityTest {

    @Override
    public TaskManager provideTaskManager(int size) {
        return TaskManagers.createFixed(size);
    }
}
