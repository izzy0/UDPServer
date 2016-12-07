public class Packet {
//	short cksum; // 16-bit 2-byte
	short len; // 16-bit 2-byte
	int ackno; // 32-bit 4-byte
	int seqno; // 32-bit 4-byte Data packet Only
	byte[] data = null; // 0-500 bytes. Data packet only. Variable

	public Packet(short len, int ackno, int seqno, byte[] data) {
        // this.cksum = cksum;
        this.len = len;
        this.ackno = ackno;
        this.seqno = seqno;
        this.data = new byte[data.length];
        this.data = data;
    }

	// public Packet( int ackno, int seqno, byte[] data){
	// ;
	//
	// this.ackno = ackno;
	// this.seqno = seqno;
	// this.data = data;
	// }

	public Packet(int ackno, int seqno, byte[] data) {
		this.ackno = ackno;
		this.seqno = seqno;
		this.data = data;
	}
	public Packet(Byte[] dgp){

	}

    public byte[] SendPacket() {

        byte[] sendBack = new byte[len];
        sendBack[0] = (byte) len;
        sendBack[1] = (byte) ((len >>8) & 0xff);
        sendBack[2] = (byte) ackno;
        sendBack[3] = (byte) ((ackno >>8) & 0xff);
        sendBack[4] = (byte) ((ackno >>16) & 0xff);
        sendBack[5] = (byte) ((ackno >>24) & 0xff);
        sendBack[6] = (byte) seqno;
        sendBack[7] = (byte) ((seqno >>8) & 0xff);
        sendBack[8] = (byte) ((seqno >>16) & 0xff);
        sendBack[9] = (byte) ((seqno >>24) & 0xff);
        System.arraycopy(data, 0, sendBack, 10, (data.length));

        return sendBack;
    }

	public int getSeqno() {
		return seqno;
	}

	public int getAck() {
		return ackno;
	}

	public byte[] getData() {
		return data;
	}
}
