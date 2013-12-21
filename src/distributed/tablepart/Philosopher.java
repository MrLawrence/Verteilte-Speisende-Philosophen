package distributed.tablepart;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Philosopher implements Runnable {
	private final static Logger LOG = Logger.getLogger(Philosopher.class
			.getName());
	private static AtomicInteger nextId = new AtomicInteger();
	private Integer id;

	private TablePart table;
	private Chair chair;

	private Integer totalMeals = 0;
	private Integer mealsBeforeSleep = 3;
	private Integer thinkTime = 10;
	private Integer sleepTime = 100;
	private Integer mealTime = 2;
	private Integer penaltyTime = 0;

	private Boolean isHungry;

	public Philosopher(TablePart table, Boolean isHungry) {
		this.id = nextId.incrementAndGet();
		LOG.info("Philosoph #" + id + " created");
		this.isHungry = isHungry;
		if (isHungry) {
			sleepTime /= 2;
		}
		this.table = table;
	}

	@Override
	public void run() {
		while (true) {
			chair = table.getChair();
			chair.sitDown();
			LOG.fine(this.toString() + " sits on " + chair.toString());
			
			penalty();
			eat();
			
			chair.leave();
			
			think();
			sleep();
		}
	}

	private void eat() {
		chair.acquireForks();
		try {
			LOG.info(this.toString() + " eats on " + chair.toString());
			Thread.sleep(mealTime);
		} catch (InterruptedException e) {
			LOG.info(this.toString() + " was interrupted");
		}

	}

	private void think() {
		totalMeals += 1;
		LOG.fine(this.toString() + " starts eating (Meal #" + totalMeals + ")");

		try {
			Thread.sleep(thinkTime);
		} catch (InterruptedException e) {
			LOG.info(this.toString() + " was interrupted");
		}

	}

	private void sleep() {
		if (totalMeals % mealsBeforeSleep == 0) {
			try {
				Thread.sleep(sleepTime);
				LOG.info(this.toString() + " sleeps");
			} catch (InterruptedException e) {
				LOG.info(this.toString() + " was interrupted");
			}
		}
	}

	public Integer getMealsAmount() {
		return totalMeals;
	}

	public void banFor(Integer penaltyTime) {
		this.penaltyTime = penaltyTime;
	}

	private void penalty() {
		if (penaltyTime != 0) {
			try {
				LOG.info(this.toString() + " banned for " + this.penaltyTime
						+ "ms");
				Thread.sleep(penaltyTime);
			} catch (InterruptedException e) {
				LOG.info(this.toString() + " was interrupted");
			}
			penaltyTime = 0;
		}
	}

	public void printStats() {
		System.out.println(this.toString() + ": " + totalMeals + " meals");
	}

	@Override
	public String toString() {
		String string = "Philosopher #" + id;
		if (isHungry) {
			string = string + " (hungry)";
		}
		return string;
	}
}
