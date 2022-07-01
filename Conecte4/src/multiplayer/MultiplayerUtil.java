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
			servidor = new Servidor(); 
			j1 = new Jogador(username, 1, 0); 
			servidor.enviarDados(username); 
			String dados=null; 
			while(dados==null) { 
				dados = servidor.recebeDados();	
			}
			j2 = new Jogador(dados, -1, 0); 
			atual = j1; 
		} else {	/
			cliente = new Cliente(ip);  
			
			
			String dados=null; 
			while(dados==null) { 
				dados = cliente.recebeDados();
			}
			
			j1 = new Jogador(dados, 1, 0);	
			cliente.enviarDados(username); 
			
			j2 = new Jogador(username, -1, 0); 
			atual = j1; 
		}
		this.exited = exited;	
		dadosRecebidos = "";
		recebeDadosContinuos();	
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
				while(!exited) { 
					if(servidor!=null) { 
						if (dadosAEnviar.isEmpty())	servidor.enviarDados("#"); 
						else {
							servidor.enviarDados(dadosAEnviar); 
							dadosAEnviar = ""; 
						}
					} else {	
						if (dadosAEnviar.isEmpty())	cliente.enviarDados("#");	
						else {
							cliente.enviarDados(dadosAEnviar); 
							dadosAEnviar = "";
						}
					}
				}
				if(servidor!=null) {	
					servidor.encerra();
				} else {
					cliente.encerra();
				}
				currentThread().interrupt();	
			}
		}.start();
	}
	
	public void recebeDadosContinuos() {	
		new Thread() {
			@Override
			public void run() {
				while(!exited) { 
					if(servidor!=null) {	
						String s = servidor.recebeDados(); 
						if (s.contains("#")) continue; 
						dadosRecebidos = s;	
					} else { 
						String s = cliente.recebeDados();
						if (s.contains("#")) continue;
						dadosRecebidos = s;
					}
				}
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
	
	public void addMove() {	
	
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
			// TODO Auto-generated catch block
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