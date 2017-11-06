package Server;

import io.netty.channel.Channel;

public class Cartes {
    private int nb;
    private String color;

    public Cartes(int _nb, String _color) {
        this.nb = _nb;
        this.color = _color;
    }

    public int getNb() {
        return this.nb;
    }

    public String getColor() {
        return this.color;
    }

    public void display(Channel channel) {
        channel.write(nb + " de " + color + "\n");
    }
}
