package distributed;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.logging.Logger;

public class Server {
	private final static Logger LOG = Logger.getLogger(Server.class.getName());

	public static void main(String args[]) throws RemoteException {
		Scanner scanner = new Scanner(System.in);

		Integer chairAmount = 12;

		LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

		TableInterface table = new Table(chairAmount);
		TableInterface stub = (TableInterface) UnicastRemoteObject
				.exportObject(table, 0);

		Registry registry = LocateRegistry.getRegistry();
		registry.rebind("table", stub);

		while (true) {
			System.out.println("Type 'help' for help");
			System.out.print("> ");
			String input = scanner.nextLine();
			String param = input.toLowerCase();

			switch (param) {
			case "lay":
				table.layTable();
				break;
			case "addphil":
				table.createPhilosophers(1, false);
				break;
			case "addhungry":
				table.createPhilosophers(1, true);
				break;
			case "killphil":
				table.killPhilosophers(1);
				break;
			case "addchair":
				table.createChair(1);
				break;
			case "killchair":
				table.killChair(1);
				break;

			case "help":
				System.out.println("lay, addphil, addhungry, killphil, addchair, killchair, help");
				break;
			case "exit": {
				scanner.close();
				System.exit(1);
			}
			}
		}
	}
}
