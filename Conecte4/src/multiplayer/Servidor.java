package multiplayer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Servidor {	//servidor de conexao
	private ServerSocket server; //socket do tipo servidor
	private Socket client; //referencia ao cliente conectado
	
	Scanner entrada; //buffers de entrada e saida  de dados.
	PrintStream saida; 
	
	
	public Servidor() throws IOException {
		server = new ServerSocket(4444); //o server aguarda conexoes na porta 4444
		client = server.accept(); //ao receber uma conexao, guarda o cliente
		entrada = new Scanner(client.getInputStream()); //a entrada e saida do cliente sao estabelecidos como buffers
		saida = new PrintStream(client.getOutputStream());
	}
	
	
}
