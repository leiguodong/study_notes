package JUC;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by lei on 2021/2/19.
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        lock.unlock();
        List list = null;
    }
}
