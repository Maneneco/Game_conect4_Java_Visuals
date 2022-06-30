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
	
	public MultiplayerUtil() {// Construtor
		
	}
}