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

    static void printMenu(){
        System.out.println("Press 1 to enter a message");
        System.out.println("Press 2 to send a file");
        System.out.println("Type 'STOP' to end the connection");
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
            if(str.trim().equalsIgnoreCase("stop")){
                System.out.println("Connection ended with client");
                break;
            }
            System.out.println("client says: "+str);
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
            }else if(str2.trim().equalsIgnoreCase("STOP")){
                dout.writeUTF("STOP");
            }
            dout.flush(); 
        }  
        din.close();  
        s.close();  
        ss.close(); 
    }   
}  