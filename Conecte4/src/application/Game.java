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
	
	public boolean addCoin(int x, int y_) {	//adiciona uma moeda na posicao (x.y_)
		int y;	//posicao y temporaria para verificacao
		
		if(coins[x][0]!=null) return false; //se ja houver uma moeda no local clicado retorna falso
		for(y=lins-1;y>0;y--) {	//passa de baixo para cima na coluna procurando o primeiro local livre.
			if(coins[x][y]==null)break; //ao encontrar um espaco livre sai do loop.
		}
		Coin c = new Coin(gc, x, 0, y,cellWidth, cellHeight, multiplayer.getJogadorAtual());	//cria uma moeda na posicao x y_, com alvo y.
		coins[x][y] = c;	//guarda a moeda no grid
		last[0] = x; //guarda a ultima posicao
		last[1] = y;
		return true; //retorna verdadeiro porque adicionou uma moeda
	}
	
	public Jogador update() { //atualiza o jogo
		if(multiplayer.taNaVez()) { //se tiver na vez do jogador local
			String dados = multiplayer.recebeDados(); // tenta receber dados
			if(dados!=null) { //se tiver recebido algum dado
				if(dados.contains("exit")) return multiplayer.getEste(); //testa para ver se foi uma desistencia, se sim, retorna o jogador local
				else if (dados.contains(multiplayer.getAdv().getName())) return multiplayer.getAdv(); //se nao, testa para ver se há o nome do jogador remoto, se sim, retorna.
			}
			if(!click.isEmpty()) { //se o click nao estiver vazio
				int dad = (int)(click.getX()/cellWidth); // guarda o click de forma tratada para que se traduza as linhas e colunas do grid
				int dos = (int)(click.getY()/cellHeight);
				if (coins[dad][dos]!=null) return null;  //se a posicao clicada for vazia, retorna null (nao faz jogada alguma);
				addCoin(dad,dos);	//se nao, adiciona uma moeda à posição
				multiplayer.enviarDados(dad+" "+dos); //envia a posicao da moeda ao jogador remoto
				click.reset(); //limpa o click
				Jogador v = verificaFim(); //verifica se o jogo terminou, recebendo o jogador que deve ter vencido o jogo.
				multiplayer.addMove(); //adiciona um movimento ao jogador atual
				if (v!=null) return v; //se o jogo terminou, retorna o jogador vencedor
				multiplayer.mudaJogador();	//se o jogo nao terminou, passa a vez
			}
		} else {	//se nao for a vez do jogador local, 
			String dados = multiplayer.recebeDados(); 	//recebe dados
			if(dados!=null) {	//se os dados nao forem nulos
				if(dados.contains("exit")) return multiplayer.getEste(); //retorna o jogador local, em caso de desistencia do remoto
				else if (dados.contains(multiplayer.getAdv().getName())) return multiplayer.getAdv(); //ou o jogador remoto caso este tenha ganhado.
				
				int dad = (int)Double.parseDouble(dados.split(" ")[0]);	//divide os dados recebidos em duas variaveis
				int dos = (int)Double.parseDouble(dados.split(" ")[1]);
				
				addCoin(dad,dos);	//adiciona a moeda no local indicado.
				multiplayer.addMove();	//adiciona o movimento do jogador
				multiplayer.mudaJogador(); //e passa a vez.
			}
		}
		return null;	//se o jogo ainda nao terminou, retorna nulo
	}

	public Jogador verificaFim() {	//verificando se o jogo terminou
		int count = 1;	//verifica horizontal	estes contadores contam quantas peças do jogador remoto estão alinhadas a partir da ultima moeda adicionada.
		
		for(int x=1;x<4;x++) {	//conta horizontalmente a partir da moeda atual
			if(last[0]-x<0) break;
			else if(coins[last[0]-x][last[1]]==null) break;	//se achar outra moeda numa distancia de até 3 moeda, não é possivel que tenha vencido, logo sai do loop.
			else if (coins[last[0]-x][last[1]].getJogador()==multiplayer.getEste()) {
				 count++;
			}
		}
		for(int x=1;x<4;x++) {	//faz a mesma coisa para o outro lado.
			if(last[0]+x>=cols) break;
			else if(coins[last[0]+x][last[1]]==null) break;
			else if (coins[last[0]+x][last[1]].getJogador()==multiplayer.getEste()){
				 count++;
			}
		}
		if(count == 4) return multiplayer.getEste();	//se o contador chega a 4. o jogador local venceu, e é retornado.
		
		count = 1;	//verifica vertical	//faz o mesmo que o acima, porém verticalmente.

		for(int y=1;y<4;y++) {
			if(last[1]-y<0) break;
			else if(coins[last[0]][last[1]-y]==null) break;
			else if (coins[last[0]][last[1]-y].getJogador()==multiplayer.getEste()){
				 count++;
			}
		}
		for(int y=1;y<4;y++) {
			if(last[1]+y>=lins) break;
			else if(coins[last[0]][last[1]+y]==null) break;
			else if (coins[last[0]][last[1]+y].getJogador()==multiplayer.getEste()){
				 count++;
			}
		}
		if(count == 4) return multiplayer.getEste();
		
		count = 1; //diagonal desc	//faz a mesma coisa que os acima, porem o index altera tanto x quanto y, movendo-se diagonalmente de forma descendente.
		
		for(int xy=1;xy<4;xy++) {
			if(last[0]-xy<0||last[1]-xy<0) break;
			else if(coins[last[0]-xy][last[1]-xy]==null) break;
			else if (coins[last[0]-xy][last[1]-xy].getJogador()==multiplayer.getEste()){
				 count++;
			}
		}
		for(int xy=1;xy<4;xy++) {
			if(last[0]+xy>=cols||last[1]+xy>=lins) break;
			else if(coins[last[0]+xy][last[1]+xy]==null) break;
			else if (coins[last[0]+xy][last[1]+xy].getJogador()==multiplayer.getEste()){
				 count++;
			}
		}
		if(count == 4) return multiplayer.getEste();
		
		count = 1; //diagonal asc	//faz quase o mesmo do acima, porém o index y é invertido, movendo-se de forma ascedente.
		
		for(int xy=1;xy<4;xy++) {
			if(last[0]-xy<0||last[1]+xy>=lins) break;
			else if(coins[last[0]-xy][last[1]+xy]==null) break;
			else if (coins[last[0]-xy][last[1]+xy].getJogador()==multiplayer.getEste()){
				 count++;
			}
		}
		for(int xy=1;xy<4;xy++) {
			if(last[0]+xy>=cols||last[1]-xy<0) break;
			else if(coins[last[0]+xy][last[1]-xy]==null) break;
			else if (coins[last[0]+xy][last[1]-xy].getJogador()==multiplayer.getEste()){
				 count++;
			}
		}
		if(count == 4) return multiplayer.getEste();
		
		return null;
	}

	public void setClick(double x, double y) {	//adiciona um clique ao jogador atual.
		if(multiplayer.taNaVez()) {	//apenas se for a vez dele.
			click.set(x, y);
		}
	}

	public boolean verificaEmpate() {	//verifica a condição de empate do jogo.
		for(Coin[] cr: coins) {	//a cada linha do grid
			for(Coin c: cr) { //a cada moeda da linha
				if(c==null) return false; // se houver um espaço vazio, ainda não é considerado empate.
			}
		}
		return true; 	//só é empate caso não haja mais espaços vazios.
	}
}