import java.util.concurrent.Semaphore;

public class DiningPhilosophers {
	
	/**
	 * The implementation for questions 3.1 and 3.2 was based on 
	 * https://www.baeldung.com/java-dining-philoshophers.
	 */
	public static void main(String[] args) {
		if (args.length != 2) {
			System.out.println("Error: invalid arguments");
			System.out.println("Usage: java DiningPhilosophers [numPhilosophers] [3.1 | 3.2 | 3.3]");
			return;
		}
		int numberOfPhilosophers = Integer.parseInt(args[0]);
		String version = args[1];

		if (version.equals("3.1")) {
			Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
			Object[] chopsticks = new Object[numberOfPhilosophers];

			for (int i = 0; i < chopsticks.length; i++) {
				chopsticks[i] = new Object();
			}

			for (int i = 0; i < numberOfPhilosophers; i++) {
				Object leftChopstick = chopsticks[i];
				Object rightChopstick = chopsticks[(i + 1) % numberOfPhilosophers];

				philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
				
				Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
				t.start();
			}
		} else if (version.equals("3.2")) {
			Philosopher[] philosophers = new Philosopher[numberOfPhilosophers];
			Object[] chopsticks = new Object[numberOfPhilosophers];

			for (int i = 0; i < chopsticks.length; i++) {
				chopsticks[i] = new Object();
			}

			for (int i = 0; i < numberOfPhilosophers; i++) {
				Object leftChopstick = chopsticks[i];
				Object rightChopstick = chopsticks[(i + 1) % numberOfPhilosophers];

				if (i == 0) {
					// first philosopher picks up right chopstick first to break 
					// circular wait condition and avoid deadlock
					philosophers[i] = new Philosopher(rightChopstick, leftChopstick);
				} else {
					philosophers[i] = new Philosopher(leftChopstick, rightChopstick);
				}
				
				Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
				t.start();
			}
		} else if (version.equals("3.3")) {
			PhilosopherSemaphore[] philosophers = new PhilosopherSemaphore[numberOfPhilosophers];
			Semaphore[] chopsticks = new Semaphore[numberOfPhilosophers];

			for (int i = 0; i < chopsticks.length; i++) {
				// set second argument to true to enable fairness queueing
				chopsticks[i] = new Semaphore(1, true);
			}

			for (int i = 0; i < numberOfPhilosophers; i++) {
				Semaphore leftChopstick = chopsticks[i];
				Semaphore rightChopstick = chopsticks[(i + 1) % numberOfPhilosophers];

				if (i == 0) {
					// first philosopher picks up right chopstick first to break 
					// circular wait condition and avoid deadlock
					philosophers[i] = new PhilosopherSemaphore(rightChopstick, leftChopstick);
				} else {
					philosophers[i] = new PhilosopherSemaphore(leftChopstick, rightChopstick);
				}
				
				Thread t = new Thread(philosophers[i], "Philosopher " + (i + 1));
				t.start();
			}
		} else {
			System.out.println("Error: invalid program version specified");
			System.out.println("Usage: java DiningPhilosophers [numPhilosophers] [3.1 | 3.2 | 3.3]");
		}
	}

	public static class Philosopher implements Runnable {

		private Object leftChopstick;
		private Object rightChopstick;

		public Philosopher(Object leftChopstick, Object rightChopstick) {
			this.leftChopstick = leftChopstick;
			this.rightChopstick = rightChopstick;
		}

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println(Thread.currentThread().getName() + ": Thinking");
					synchronized (leftChopstick) {
						System.out.println(Thread.currentThread().getName() + ": Picked up left chopstick");
						Thread.sleep(((int) (Math.random() * 200)));
						synchronized (rightChopstick) {
							System.out.println(Thread.currentThread().getName() + ": Picked up right chopstick, eating..."); 
							Thread.sleep(((int) (Math.random() * 200)));
							System.out.println(Thread.currentThread().getName() + ": Put down right chopstick");
						}
						
						System.out.println(Thread.currentThread().getName() + ": Put down left chopstick, thinking...");
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

	/**
	 * Modified Philosopher class for question 3.3.
	 */
	public static class PhilosopherSemaphore implements Runnable {

		private Semaphore leftChopstick;
		private Semaphore rightChopstick;

		public PhilosopherSemaphore(Semaphore leftChopstick, Semaphore rightChopstick) {
			this.leftChopstick = leftChopstick;
			this.rightChopstick = rightChopstick;
		}

		@Override
		public void run() {
			try {
				while (true) {
					System.out.println(Thread.currentThread().getName() + ": Thinking");

					leftChopstick.acquire();
					System.out.println(Thread.currentThread().getName() + ": Picked up left chopstick");

					rightChopstick.acquire();
					System.out.println(Thread.currentThread().getName() + ": Picked up right chopstick, eating..."); 
					Thread.sleep(((int) (Math.random() * 200)));

					rightChopstick.release();
					System.out.println(Thread.currentThread().getName() + ": Put down right chopstick");

					leftChopstick.release();
					System.out.println(Thread.currentThread().getName() + ": Put down left chopstick, thinking...");
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				return;
			}
		}
	}

}
