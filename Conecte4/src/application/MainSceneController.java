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

public class MainSceneController {

	private Label labelIp;
	@FXML
	private Label movesoutro;
	@FXML
	private Label nomeeste;
	@FXML
	private Label labelUsername;
	@FXML
	private Label time;
	@FXML
	private Label moves;
	@FXML
	private Label nomeeste;
	@FXML
	private Label nomeoutro;
	@FXML
	private Label progressing;
	@FXML
	private AnchorPane canvasAnchor;
	@FXML
	private AnchorPane loginScreen;
	@FXML
	private AnchorPane endGame;
	@FXML
	private ImageView won;
	@FXML
	private ImageView lost;
	@FXML

	//teste
	@FXML
	public void initialize() { //inicializa os dados da cena principal
		Font f;	//fonte a ser usada
		try {
			f = Font.loadFont(new FileInputStream("res/font/aller-display.ttf"), 14); //carrega a fonte no tamanho 14
			labelIp.setFont(f); //seta a fonte para objetos da cena
			moveseste.setFont(f);
			movesoutro.setFont(f);
			labelUsername.setFont(f);
			time.setFont(f);
			moves.setFont(f);
			f = Font.loadFont(new FileInputStream("res/font/aller-display.ttf"), 18); //mesma coisa, porem tamanho 18
			progressing.setFont(f);
			nomeeste.setFont(f);
			nomeoutro.setFont(f);
			progressing.setText("Aguardando Advers�rio!\nIP:" + MultiplayerUtil.getIp()); //seta o ip na mensagem de espera.
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void reset() {	//limpa os dados da tela e do jogo.
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
	
	/ Event Listener on Canvas[#canvasArea].onMouseClicked
	@FXML
	public void canvasMouseClicked(MouseEvent event) {	//quando há um clique
		if (game==null) return; //caso o game não tenha iniciado, nada é feito
		game.setClick(event.getX(),event.getY()); //se nao, envia o clique ao jogo
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void buttonPlay(ActionEvent event) { // inicia o jogo
		if(username.getText().isEmpty()) return; // se nao houver um nome de usuario, nao inicia o jogo.
		new Thread() { //cria uma thread para iniciar o jogo.
			@Override
			public void run() {	//funcao da thread
				Image righton=null, 	//imagens a serem usadas para indicar a vez dos jogadores;
						rightoff=null,
						lefton=null,
						leftoff=null;
				try {
					loginScreen.setVisible(false);	//esconde a tela de login
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
					canvasAnchor.setVisible(true); //mostra o canvas na tela
				} catch (Exception e) {
					error.setVisible(true); //em caso de erro, é mostrado o painel de erro
					loginScreen.setVisible(false);	//a tela de login some
					erroTXT.setText(e.getMessage()); //e o erro é mostrado
				}
	}
	// Event Listener on Button.onAction
	@FXML
	public void buttonClose(ActionEvent event) {	//ao clicar no botao de fechar o jogo
		
	}
	// Event Listener on TextField[#ipField].onKeyTyped
	@FXML
	public void ipUpdate(KeyEvent event) {	//ao digitar texto nos campos que são invisíveis, a label formatada é atualizada com o texto correspondente.
		
	}
	
	@FXML
	public void usernameUpdate(KeyEvent event) { //mesma coisa.
		
	}
}
