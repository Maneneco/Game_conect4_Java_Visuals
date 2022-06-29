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
	public void initialize() { //inicializa os dados da cena principal

	}

	public void reset() {	//limpa os dados da tela e do jogo.

	}
	

	// Event Listener on Button.onAction
	@FXML
	public void buttonRestart(ActionEvent event) {	//o botao de restart serve para limpar os dados
		
	}
	
	// Event Listener on Canvas[#canvasArea].onMouseClicked
	@FXML
	public void canvasMouseClicked(MouseEvent event) {	//quando há um clique
		
	}
	
	// Event Listener on Button.onAction
	@FXML
	public void buttonPlay(ActionEvent event) { // inicia o jogo
		
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
