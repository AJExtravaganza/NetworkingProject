import java.net.*; 
import java.io.*; 
import java.text.*;
import java.time.*;
import java.util.*;

public class Server 
{ 
	//initialize socket and input stream 
	private Socket		 socket = null; 
	private ServerSocket server = null; 
//	private DataInputStream in	 = null;

	public Server(int port) throws IOException {
		server = new ServerSocket(port);
	}


	private void listen() throws IOException {
		socket = server.accept();
	}

	private void process() throws IOException {
		respondTo(receive());
	}

	private String receive() throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		StringBuilder http = new StringBuilder();
		String line = in.readLine();
		System.out.println("Received:");
		try {
			while (!line.isEmpty()) {
				System.out.println(line);
				http.append(line);
				line = in.readLine();
			}
		} catch (NullPointerException err) {
			System.out.println("End of request reached");
		}
		return http.toString();
	}

	private void respondTo(String inData) throws IOException {
		HttpResponse response = new HttpResponse();
		TimeClient timeGrabber = new TimeClient("time.nist.gov");
		Date time= timeGrabber.getTimeFromNistServer();
		response.setContent(time.toString());
		send(response.get());
	}

	private void send(String outData) throws IOException {
		DataOutputStream out = new DataOutputStream(socket.getOutputStream());
		System.out.println("Sending:\n" + outData);
		out.writeUTF(outData);
		out.close();
	}

	
	public static void main(String[] args) throws IOException {
		int port = 5000;
		Server server = new Server(port);
		while (true) {
			server.listen();
			server.process();
		}
	} 
} 
