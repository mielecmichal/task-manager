package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.TaskManager;

class TaskManagerFifoModeTest implements
        AddingFifoTest,
        AddingIncorrectTest,
        KillingTest,
        ListingTest,
        ConcurrencyTest,
        MaintainingCapacityTest {

    @Override
    public TaskManager provideTaskManager(int size) {
        return TaskManagers.createFifo(size);
    }
}
