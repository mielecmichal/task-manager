package pl.mielecmichal.taskmanager.queueing;

import pl.mielecmichal.taskmanager.Process;
import pl.mielecmichal.taskmanager.Process.Priority;
import pl.mielecmichal.taskmanager.TaskManager;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ConcurrentTaskManager implements TaskManager {

    private final TaskManager delegatedManager;
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public ConcurrentTaskManager(TaskManager delegatedManager) {
        this.delegatedManager = delegatedManager;
    }

    @Override
    public void addProcess(Process process) {
        runWithLock(readWriteLock.writeLock(), () -> delegatedManager.addProcess(process));
    }

    @Override
    public Collection<Process> listProcesses(Sorting sorting) {
        return doWithLock(readWriteLock.readLock(), () -> delegatedManager.listProcesses(sorting));
    }

    @Override
    public void killProcess(long pid) {
        runWithLock(readWriteLock.writeLock(), () -> delegatedManager.killProcess(pid));
    }

    @Override
    public void killGroup(Priority priority) {
        runWithLock(readWriteLock.writeLock(), () -> delegatedManager.killGroup(priority));
    }

    @Override
    public void killAll() {
        runWithLock(readWriteLock.writeLock(), delegatedManager::killAll);
    }

    private void runWithLock(Lock lock, Runnable runnable) {
        doWithLock(lock, () -> {
            runnable.run();
            return null;
        });
    }

    private <T> T doWithLock(Lock lock, Callable<T> callable) {
        try {
            lock.lock();
            return callable.call();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        } finally {
            lock.unlock();
        }
        return null;
    }
}
