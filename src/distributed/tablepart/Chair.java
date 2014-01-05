package distributed.tablepart;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class Chair implements Serializable {
	private static final long serialVersionUID = 1L;
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
		if (leftFork.isLocked()) {
			leftFork.unlock();
		}
		if (rightFork != null && rightFork.isLocked()) {
			rightFork.unlock();
		}
		chair.unlock();
	}

	public void acquireForks() {
		if (this.isWeird()) {
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
	 * 
	 * @return
	 */
	public Boolean isWeird() {
		return id == 1;
	}

	public void changeRightFork(ReentrantLock fork) {
		this.rightFork = fork;
	}

	@Override
	public String toString() {
		return "Chair #" + this.id;
	}
}
