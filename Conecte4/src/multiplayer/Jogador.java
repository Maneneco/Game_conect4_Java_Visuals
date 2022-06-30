package multiplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class Jogador {	//jogador
	private String name; //dados
	private Image coin;	//imagem a ser usada pelas moedas do jogador
	private int moves, code;
	
	public Jogador(String name, int code, int moves) { //Construtor
		this.name = name;
		this.moves = moves;
		this.code = code;
		try {	//apos guardar os dados, carrega a imagem a ser usada por este jogador
			coin = new Image(new FileInputStream("res/" + (code==1?"rpiece":"ypiece") + ".png"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Getter para o nome
	public String getName() {
		return name;
	}

	//Setter para o nome
	public void setName(String name) {
		this.name = name;
	}

	//getter para os movimentos
	public int getMoves() {
		return moves;
	}

	//setter para os moviemtos
	public void setMoves(int moves) {
		this.moves = moves;
	}

	//getter para a coin
	public Image getCoin() {
		return coin;
	}

	//setter para a coin
	public void setCoin(Image coin) {
		this.coin = coin;
	}

	public int getCode() {
		return code;
	}

	//setter para o codigo da coin
	public void setCode(int code) {
		this.code = code;
	}

	//Adiciona moves ao contador
	public void addMove() {
		moves++;
	}
}