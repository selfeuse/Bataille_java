package Server;

import io.netty.channel.Channel;

import java.util.List;

public class Player {
    private int id;
    private Main hand;
    private int play = 0;
    private Channel channel;

    public Player(Channel channel, int id){
        this.id = id;
        this.channel = channel;
    }

    public int getId(){ return this.id;}
    public void setId(int _id){ this.id = _id;}
    public Main getMain(){ return this.hand;}
    public void setMain(Main _main){ this.hand = _main;}
    public int getPlay(){ return play;}
    public Channel getChannel() { return this.channel;}
    public void setPlay(int _play){ play = _play;}
    public void displayCartes(Channel channel, List<Cartes> cartes) {
        for (Cartes cartes1 : cartes)
            channel.write(cartes1.getNb() + " " + cartes1.getColor() + " - ");
        channel.write("\n");
    }
}
