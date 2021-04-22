package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.TaskManager;

interface TestedManagerProvider {

    TaskManager provideTaskManager(int size);
}
