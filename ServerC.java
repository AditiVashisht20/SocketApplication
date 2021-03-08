import java.net.*;  
import java.io.*;  
public class ServerC {

    private static DataInputStream din = null;
    private static DataOutputStream dout = null;

    private static void sendFile(String path) throws Exception{
        int bytes = 0;
        File file = new File(path);
        FileInputStream fileInputStream = new FileInputStream(file);
        
        dout.writeLong(file.length());  
     
        byte[] buffer = new byte[4*1024];
     
        while ((bytes=fileInputStream.read(buffer))!=-1){
            dout.write(buffer,0,bytes);
            dout.flush();
        }
        fileInputStream.close();
    }
    
    private static void receiveFile(String fileName) throws Exception {
        int bytes = 0;
        FileOutputStream fileOutputStream = new FileOutputStream(fileName);

        long size = din.readLong();
        byte[] buffer = new byte[4 * 1024];
        while (size > 0 && (bytes = din.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
            fileOutputStream.write(buffer, 0, bytes);
            size -= bytes;
        }
        fileOutputStream.close();
    }

    private static void printMenu(){
        System.out.println("\n\nPress 1 to enter a message");
        System.out.println("Press 2 to send a file");
        System.out.println("Type 'STOP' to end the connection\n\n");
    }
    public static void main(String args[])throws Exception{  
        ServerSocket ss=new ServerSocket(3333);  
        Socket s=ss.accept();  
        System.out.println("Socket Server Connected");
        din=new DataInputStream(s.getInputStream());  
        dout=new DataOutputStream(s.getOutputStream());  
        BufferedReader br=new BufferedReader(new InputStreamReader(System.in));  
          
        String str="",str2="";  
        while(true){
            str=din.readUTF();
            String rec[] = str.split("\\|");
            if (rec[0].trim().equalsIgnoreCase("1")) {
                System.out.println("Client says: " + rec[1]);
            } else if (rec[0].trim().equalsIgnoreCase("2")) {
                System.out.println("File received. Downloading File..." + rec[1]);
                String filePath = "E:\\Server\\" + rec[1];
                receiveFile(filePath);
                System.out.println("Downloading Complete....");
            }
            if(str.trim().equalsIgnoreCase("stop")){
                System.out.println("Connection ended with client");
                break;
            }
            // System.out.println("client says: "+str);
            printMenu();
            str2=br.readLine();
            if(str2.trim().equals("1")){
                System.out.print("Enter the message you want to send to the client: ");
                str2 = "1|"+br.readLine();
                dout.writeUTF(str2);  
            }else if(str2.trim().equals("2")){
                System.out.print("Enter the path of the file you want to send to the client: ");
                str2 =br.readLine();
                str2 = str2.replace("\\" , "\\\\");
                String arr[] = str2.split("\\\\");
                String filename = arr[arr.length-1];
                dout.writeUTF("2|"+filename);
                sendFile(str2);
                System.out.println("File Sent");
            }else if(str2.trim().equalsIgnoreCase("STOP")){
                dout.writeUTF("STOP");
                System.out.println("Connection with client ended...");
                break;
            }
            dout.flush(); 
        }  
        din.close();  
        s.close();  
        ss.close(); 
    }   
}  