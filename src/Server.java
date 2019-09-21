import java.net.*;
import java.io.*; 
import java.text.*;
import java.time.*;
import java.util.*;

class BadRequestException extends Exception {
	BadRequestException(String errorMessage) {
		super(errorMessage);
	}
}

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
		try {
			if (!request.uri.getPath().equals("/time")) {
				throw new BadRequestException("Unsupported path");
			}
			HttpResponse response = new HttpResponse();
			TimeClient timeGrabber = new TimeClient("time.nist.gov");
			Date date = timeGrabber.getTimeFromNistServer();

			String requestedTimezone;
			try {
				requestedTimezone = request.uri.getQuery().split("zone=")[1].split("&")[0]; //don't judge me
			} catch (NullPointerException err) {
				requestedTimezone = "all";
			} catch (ArrayIndexOutOfBoundsException err) {
				throw new BadRequestException("Unsupported uri parameters");
			}

			String[] timeOutputLines = new String[3];
			switch (requestedTimezone.toLowerCase()) {
				case "all":
					timeOutputLines[0] = getOutputLine(date, "UTC");
					timeOutputLines[1] = getOutputLine(date, "EST");
					timeOutputLines[2] = getOutputLine(date, "PST");
					break;
				case "est":
					timeOutputLines[0] = getOutputLine(date, "EST");
					break;
				case "pst":
					timeOutputLines[0] = getOutputLine(date, "PST");
					break;
				default:
					throw new BadRequestException("Unsupported timezone (supports all, est, pst)");
			}

			StringBuilder responseBody = new StringBuilder();
			for (String line : timeOutputLines) {
				if (line != null) {
					responseBody.append(line).append("<br>");
				}
			}

			String responseContent = String.format("<html><head><title>Current Time</title></head><body><h3>%s</h3></body></html>", responseBody);
			response.setContent(responseContent);
			send(response.get());

		} catch (BadRequestException err) {
			send(new HttpResponse().badRequest());
		}
	}

	private String getOutputLine(Date date, String timezone) {
		return String.format("%s Date/Time: %s", timezone, getDateStr(date, timezone));
	}

	private String getDateStr(Date date, String timezone) {
		SimpleDateFormat outputFormat = new SimpleDateFormat("yy-MM-dd h:mm a");
		outputFormat.setTimeZone(TimeZone.getTimeZone(timezone));
		return outputFormat.format(date);
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
