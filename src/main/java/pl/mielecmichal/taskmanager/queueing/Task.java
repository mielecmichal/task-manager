package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.Process;

record Task(Process process, long monotonicTimestamp) {

    public Task(Process process) {
        this(process, System.nanoTime());
    }

    public long priorityValue() {
        return process.priority().value();
    }

    public void kill() {
        process.kill();
    }
}
