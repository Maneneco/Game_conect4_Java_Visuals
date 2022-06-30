package application;

import javafx.scene.canvas.GraphicsContext;
import multiplayer.Jogador;

public class Coin { 
    private GraphicsContext gc; 
    private Jogador jogador;    
    private double posX, posY;  
    private double width, height;   
    private double velY = 0;    
    private double graV = 10;   
    private double finY;    
    
    public Coin(GraphicsContext gc, double posX, double posY, double finY, double width, double height, Jogador jogador) {
        this.gc = gc;
        this.posX = posX * width;
        this.posY = posY * height;
        this.finY = finY * height;
        this.width = width;
        this.height = height;
        this.jogador = jogador;
    }   //construtor simples
    
    public void update() { 
        if(velY<2.5) return;    
        velY+=graV;
        if(velY<2.5) return;    
        posY += velY;   
        if(posY>=finY) { 
            posY = finY;    
            velY*=-0.2; 
        }
    }
    
    public void show() {    
        gc.drawImage(jogador.getCoin(), posX, posY, width, height);
    }
    
    public Jogador getJogador() {   
        return this.jogador;
    }

    public String getPos() {    
        return posX + " " + posY;
    }
    
}


