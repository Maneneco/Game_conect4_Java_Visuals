package multiplayer;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;

public class MultiplayerUtil {	//utilitario de conexao
	private Jogador j1, j2, atual = null; //guarda os jogadores e jogador atual
	private Servidor servidor=null; //guarda um possivel servidor
	private Cliente cliente=null; //ou cliente
	private Boolean exited; // e uma referencia que indica que o jogador local desistiu.
	private String dadosRecebidos = ""; //string que guarda dados recebidos
	private String dadosAEnviar = ""; //string que guarda dados a enviar
	
	
	public MultiplayerUtil(String ip, String username, Boolean exited) throws IOException, UnknownHostException {
		if(ip.isEmpty()) {	//se o ip recebido for vazio, significa que o jogador atual é um servidor.
			servidor = new Servidor(); //cria o servidor
			
			j1 = new Jogador(username, 1, 0); //cria um jogador
			servidor.enviarDados(username); //envia nomes do jogador ao cliente conectado
			String dados=null; //dados iniciam vazios
			while(dados==null) { //aguarda recebimento do nome do jogador.
				dados = servidor.recebeDados();	//ao receber guarda em dados
			}
			j2 = new Jogador(dados, -1, 0); //cria o jogador remoto
			atual = j1; //o servidor inicia o jogo
		} else {	//se o ip for valido
			cliente = new Cliente(ip);  //cria um cliente ligado ao ip indicado
			
			
			String dados=null; 
			while(dados==null) { //recebe o nome do jogador remoto
				dados = cliente.recebeDados();
			}
			
			j1 = new Jogador(dados, 1, 0);	//cria o jogador remoto
			cliente.enviarDados(username); //envia o nome do jogador atual
			
			j2 = new Jogador(username, -1, 0); // cria o jogador atual
			atual = j1; //remoto inicia
		}
		this.exited = exited;	//guarda as informações passadas
		dadosRecebidos = "";
		recebeDadosContinuos();	//inicia o envio e recebimento de dados pelo socket
		enviaDadosContinuos();
	}
	
	public Jogador getJogadorAtual() {	//retorna o jogador atual
		return atual;
	}
	
	public void enviarDados(String dados) {	//guarda os dados a serem enviados
		this.dadosAEnviar = dados;
	}
	
	public String recebeDados() {	//tenta recuperar dados recebidos
		if(!dadosRecebidos.isEmpty()) { //se houver dados recebidos
			String cpy = dadosRecebidos; //é criado uma copia destes
			dadosRecebidos = ""; //os dados recebidos sao apagados
			return cpy; //é retorna a copia
		} 
		return null; //se nao houver dados, retorna nulo
	}
	
	public void enviaDadosContinuos() {	//inicia o envio continuo de dados
		new Thread() {
			@Override
			public void run() {	//a thread inicia
				while(!exited) { //se o jogador atual nao tiver saido
					if(servidor!=null) { //e servidor nao for nulo (ou seja, nao é cliente)
						if (dadosAEnviar.isEmpty())	servidor.enviarDados("#"); //se nao houver dados a enviar, envia o sinal de que nao há nada a ser enviado.
						else {
							servidor.enviarDados(dadosAEnviar); //se nao, envia os dados
							dadosAEnviar = ""; //e apaga
						}
					} else {	//se for um cliente
						if (dadosAEnviar.isEmpty())	cliente.enviarDados("#");	//faz a mesma coisa
						else {
							cliente.enviarDados(dadosAEnviar); //usando o socket de cliente.
							dadosAEnviar = "";
						}
					}
				}
				if(servidor!=null) {	//ao sair do jogo, encerra o socket que esta a ser utilizado
					servidor.encerra();
				} else {
					cliente.encerra();
				}
				currentThread().interrupt();	//e interrompe a thread.
			}
		}.start();
	}
	
	public void recebeDadosContinuos() {	//o recebimento de dados continuos é parecido
		new Thread() {
			@Override
			public void run() {
				while(!exited) { //se nao tiver saido
					if(servidor!=null) {	//e for servidor
						String s = servidor.recebeDados(); //tenta receber dados
						if (s.contains("#")) continue; //se receber apenas o sinal de 'sem dados', salta para a prxima interacao do loop
						dadosRecebidos = s;	//se os dados forem validos, sao armazenados nos dados recebidos.
					} else { //mesma coisa mas com socket de cliente
						String s = cliente.recebeDados();
						if (s.contains("#")) continue;
						dadosRecebidos = s;
					}
				}
				currentThread().interrupt();	//interrompe a thread apos o loop.
			}
		}.start();
	}
	
	

	public Jogador getEste() {	//retorna o jogador local
		if(servidor!=null) {
			return j1;
		} else {
			return j2;
		}
	}
	
	public void addMove() {	//adiciona um movimento à contagem do jogador atual
		atual.addMove();
	}

	public void mudaJogador() {	//passa a vez
		this.atual = this.atual==j1?j2:j1;
	}

	public boolean taNaVez() {	//retorna verdadeiro se for vez do jogador local
		return getJogadorAtual()==getEste();
	}

	public static String getIp() {//retorna o ip do servidor
		try {
			return Inet4Address.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Jogador getAdv() { //retorna o jogador remoto
		if(servidor!=null) {
			return j2;
		} else {
			return j1;
		}
	}

	public boolean isServer() {	//retorna verdadeiro se for servidor
		return servidor!=null;
	}

	public void close() {	//fecha o utilitario
		exited = true;
	}
}
