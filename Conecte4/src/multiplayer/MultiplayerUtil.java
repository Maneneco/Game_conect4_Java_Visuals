package multiplayer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class MultiplayerUtil {	//utilitario de conexao
	private Jogador j1, j2, atual = null; //guarda os jogadores bem como qual é o jogador atual
	private Servidor servidor=null; //guarda um possivel servidor
	private Cliente cliente=null; //ou cliente
	private Boolean exited; // e uma referencia que indica que o jogador local desistiu.
	private String dadosRecebidos = ""; //string que guarda dados recebidos
	private String dadosAEnviar = ""; //string que guarda dados a enviar
	
	
	public MultiplayerUtil(String ip, String username, Boolean exited) throws IOException, UnknownHostException {
		if(ip.isEmpty()) {	
			
			j1 = new Jogador(username, 1, 0); 
			servidor.enviarDados(username); 
			while(dados==null) { 
				dados = servidor.recebeDados();	
			j2 = new Jogador(dados, -1, 0); 
			atual = j1; 
		} else {	
			cliente = new Cliente(ip);  
			
			
			String dados=null; 
			while(dados==null) { 
				dados = cliente.recebeDados();
			}
			
			j1 = new Jogador(dados, 1, 0);	
			
			j2 = new Jogador(username, -1, 0); 
			atual = j1; 
		}
		this.exited = exited;	
		dadosRecebidos = "";

		enviaDadosContinuos();
	}
	
	public Jogador getJogadorAtual() {	
		return atual;
	}
	
	public void enviarDados(String dados) {	
		this.dadosAEnviar = dados;
	}
	
	public String recebeDados() {	
		if(!dadosRecebidos.isEmpty()) {
			String cpy = dadosRecebidos; 
			dadosRecebidos = ""; 
			return cpy; 
		} 
		return null; 
	}
	
	public void enviaDadosContinuos() {	
		new Thread() {
			@Override
			public void run() {	
		
				currentThread().interrupt();	
			}
		}.start();
	}
	
	
	

	public Jogador getEste() {	
		if(servidor!=null) {
			return j1;
		} else {
			return j2;
		}
	}
	

	public void mudaJogador() {
		this.atual = this.atual==j1?j2:j1;
	}

	public boolean taNaVez() {	
		return getJogadorAtual()==getEste();
	}

	public static String getIp() {
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Jogador getAdv() { 
		if(servidor!=null) {
			return j2;
		} else {
			return j1;
		}
	}

	public boolean isServer() {	
		return servidor!=null;
	}

	public void close() {	
		exited = true;
	}
}