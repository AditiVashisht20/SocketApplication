import java.net.*;
import java.io.*;
class MyClient {
    static DataOutputStream dout = null;
    static DataInputStream din = null;
    public static void main(String args[]) throws Exception {
        Socket s = new Socket(InetAddress.getByName("localhost"), 3333);
        din = new DataInputStream(s.getInputStream());
        dout = new DataOutputStream(s.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = "", str2 = "";
        while (!str.equals("stop")) {
            printMenu();
            str = br.readLine();
            if(str.trim().equals("1")){
                System.out.print("Enter the message you want to send to the client: ");
                str = "1|"+br.readLine();
                dout.writeUTF(str);  
            }else if(str.trim().equals("2")){
                System.out.print("Enter the path of the file you want to send to the client: ");
                str =br.readLine();
                str = str.replace("\\" , "\\\\");
                String arr[] = str.split("\\\\");
                String filename = arr[arr.length-1];
                dout.writeUTF("2|"+filename);
                System.out.println("2|" + filename);
                sendFile(str);
            }
            else if (str.equalsIgnoreCase("stop")) {
                dout.writeUTF("stop");
                System.out.println("Connection with server ended");
                break;
            }
            // dout.writeUTF(str);
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
                System.out.println("File received. Downloading File...");
                String filePath = "E:\\Client\\"+arr[1];
                receiveFile(filePath);
                System.out.println("Downloading Complete..... . File saved at "+filePath);
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


    private static void sendFile(String path) throws Exception {
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);

        dout.writeLong(file.length());

        byte[] buffer = new byte[4 * 1024];

        while ((bytes = fileInputStream.read(buffer)) != -1) {
            dout.write(buffer, 0, bytes);
            dout.flush();
        }
        fileInputStream.close();
    }

    static void printMenu() {
        System.out.println("\n\nPress 1 to enter a message");
        System.out.println("Press 2 to send a file");
        System.out.println("Type 'STOP' to end the connection\n\n");
    }
}