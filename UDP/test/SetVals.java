package test;

import java.util.Scanner;

public class SetVals {
	private static short packetSize;
	private static int percent;
	private static int tTime;

	public void setVals() {
		System.out.println("Enter data size: ");
		Scanner input = new Scanner(System.in);
		packetSize = input.nextShort();

		System.out.println("Enter Failed %: ");
		percent = input.nextInt();

		System.out.println("Enter Time out time in nano-second: ");
		tTime = input.nextInt();

		input.close();
	}

	public static short getPacketSize() {
		return packetSize;
	}

	public static int getFailPercent() {
		return percent;
	}

	public static int getTimeOut() {
		return tTime;
	}
}
