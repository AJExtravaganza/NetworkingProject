import java.net.*; 
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeClient 
{ 
	private Socket socket		 = null; 
	private DataInputStream input = null;

	public TimeClient(String timeHostname) throws IOException {
		int nistPort = 13;
		socket = new Socket(timeHostname, nistPort);
		input = new DataInputStream(socket.getInputStream());
	}

	public Date getTimeFromNistServer() throws IOException {
		int daytimeSize = 50;
		int byteChar;
		StringBuilder data = new StringBuilder();

		while ((byteChar = input.read()) != -1) {
			data.append((char) byteChar);
		}

		return daytimeToDateObj(data.toString());
	}

	static Date daytimeToDateObj(String nistDaytimeStr) {
		String[] chunks = nistDaytimeStr.split(" ");
		String dateTime = chunks[1] + " " + chunks[2] + " UTC";
		DateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss z");
		try {
			return format.parse(dateTime);
		} catch (ParseException err) {
			return new Date();
		}
	}
	
	public static void main(String[] args) throws IOException {
		TimeClient client = new TimeClient("time.nist.gov");
		System.out.println(client.getTimeFromNistServer());
	}
} 
