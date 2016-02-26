import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class HelpServer {
	private InetAddress addr;
	private int portNum;

	public HelpServer(String address, int port) throws IOException{
		portNum = port;
		addr = InetAddress.getByName(address);
	}

	public String RequestHandler(String req) throws IOException {
	Socket socket = new Socket(addr, portNum);
	try {
			BufferedReader in = new BufferedReader(
			new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(
			new OutputStreamWriter(socket.getOutputStream())),true);
			Scanner input = new Scanner(System.in);
			out.println(req);
			String response = in.readLine();
			return response;
		} finally {
		socket.close();
		}
		
	}
}
