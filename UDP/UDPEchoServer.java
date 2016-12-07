import java.io.*;
import java.net.*;
import java.nio.*;
import java.util.*;
import java.util.logging.*;

public class UDPEchoServer implements Runnable {

    public final static int DEFAULT_PORT = 4200;
    private int bufferSize;
    private final int port;
    private final Logger logger = Logger.getLogger(UDPEchoServer.class.getCanonicalName());
    private volatile boolean isShutDown = false;
    public int currentSeqNo = 0;

    List<Integer> seqNos = new ArrayList<>();
	private int per;

    public UDPEchoServer(int port, int bufferSize) {
        this.port = port;
        this.bufferSize = bufferSize;

    }

    public UDPEchoServer(int port) {
        this(port, 65507);
    }

    public UDPEchoServer() {
        this(DEFAULT_PORT);

    }
//added if statement to check if pack coming in is in order, if not it will not
//recieve the rest of the packets
    @Override
    public void run() {
        byte[] buffer = new byte[bufferSize];

        try (DatagramSocket socket = new DatagramSocket(port)) {

        	//socket.setSoTimeout(10000);
        	DatagramPacket percentIn = new DatagramPacket(buffer, buffer.length);
        	socket.receive(percentIn);
        	System.out.println("I feel the CONNECTION..");
        	socket.send(percentIn);
        	byte[] Per = Arrays.copyOfRange(percentIn.getData(), 0, 4);
        	per = bytesToInt(Per);

        	while ( true ) {
                if ( isShutDown )
                    return;

                DatagramPacket incoming = new DatagramPacket(buffer, buffer.length);
                try {
                    socket.receive(incoming);

                    //SetVals gogo = new SetVals();
//                    System.out.println("New percent"+per);

                    byte[] Len = Arrays.copyOfRange(incoming.getData(), 0, 2);
                    short len = bytesToShort(Len);

                    byte[] Ack = Arrays.copyOfRange(incoming.getData(), 2, 6);
                    int ack = bytesToInt(Ack);

                    byte[] Seq = Arrays.copyOfRange(incoming.getData(), 6, 10);
                    int seq = bytesToInt(Seq);

                    if ( seqNos.contains(seq) ) {
                        byte[] ackP = new byte[10];

                        ackP = Arrays.copyOfRange(buffer, 0, 10);

                        DatagramPacket ackPacket = new DatagramPacket(
                            ackP ,ackP.length, incoming.getAddress(),incoming.getPort());
                       System.out.println("Duplicate Packet "+ seq +" Recieved, Resending ACK");
                        this.respond(socket, ackPacket);
                    }
                    else if (currentSeqNo == seq){

                        seqNos.add(seq);

                        byte[] buffer2 = new byte[len];
                        buffer2 = Arrays.copyOfRange(buffer, 10, len);

                      //  System.out.println("- Recieved ACK for Packet " + seq);
                        System.out.println("- Recieved Packet " + seq );
                        String str = new String(buffer2);
//                        System.out.println("RECEIVED: " + str);

                        ReadWrite writeTo = new ReadWrite();
                        writeTo.write(str.getBytes(), "RCTestFile.txt");
               //         Thread.sleep(2000);

                        byte[] ackP = new byte[10];
                        ackP = Arrays.copyOfRange(buffer, 0, 10);

                        DatagramPacket ackPacket = new DatagramPacket(
                            ackP ,ackP.length, incoming.getAddress(),incoming.getPort());
                        currentSeqNo++;
                        if (droppedPacket()) {
                            System.out.println("ACK Dropped For Packet " + seq);
                        }else{
                            this.respond(socket, ackPacket);
                            System.out.println("- Sending ACK for Packet " + seq);
                            }
                        }else {
                 //           socket.receive(incoming);
                              System.out.println("Recieving packets out of order, dropping the rest of the frames");
						}


                } catch ( IOException ex ) {

                	logger.log(Level.WARNING, ex.getMessage(), ex);
                }
            }
        } catch ( SocketException ex ) {
            logger.log(Level.SEVERE, "could not bind to port" + port, ex);
        } catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

    }

    public Boolean droppedPacket() {
		int rand = (int) (Math.random() * 100);
//		System.out.println("% dropped " + per);

		if (rand < per) {
			return true;
		} else {
			return false;
		}
	}

    public void shutDown() {
        this.isShutDown = true;
    }

    public void respond(DatagramSocket socket, DatagramPacket packet) throws IOException {
        DatagramPacket outgoing = new DatagramPacket(packet.getData(),
        		packet.getLength(), packet.getAddress(), packet.getPort());

        socket.send(outgoing);

    }

    public short bytesToShort(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
    }

    public int bytesToInt(byte[] bytes) {
        return ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public static void main(String[] args) throws IOException {

        System.out.println("server started\n");
        UDPEchoServer server = new UDPEchoServer();
        Thread t = new Thread(server);
        t.start();

    }

}