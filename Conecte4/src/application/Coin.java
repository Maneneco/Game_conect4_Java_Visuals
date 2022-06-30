package application;

import javafx.scene.canvas.GraphicsContext;
import multiplayer.Jogador;

public class Coin {	//classe que representa uma das moedas do jogo.
	private GraphicsContext gc;	//contem uma referencia ao contexto gráfico, para que possa ser mostrada.
	private Jogador jogador;	//contem uma referencia ao jogador que colocou a moeda
	private double posX, posY;	//tem uma posicao no ecrã
	private double width, height;	//altura e largura
	private double velY = 0;	//uma velocidade em y, usada na animacao
	private double graV = 10;	//a gravidade da animacao
	private double finY;	//a posicao alvo em y
	
	public Coin(GraphicsContext gc, double posX, double posY, double finY, double width, double height, Jogador jogador) {
		this.gc = gc;
		this.posX = posX * width;
		this.posY = posY * height;
		this.finY = finY * height;
		this.width = width;
		this.height = height;
		this.jogador = jogador;
	}	//construtor
	
	public void update() { //o update é chamado para atualizar a posicao da moeda.
		//Fazer debug da posição da moeda
		//if(velY<2.5) return;	//se a velocidade da moeda for menor que 2.5 a animacao nao funciona;
		velY+=graV;
		if(velY<2.5) return;	//garantia para que não ocorra nada apos a alteracao da velocidade acima.
		posY += velY;	//se nao, a pos e aumentada pela velocidade
		if(posY>=finY) { //se a posicao for maior ou igual ao alvo, ou seja, se chegar ao alvo ou passar por ele numa das atualizacoes
			posY = finY;	//a posicao sera reiniciada para o alvo para impedir que esta apareca seguindo pela moeda de baixo.
			velY*=-0.2;	//e a sua velocidade e invertida, para que a moeda tenha a reacao de queda. assim como também é diminuida, para que não leve tanto tempo a acabar a animação.
		}
	}
	
	public void show() {	//mostra a imagem da moeda na posicao e tamanhos especificados. a imagem é recebida pelo jogador.
		gc.drawImage(jogador.getCoin(), posX, posY, width, height);
	}
	
	public Jogador getJogador() {	//retorna o jogador a quem a moeda pertence
		return this.jogador;
	}

	public String getPos() {	//retorna a posicao da moeda como string, para envio pela rede.
		return posX + " " + posY;
	}
	
}
