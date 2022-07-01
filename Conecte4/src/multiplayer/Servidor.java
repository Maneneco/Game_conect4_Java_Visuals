package multiplayer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor {	
	private ServerSocket server; 
	private Socket client; 
	
	Scanner entrada; 
	PrintStream saida; 
	
	
	public Servidor() throws IOException {
		server = new ServerSocket(4444); 
		client = server.accept(); 
		entrada = new Scanner(client.getInputStream()); 
		saida = new PrintStream(client.getOutputStream());
	}
	
	
}
