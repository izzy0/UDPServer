import java.io.*;

public class ReadWrite {

	private BufferedReader buffer;

	public String read(String fileName) {
		StringBuilder string = new StringBuilder();
		try {
			buffer = new BufferedReader(new FileReader(fileName));
			String str;
			while ((str = buffer.readLine()) != null) {
				if (string.length() > 0) {
					string.append("\n");
				}
				string.append(str);
			}
		} catch (Exception e) {
			System.out.println("ERROR - FILE NOT READ");
		}
		String contents = string.toString();
		System.out.println(contents);
		return contents;
	}

	public String read() {
		StringBuilder string = new StringBuilder();
		try {
			buffer = new BufferedReader(new FileReader("test.txt"));
			String str;
			while ((str = buffer.readLine()) != null) {
				if (string.length() > 0) {
					string.append("\n");
				}
				string.append(str);
			}
		} catch (Exception e) {
			System.out.println("ERROR - FILE NOT READ");
		}
		String contents = string.toString();
		System.out.println(contents);
		return contents;
	}

	public void write(BufferedReader data, String fileName) throws IOException {
		BufferedWriter output = null;
		File file = new File(fileName);
		StringBuilder sb = new StringBuilder();
		String line;
		if (!file.exists()) {
			file.createNewFile();
		}
		try {

			output = new BufferedWriter(new FileWriter(file, true));
			String inputLine = null;
			do {
				// output.write();
				//System.out.println("read line (writting) --- "+data.readLine());
				while ((line = data.readLine()) != null) {
					sb.append(line);
				}
				//System.out.println("In WRite -- " + inputLine);
				output.write(sb.toString() + "\n");
				// output.newLine();
			} while ((inputLine != null));
			//System.out.println("SUCCESSFUL WRITE");
		} catch (Exception e) {
			System.out.println("ERROR - FILE NOT WRITTEN " + e);
		} finally {
			output.close();
			
		}

	}

	public void write(BufferedReader data) throws IOException {
		String fileName = "test.txt";
		// System.out.println("-------------in Write 2");
		write(data, fileName);

	}

	public void write(byte[] data, String fileName) {
		InputStream is = null;
		BufferedReader bfReader = null;
		// System.out.println("--------------in Write 3");
		// try {
		is = new ByteArrayInputStream(data);
		bfReader = new BufferedReader(new InputStreamReader(is));
		// String temp = null;
		// while((temp = bfReader.readLine()) != null){
		// System.out.println(temp);
		// }
		try {
			write(bfReader, fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// } catch (IOException e) {
		// e.printStackTrace();
		// } finally {
		// try{
		// if(is != null) is.close();
		// } catch (Exception ex){
		//
		// }
		// }
		// System.out.println("in Write to read buffer "+bfReader.read());

	}

}
