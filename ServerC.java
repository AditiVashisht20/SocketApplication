import java.net.*;  
import java.io.*;  
public class ServerC {
    static void printMenu(){
        System.out.println("Press 1 to enter a message");
        System.out.println("Press 2 to send a file");
        System.out.println("Type 'STOP' to end the connection");
    }
    public static void main(String args[])throws Exception{  
        ServerSocket ss=new ServerSocket(3333);  
        Socket s=ss.accept();  
        System.out.println("Socket Server Connected");
        DataInputStream din=new DataInputStream(s.getInputStream());  
        DataOutputStream dout=new DataOutputStream(s.getOutputStream());  
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
            }else if(str2.trim().equals("2")){
                System.out.print("Enter the path of the file you want to send to the client: ");
                str2 =br.readLine();
                str2 = str2.replace("\\" , "\\\\");
                String arr[] = str2.split("\\\\");
                String filename = arr[arr.length-1];
                System.out.println(filename);
                File transferFile = new File(str2);
                byte[] bytearray = new byte[(int) transferFile.length()];
                FileInputStream fin = new FileInputStream(transferFile);
                BufferedInputStream bin = new BufferedInputStream(fin);
                bin.read(bytearray, 0, bytearray.length);
                str2 = "2|"+filename+"|"+new String(bytearray);
            }else if(str2.trim().equalsIgnoreCase("STOP")){
                dout.writeUTF("STOP");
            }
            dout.writeUTF(str2);  
            dout.flush(); 
        }  
        din.close();  
        s.close();  
        ss.close(); 
    }   
}  