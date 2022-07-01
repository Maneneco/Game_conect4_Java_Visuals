package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import multiplayer.Jogador;
import multiplayer.MultiplayerUtil;

public class Game extends Thread{	//o jogo extende a thread, para que possa continuar sem interrupção do fluxo de dados do servidor
	private GraphicsContext gc;	//contexto grafico que serve para desenhar no ecrã
	private Click click; //o clique no ecrã
	
	private Image grid; //imagem que guarda o tabuleiro.
	private double width, height; //tamanho do tabuleiro
	private int cols, lins; //quantidade de colunas e linhas
	private double cellWidth, cellHeight; //tamanho de cada celula
	private Coin[][] coins; //matriz que guarda as moedas.
	private int[] last; //posicao da ultima moeda inserida.
	
	
	private MultiplayerUtil multiplayer;	//guarda os jogadores e a conexao do jogo pelos sockets.
		
	public Game(Canvas canvas, MultiplayerUtil multiplayer){	//construtor da classe
		gc = canvas.getGraphicsContext2D(); //guarda uma referencia do contexto grafico para desenhar no canvas
		width = canvas.getWidth();	//guarda o valor da largura do canvas
		height = canvas.getHeight();	//altura do canvas
		lins = 6;	//quantidade de linhas
		cols = 7;	//quantidade de colunas
		cellWidth = width/(double)cols;	//largura e altura das celulas que são calculadas
		cellHeight = height/(double)lins;
		coins = new Coin[cols][lins];	//cria a matriz que guarda as moedas na grelha
		last = new int[2];	//vetor que guarda ultima posicao que recebeu uma moeda
		
		
		this.click = new Click();	//cria um buffer para guardar os cliques
		this.multiplayer = multiplayer; //guarda a referencia da ferramenta multiplayer
		
		try {
			grid = new Image(new FileInputStream("res/grid.png"));	//carrega a imagem do tabuleiro
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		drawGrid();	//desenha o tabuleiro
	}
	
	public void drawGrid() {	//funcao que desenha o tabuleiro
		gc.clearRect(0, 0, width, height);	//limpa qualquer residuo de outros frames
		for(Coin[] cr: coins) {	//para cada linha do tabuleiro
			for(Coin c: cr) { //para cada moeda na linha atual
				if(c==null) continue; //se a moeda for nula pula para a proxima posição.
				c.update();	//atualiza os valores da posicao
				c.show();	//desenha a moeda no canvas.
			}
		}
		gc.drawImage(grid, 0, 0, width, height);	//desenha o tabuleiro por cima das moedas
	}
}