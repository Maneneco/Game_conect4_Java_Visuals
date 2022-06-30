package multiplayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.image.Image;

public class Jogador {	
	private String name; 
	private Image coin;	
	private int moves, code;
	
	public Jogador(String name, int code, int moves) {
		this.name = name;
		this.moves = moves;
		this.code = code;
		try {	
			coin = new Image(new FileInputStream("res/" + (code==1?"rpiece":"ypiece") + ".png"));
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMoves() {
		return moves;
	}

	public void setMoves(int moves) {
		this.moves = moves;
	}

	public Image getCoin() {
		return coin;
	}

	public void setCoin(Image coin) {
		this.coin = coin;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void addMove() {
		moves++;
	}
}