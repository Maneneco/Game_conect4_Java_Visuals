package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import multiplayer.Jogador;
import multiplayer.MultiplayerUtil;

public class MainSceneController {

	@FXML
	private Canvas canvasArea;	//referencias aos objetos da cena jfx
	@FXML
	private AnchorPane loginScreen;
	@FXML
	private Label labelIp;
	@FXML
	private Label labelUsername;
	@FXML
	private TextField ipField;
	@FXML
	private TextField username;
	@FXML
	private AnchorPane error;
	@FXML
	private AnchorPane canvasAnchor;
	@FXML
	private AnchorPane endGame;
	@FXML
	private TextArea erroTXT;
	@FXML
	private AnchorPane ipAdd;
	@FXML
	private Label progressing;
	@FXML
	private Label moveseste;
	@FXML
	private Label movesoutro;
	@FXML
	private Label nomeeste;
	@FXML
	private Label nomeoutro;
	@FXML
	private Label moves;
	@FXML
	private Label time;
	@FXML
	private ImageView leftbulb;
	@FXML
	private ImageView rightbulb;
	@FXML
	private ImageView estebutton;
	@FXML
	private ImageView outrobutton;
	@FXML
	private ImageView won;
	@FXML
	private ImageView lost;
	@FXML
	private ImageView tie;

	
	private Game game;	//referencia ao jogo
	private MultiplayerUtil multiplayer; // referencia ao utilitario multiplayer
	private Boolean exited = false; // boolean que verifica quando o jogador decidiu sair
	private Boolean finished = false; //boolean que verifica se o jogo terminou
	
	@FXML
	public void initialize() { //inicia os dados do acrãprincipal
		Font f;	//fonte a ser usada
		try {
			f = Font.loadFont(new FileInputStream("res/font/aller-display.ttf"), 14); //carrega a fonte no tamanho 14
			labelIp.setFont(f); // fonte para objetos do ecrâ
			moveseste.setFont(f);
			movesoutro.setFont(f);
			labelUsername.setFont(f);
			time.setFont(f);
			moves.setFont(f);
			f = Font.loadFont(new FileInputStream("res/font/aller-display.ttf"), 18); //mesma coisa, mas tamanho 18
			progressing.setFont(f);
			nomeeste.setFont(f);
			nomeoutro.setFont(f);
			progressing.setText("Waiting for Match!\nIP:" + MultiplayerUtil.getIp()); //ip na mensagem de espera.
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reset() {	//limpa os dados do ecrâ e do jogo.
		exited = true;
		canvasAnchor.setVisible(false);
		loginScreen.setVisible(true);
		endGame.setVisible(false);
		won.setVisible(false);
		lost.setVisible(false);
		game = null;
		multiplayer.close();
		multiplayer = null;
		exited = false;
		finished = false;
	}
	

	// Event Listener on Button.onAction
	@FXML
	public void buttonRestart(ActionEvent event) {	//o botao de restart serve para limpar os dados
		reset();
	}
	
	// Event Listener on Canvas[#canvasArea].onMouseClicked
	@FXML
	public void canvasMouseClicked(MouseEvent event) {	//quando há um clique
		if (game==null) return; //caso jogo não tenha iniciado, nada é feito
		game.setClick(event.getX(),event.getY()); //se nao, envia o clique ao jogo
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void buttonPlay(ActionEvent event) { // inicia o jogo
		if(username.getText().isEmpty()) return; // se nao houver um nome de utilizador, nao inicia o jogo.
		new Thread() { //cria uma thread para iniciar o jogo.
			@Override
			public void run() {	//funcao da thread
				Image righton=null, 	//imagens a serem usadas para indicar a vez dos jogadores;
						rightoff=null,
						lefton=null,
						leftoff=null;
				try {
					loginScreen.setVisible(false);	//esconde o ecrâ de login
					ipAdd.setVisible(true); //mostra o painel do endereco de ip.
					multiplayer = new MultiplayerUtil(ipField.getText(),username.getText(),exited); //inicia o utilitario com o nome e o ip inseridos
																									
					Platform.runLater(new Runnable() { //executa esta função após o fim deste fluxo da thread
						
						@Override
						public void run() {	//ao ser executado, guarda os nomes dos jogadores
							nomeeste.setText(multiplayer.getEste().getName());
							nomeoutro.setText(multiplayer.getAdv().getName());
						}
					});
					
					if (multiplayer.isServer()) {	//se for um servidor, o jogador local será o vermelho, e o remoto, o amarelo
						estebutton.setImage(new Image(new FileInputStream("res/rbutton.png")));
						outrobutton.setImage(new Image(new FileInputStream("res/ybutton.png")));
					} else {						//se for cliente, o contrario
						estebutton.setImage(new Image(new FileInputStream("res/ybutton.png")));
						outrobutton.setImage(new Image(new FileInputStream("res/rbutton.png")));
					}
										//carrega as imagens das lampadas indicadoras de ordem de jogo 
					righton = new Image(new FileInputStream("res/righton.png"));
					rightoff = new Image(new FileInputStream("res/rightoff.png"));
					lefton = new Image(new FileInputStream("res/lefton.png"));
					leftoff = new Image(new FileInputStream("res/leftoff.png"));
					ipAdd.setVisible(false);	//esconde o painel de login
					canvasAnchor.setVisible(true); //mostra o canvas no ecrâ
				} catch (Exception e) {
					error.setVisible(true); //em caso de erro, é mostrado o painel de erro
					loginScreen.setVisible(false);	//o ecrâ de login sai
					erroTXT.setText(e.getMessage()); //e o erro é mostrado
				}
				
				game = new Game(canvasArea,multiplayer);	//o jogo é inicializado
				long start = System.currentTimeMillis(); //inicia-se a contagem do tempo de jogo
				while(game != null) {	//enquanto o jogo existir
					if (exited) { //se o jogador local sair,
						multiplayer.enviarDados("exit"); //é enviado uma mensagem de saida, como sinal de desistencia.
						break; 	//sai do loop do jogo
					}
					if(!finished) { //se o jogo nao tiver finalizado

						Jogador v = game.update(); //o jogo é atualizado, e é guardado o retorno em v
						
						Platform.runLater(new Runnable() { //executado apos a call atual da thread para nao dar erro
							
							@Override
							public void run() {
								moveseste.setText(multiplayer.getEste().getMoves()+"");	//os indicadores de movimentos no acra são mostrados
								movesoutro.setText(multiplayer.getAdv().getMoves()+"");
							}
						});
						
						if(multiplayer.taNaVez()) {	//se for a vez do jogador local
							leftbulb.setImage(lefton); //acende a luz do local
							rightbulb.setImage(rightoff); //apaga a do remoto
						} else {					//se nao, faz o contrario
							leftbulb.setImage(leftoff);
							rightbulb.setImage(righton);
						}
						
						
						if(v==multiplayer.getEste()) {	//se houve um retorno, e este for igual ao jogador local, entao este venceu.
							long stop = System.currentTimeMillis(); //o tempo de fim do jogo é calculado
							long secs = (stop-start)/1000; //transformado em segundos
							long mins =	secs/60; //e em minutos
							secs = secs%60; //e segundos
							String times = mins + ":" + secs; //os valores sao formatados
							
							int totalMoves = multiplayer.getAdv().getMoves() + multiplayer.getEste().getMoves(); //os movimentos dos jogadores somados
							
							endGame.setVisible(true); //o acrâ de fim de jogo aparece.
							won.setVisible(true); //com a mensagem de vitoria
							Platform.runLater(new Runnable() { //apos a call da thread
								
								@Override
								public void run() {
									time.setText(times); //o tempo total é adicionado
									moves.setText("" + totalMoves); //assim como os movimentos totais
								}
							});


							multiplayer.enviarDados(v.getName());	//o nome do jogador atual é enviado como sinal de vitoria
							
							try {
								Thread.sleep(100); //antes de finalizar o jogo é aguardado 1/10 de segundo
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							finished = true; //e o jogo finaliza
						} else if (v!=null) {	//se v não for nulo, quer dizer que o jogador local perdeu
							long stop = System.currentTimeMillis(); 	//e feito todo o calculo de fim de jogo
							long secs = (stop-start)/1000;
							long mins =	secs/60;
							secs = secs%60;
							String times = mins + ":" + secs;

							int totalMoves = multiplayer.getAdv().getMoves() + multiplayer.getEste().getMoves();

							endGame.setVisible(true);
							lost.setVisible(true);	//a diferença é que o ecrâ mostrado indica a derrota do jogador local.
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									time.setText(times);
									moves.setText("" + totalMoves);
								}
							});
							
							finished = true;
						}

						if(game.verificaEmpate()) {	//se houve empate
							long stop = System.currentTimeMillis();
							long secs = (stop-start)/1000;
							long mins =	secs/60;
							secs = secs%60;
							String times = mins + ":" + secs;
							
							int totalMoves = multiplayer.getAdv().getMoves() + multiplayer.getEste().getMoves();
							endGame.setVisible(true);
							tie.setVisible(true);		//é mostrado um ecrâ de empate.
							Platform.runLater(new Runnable() {
								
								@Override
								public void run() {
									time.setText(times);
									moves.setText("" + totalMoves);
								}
							});
							
							finished = true;
						}
					}

					if (game != null) game.drawGrid();	//se o jogo ainda permanece.
					try {								//a uma taxa de 20 fps a tela é atualizada com o grid
						Thread.sleep(50);	
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				currentThread().interrupt();	//ao fim do loop do jogo, a thread é interrompida.
			}
		}.start(); //inicia a thread apos criada.
	}
	// Event Listener on Button.onAction
	@FXML
	public void buttonClose(ActionEvent event) {	//ao clicar no botao de fechar o jogo
		exited = true; //o sinal de exit é criado
		try {
			Thread.sleep(50); //apos 50 milis
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Platform.exit(); //a plataforma jfx é encerrada
		System.exit(0); //e depois a aplicação java também;
	}
	// Event Listener on TextField[#ipField].onKeyTyped
	@FXML
	public void ipUpdate(KeyEvent event) {	//ao digitar texto nos campos que são invisíveis, a label formatada é atualizada com o texto correspondente.
		labelIp.setText(ipField.getText());
	}
	// Event Listener on TextField[#username].onKeyTyped
	@FXML
	public void usernameUpdate(KeyEvent event) { //mesma coisa.
		labelUsername.setText(username.getText());
	}
}
