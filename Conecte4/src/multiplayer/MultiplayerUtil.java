package multiplayer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class MultiplayerUtil {	
	private Jogador j1, j2, atual = null; 
	private Servidor servidor=null; 
	private Cliente cliente=null;
	private Boolean exited; 
	private String dadosRecebidos = ""; 
	private String dadosAEnviar = ""; 
	
	public MultiplayerUtil(String ip, String username, Boolean exited) throws IOException, UnknownHostException {
		if(ip.isEmpty()) {	
			
		} else {	
			
			String dados=null; 
			while(dados==null) { 

			}
			

		}
		this.exited = exited;	
		dadosRecebidos = "";
	}
	
	public Jogador getJogadorAtual() {	
		return atual;
	}
	
	public void enviarDados(String dados) {	
		this.dadosAEnviar = dados;
	}
}