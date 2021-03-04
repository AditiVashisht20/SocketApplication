import java.net.*;
import java.io.*;
class MyClient {
    static DataOutputStream dout = null;
    static DataInputStream din = null;
    static final int filesize = 1022386;
    public static void main(String args[]) throws Exception {
        Socket s = new Socket(InetAddress.getByName("localhost"), 3333);
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = "", str2 = "";
        while (!str.equals("stop")) {
            str = br.readLine();
            if (str.equalsIgnoreCase("stop")) {
                System.out.println("Connection with server ended");
                break;
            }
            dout.writeUTF(str);
            dout.flush();
            str2 = din.readUTF();
            if (str2.equalsIgnoreCase("stop")) {
                System.out.println("Connection ended with server");
                break;
            }
            String arr[] = str2.split("\\|");
            if (arr[0].trim().equalsIgnoreCase("1")) {
                System.out.println("Server says: " + arr[1]);
            } else if (arr[0].trim().equalsIgnoreCase("2")) {
                System.out.println("File received. Downloading File..." + arr[1]);
                String filePath = "E:\\"+arr[1];
                receiveFile(filePath);
                System.out.println("Downloading Complete....");
            }
        }

        dout.close();
        s.close();
    }


    private static void receiveFile(String fileName) throws Exception{
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);
        
        long size = din.readLong();
        byte[] buffer = new byte[4*1024];
        while (size > 0 && (bytes = din.read(buffer, 0, (int)Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer,0,bytes);
            size -= bytes;
        }
        fileOutputStream.close();
    }
}