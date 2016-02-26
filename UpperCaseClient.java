import java.net.*;
import java.io.*;
import java.util.*;
public class UpperCaseClient {
	public static void main(String[] args) throws IOException {
		InetAddress addr = InetAddress.getByName(null);
		// Alternatively:
		// InetAddress addr = InetAddress.getByName("127.0.0.1");
		// InetAddress addr = InetAddress.getByName("localhost");
		System.out.println("test");
		Socket socket = new Socket(addr, 5050);
		try {
			BufferedReader in = new BufferedReader(
			new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(socket.getOutputStream())),true);
			Scanner input = new Scanner(System.in);
			while (true) {
				String line = input.nextLine();
				if (line.equals("quit"))
				break;
				out.println(line);
				String response = in.readLine();
				System.out.println("Echo: " + response);
			}
		} finally {
			System.out.println("closing...");
			socket.close();
		}
	}
}
