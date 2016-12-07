import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.file.*;
import java.util.*;

import test.*;

public class SenderThread extends Thread {

	private InetAddress server;
	private DatagramSocket socket;
	private int port;

	Socket connection = null;

	short packetSize = SetVals.getPacketSize();
	double packetsToSend;
	byte[] recievePacket = new byte[10];

	public SenderThread(DatagramSocket socket, InetAddress address, int port) {
		this.server = address;
		this.port = port;
		this.socket = socket;
		this.socket.connect(server, port);
	}

	public short bytesToShort(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
	}

	public int bytesToInt(byte[] bytes) {
		return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
	}
	public void goBackIn(ArrayList<DatagramPacket> dgp, int windowSize){
	    System.out.println(dgp.size());
	    //      int i = 0;
        int seq = 0;
        //while loop for something
        while(seq < dgp.size()){
            //for loop send all 5 packets
            //if the difference between the size of dgp and seq, is less than
            //windowSize then it sets windowSize to that value
            if(((dgp.size()- seq) - 1)< windowSize){
                windowSize = ((dgp.size() - seq) - 1);
                System.out.println(windowSize);
            }
            System.out.print("Sending packets");
            for (int j = seq; j < seq + (windowSize); j++) {
                try {
                    socket.send(dgp.get(j));
                    System.out.print(" " + j);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println();
                try {
                    socket.setSoTimeout(SetVals.getTimeOut());
                } catch (SocketException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                while(true){
                    try {
                    DatagramPacket rp = new DatagramPacket(recievePacket,
                            recievePacket.length);

                    socket.receive(rp);

                    byte[]  Seq = Arrays.copyOfRange(rp.getData(), 6, 10);
                    seq = bytesToInt(Seq);

                    System.out.println("Recieved ack for packet "+ seq);

                    if(dgp.size()>(seq+windowSize)){
                        System.out.println(dgp.size() + " " + (seq+windowSize));

                        socket.send(dgp.get(seq+windowSize));
                        System.out.println("Sending Packet " + (seq + windowSize));
                    }
                    }
                    catch (SocketTimeoutException x) {
                        break;
                        //System.out.println("Timed Out, Resending Packet " + seq);
                    }


            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //socketTImeOut
            //receive seq # and store
            //if received next packet (ACK for packet you where waiting for) send Window size + 1
            //break loop after timeOut to send more packets

                }
           }
        System.out.println("All Packets sent.");
    }
	public void sendingPacket(DatagramPacket sp, int seq) {
		boolean notTimedOut = true;
		while (notTimedOut) {

			try {
				System.out
						.println("-> Sending Packet " + seq);
				// if dropping
				if (droppedPacket()) {
					System.out.println("Packet - " + seq + " Dropped :O");
				} else {
					socket.send(sp);
				}
				//System.out.println("Time OUT -- " + SetVals.getTimeOut());

				socket.setSoTimeout(SetVals.getTimeOut());

				DatagramPacket rp = new DatagramPacket(recievePacket,
						recievePacket.length);

				socket.receive(rp);

				byte[] Ack = Arrays.copyOfRange(rp.getData(), 2, 6);

				System.out.println("- Recieved Acknowledgement for Packet " + seq);
				notTimedOut = false;

			} catch (SocketTimeoutException x) {

				System.out.println("Timed Out, Resending Packet " + seq);
			} catch (IOException x) {
				x.printStackTrace();
			}

		}

	}

	public Boolean droppedPacket() {
		int rand = (int) (Math.random() * 100);
		//System.out.println("% dropped " + SetVals.getFailPercent());

		if (rand < SetVals.getFailPercent()) {
			return true;
		} else {
			return false;
		}
	}

	byte[] sendData;

	public void run() {

		System.out.println("Connecting to Server ..");

		byte[] per = new byte[4];
        per[0] = (byte) SetVals.getFailPercent();
        per[1] = (byte) ((SetVals.getFailPercent() >>8) & 0xff);
        per[2] = (byte) ((SetVals.getFailPercent() >>16) & 0xff);
        per[3] = (byte) ((SetVals.getFailPercent() >>24) & 0xff);
		DatagramPacket out = new DatagramPacket(per, per.length);
		try {
			socket.send(out);
			socket.receive(out);
			System.out.println("... Connected to server");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Path path = Paths.get("test.txt");

		try {
			this.sendData = Files.readAllBytes(path);

		} catch (IOException x) {
			x.printStackTrace();
		}

		int ackno = 0;
		ArrayList<byte[]> dataArray = new ArrayList<byte[]>();
		ArrayList<DatagramPacket> datagramPackets = new ArrayList<DatagramPacket>();


		while (sendData.length > 0) {

			if (sendData.length <= (packetSize - 10)) {

				byte[] data = new byte[sendData.length];

				data = Arrays.copyOf(sendData, sendData.length);

				dataArray.add(data);
				break;
			}

			byte[] data = new byte[packetSize - 10];

			data = Arrays.copyOfRange(sendData, 0, (packetSize - 10));
			dataArray.add(data);

			sendData = Arrays.copyOfRange(sendData, (packetSize - 10),
					sendData.length);
		}


		for (byte[] data : dataArray) {
			int seq = dataArray.indexOf(data);

			if (seq == (dataArray.size() - 1)) {

				Packet packet = new Packet((short) (data.length + 10), 1, seq,
						data);

				DatagramPacket sendPacket = new DatagramPacket(
						packet.SendPacket(), (data.length + 10), server, port);

				//sendingPacket(sendPacket, seq);
				datagramPackets.add(sendPacket);
				break;
			}

			Packet packet = new Packet(packetSize, ackno, seq, data);

			DatagramPacket sendPacket = new DatagramPacket(packet.SendPacket(),
					packetSize, server, port);

			//sendingPacket(sendPacket, seq);
			datagramPackets.add(sendPacket);
		}
		goBackIn(datagramPackets,5);
		return;

	}
}