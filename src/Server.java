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
		try {
			respondTo(receive());
		} catch (URISyntaxException err) {
			send(new HttpResponse().badRequest());
		}
	}

	private SimpleHttpRequest receive() throws IOException, URISyntaxException {
		return new SimpleHttpRequest(socket.getInputStream());
	}

	private void respondTo(SimpleHttpRequest request) throws IOException {
		Boolean goodRequest = true;
		HttpResponse response = new HttpResponse();
		TimeClient timeGrabber = new TimeClient("time.nist.gov");
		Date time= timeGrabber.getTimeFromNistServer();

		String requestedTimezone;
		try {
			requestedTimezone = request.uri.getQuery().split("zone=")[1].split("&")[0]; //don't judge me
 		} catch (NullPointerException err) {
			requestedTimezone = "all";
		}

		String[] timeOutputLines = new String[3];
		switch (requestedTimezone.toLowerCase()) {
			case "all":
				timeOutputLines[0] = String.format("GMT Date/Time: %s", time.toString());
				timeOutputLines[1] = String.format("GMT Date/Time: %s", time.toString());
				timeOutputLines[2] = String.format("GMT Date/Time: %s", time.toString());
				break;
			case "est":
				timeOutputLines[0] = String.format("GMT Date/Time: %s", time.toString());
				break;
			case "pst":
				timeOutputLines[0] = String.format("GMT Date/Time: %s", time.toString());
				break;
			default:
				goodRequest = false;
		}

		if (goodRequest) {
			StringBuilder responseBody = new StringBuilder();
			for (String line : timeOutputLines) {
				if (line != null) {
					responseBody.append(line).append("<br>");
				}
			}

			String responseContent = String.format("<html><head><title>Current Time</title></head><body><h3>%s</h3></body></html>", responseBody);
			response.setContent(responseContent);
			send(response.get());
		}
		else {
			send(new HttpResponse().badRequest());
		}
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
