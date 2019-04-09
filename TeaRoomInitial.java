import java.util.LinkedList;
import tearoom.*;

class Machine implements VendingMachine {

	private int supply;
	private int profit;

	// Boilerplate getters

	public int supply() {
		return this.supply;
	}

	public int profit() {
		return this.profit;
	}

	// Constructor

	public Machine(int supply) {
		this.supply = supply;
		this.profit = 0;
	}

	// Main action

	public synchronized Tea vend(Coin c, Drinker drinker) {

		Tea possibleTea = null; // might end up with no tea if not enough supply
		drinker.makeMeWait();

		if (this.supply > 0) {

			// Economics
			this.profit++;
			this.supply--;

			// Pouring the tea
			try {

				Thread.sleep(2000);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

			// Tea is ready
			System.out.println("Machine is dispensing for " + drinker.name);
			possibleTea = new Tea();

		}

		return possibleTea;
	}

}

// *****************************************************************************

class Drinker extends Thread implements Customer {

	private Machine machine;
	private int walletSize;
	public String name;
	private Status status;

	public Status status() {
		return this.status;
	}

	public int funds() {
		return this.walletSize;
	}

	// Allow someone else to make me wait
	public void makeMeWait() {
		this.status = Status.WAITING;
	}

	public Drinker(Machine machine, int walletSize, String name) {
		this.machine = machine;
		this.walletSize = walletSize;
		this.name = name;
	}

	public void run() {
		while (this.walletSize > 0) {

			try {
				// Have a little read of the paper...
				this.status = Status.RESTING;
				Thread.sleep(1000);

				// Let's get tea
				this.status = Status.QUEUEING;
				this.walletSize--;
				// Vend...
				Tea tea = this.machine.vend(new Coin(), this);

				if (tea != null) {

					this.status = Status.DRINKING;
					System.out.println(this.name + " is drinking some tea.");
					tea.drink();

				} else {
					System.out.println(this.name + " was robbed");
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();

			}
		}

		System.out.println(this.name + ": all out of cash.");
	}
}

// *****************************************************************************

public class TeaRoomInitial implements TeaRoom {

	public static int n = 5;
	private LinkedList<Drinker> customers;
	private Machine machine;

	public LinkedList<Drinker> customers() {
		return this.customers;
	}

	public Machine machine() {
		return this.machine;
	}

	public TeaRoomInitial(int supply, int walletSize) {

		this.machine   = new Machine(supply);
		this.customers = new LinkedList<Drinker>();

		for (int i = 0; i < n; i++) {
			Drinker d = new Drinker(machine, walletSize, "Customer " + i);
			d.start();
			customers.add(d);
		}


	}

}
