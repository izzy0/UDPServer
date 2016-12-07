import java.io.*;
import java.net.*;
import java.util.*;

import test.SetVals;

public class UDPEchoClient {
	public String hostname = "localhost";
	public final static int PORT = 4200;

	public static void main(String[] args) throws IOException {

		String hostname = "localhost";
		if (args.length > 0)
			hostname = args[0];

		SetVals getVals = new SetVals();
		getVals.setVals();

		try {
			InetAddress ia = InetAddress.getByName(hostname);

			DatagramSocket socket = new DatagramSocket();

			DatagramSocket clientSocket = new DatagramSocket();

			SenderThread sender = new SenderThread(socket, ia, PORT);

			sender.start();

			// Thread receiver = new RecieverThread(socket);
			//
			// receiver.start();

			clientSocket.close();

		} catch (UnknownHostException ex) {
			System.err.println(ex);
		} catch (SocketException ex) {
			System.err.println(ex);
		}

	}
}

// private static short packetSize = 50;
// private static int percent = 30;
// private static int tTime = 3000;
// //FIXME static not changing when values set
//
// public static void setValues(){
// System.out.println("Enter data size: ");
// Scanner input = new Scanner(System.in);
// packetSize = input.nextShort();
//
// System.out.println("Enter Failed %: ");
// percent = input.nextInt();
//
// System.out.println("Enter Time out time in second: ");
// tTime = input.nextInt();
//
// input.close();
//
// }
//
// public static int setFailPercent(){
//
// System.out.println("Enter Failed %: ");
// Scanner input1 = new Scanner(System.in);
// // System.out.println("----- a1");
// if (input1.equals(" ")) {
// percent = 10;
// // System.out.println("----- a2");
// } else {
// // || input.next() == null
// while (!input1.hasNextInt()) {
// if (input1.nextInt() > 0) {
// percent = input1.nextInt();
// System.out.println("-------> EXITED :) ");
// break;
// } else {
// input1.nextInt();
// // System.out.println("----- a3");
// }
// }
// }
//
// input1.close();
// return percent;
//
// }
// //should be private
// public static short setPacketSize(){
// System.out.println("Enter data size: ");
// Scanner input2 = new Scanner(System.in);
// // System.out.println("----- a1");
// if (input2.equals(" ")) {
// packetSize = 10;
// // System.out.println("----- a2");
// } else {
// // || input.next() == null
// while (!input2.hasNextShort()) {
// if (input2.nextShort() > 0) {
// packetSize = input2.nextShort();
// // System.out.println("-------> EXITED :) ");
// break;
// } else {
// input2.nextShort();
// // System.out.println("----- a3");
// }
// }
// }
//
// input2.close();
// return packetSize;
//
// }
//
// public static int setTimeOut(){
// System.out.println("Enter Time out time in second: ");
// Scanner input = new Scanner(System.in);
// // System.out.println("----- a1");
// if (input.equals(" ")) {
// tTime = 3;
// // System.out.println("----- a2");
// } else {
// // || input.next() == null
// while (!input.hasNextInt()) {
// if (input.nextInt() > 0) {
// tTime = input.nextInt();
// // System.out.println("-------> EXITED :) ");
// break;
// } else {
// input.nextInt();
// // System.out.println("----- a3");
// }
// }
// }
//
// input.close();
// return tTime * 1000 ;
//
// }
// public static short getPacketSize() {
// return packetSize;
// }
//
// public static int getFailPercent() {
// return percent;
// }
//
// public static int getTimeOut() {
// return tTime;
// }