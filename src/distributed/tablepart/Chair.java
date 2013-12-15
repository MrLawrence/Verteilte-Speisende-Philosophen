package distributed.tablepart;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Chair {
	private final static Logger LOG = Logger.getLogger(Chair.class.getName());
	private static AtomicInteger nextId = new AtomicInteger();
	private Integer id;

	private ReentrantLock chair = new ReentrantLock(true);
	private ReentrantLock leftFork;
	private ReentrantLock rightFork;

	public Chair(ReentrantLock leftFork, ReentrantLock rightFork) {
		this.id = nextId.incrementAndGet();
		this.leftFork = leftFork;
		this.rightFork = rightFork;
		LOG.info("Created Chair #" + this.id);
	}

	public void sitDown() {
		chair.lock();
	}

	public void leave() {
		leftFork.unlock();
		rightFork.unlock();
		chair.unlock();
	}

	public void acquireLeftFork() {
		leftFork.lock();
	}

	public void acquireRightFork() {
		rightFork.lock();
	}

	public void acquireForks() {
		if(this.isWeird()) {
			rightFork.lock();
			leftFork.lock();
		} else {
			leftFork.lock();
			rightFork.lock();
		}
	}
	public Boolean empty() {
		return !chair.isLocked();
	}
	
	/**
	 * Is used to break Deadlocks. 
	 * @return
	 */
	public Boolean isWeird() {
		return id == 1;
	}

	@Override
	public String toString() {
		return "Chair #" + this.id;
	}
}
