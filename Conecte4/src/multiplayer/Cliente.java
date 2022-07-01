package multiplayer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Cliente {	//o socket do cliente
	private Socket client; //guarda um socket simples

	Scanner entrada; //um buffer de entrada 
	PrintStream saida; //e um de saida
	
	public Cliente(String ip) throws UnknownHostException, IOException {
		client = new Socket(ip,4444);	//o socket se conecta ao ip passado via porta 4444 (conecte 4)
		
		entrada = new Scanner(client.getInputStream());	//o buffer de entrada é criado usando a stream de entrada do socket recem criado
		saida = new PrintStream(client.getOutputStream()); //o buffer de saida também do mesmo jeito.
	}
	
	public void enviarDados(String dados) {	//a funcao que envia dados via socket é bem simples: recebe os dados, e envia.
		saida.println(dados);
	}
	
	public String recebeDados() { 	//para receber os dados
		if(entrada.hasNextLine()) { //é verificado se há uma nova entrada de dados.
			return entrada.nextLine(); //se houver, retorna os dados.
		}
		return "#";	//se nao, retorna # como simbolo de que nao há nada.
	}

	public void encerra() { //funcao que encerra os buffers e depois o socket.
		try {
			client.close();
			entrada.close();
			saida.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
}
