package Server;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.List;

public class RoomManager {
    private final List<Player> game = new ArrayList<>();
    private final List<Player> waitingList = new ArrayList<>();
    private static final RoomManager room = new RoomManager();
    private final List<Cartes> tour = new ArrayList<>();
    private final List<Integer> tourInt = new ArrayList<>();

    public static RoomManager getRoom() { return room;}
    public List<Cartes> getTour() {return tour;}
    public List<Integer> getTourInt() {return tourInt;}
    public void addToRoom(Channel channel, int id) {
        Player play = new Player(channel, id);
        if (game.size() < 4) {
            channel.write("En attente d'autres joueurs ...\r\n");
            game.add(play);
        }
        else {
            play.getChannel().write("La room est complète. Ajout à la liste d'attente.\n");
            waitingList.add(play);
        }
        if (game.size() == 4) {
            for (int i = 1; i <= 4; i += 1) {
                game.get(i - 1).getChannel().write("Vous êtes le joueur " + i + ".\n");
                game.get(i - 1).getChannel().write("Le jeu commence.\n");
                game.get(i - 1).setMain(distCards(i));
                game.get(i - 1).setId(i);
            }
            display(game);
        }
    }

    public void removeFromRoom(Channel incoming) {
        for (int i = 0; i < game.size(); i += 1) {
            if (game.get(i).getChannel() == incoming) {
                game.remove(i);
            }
        }

    }

    public Main distCards(int i) {
        Main hand = new Main();
            for (int j = 7; j <= 13; j += 1) {
                if (i == 1) {hand.addCartes(new Cartes(j, "coeur"));}
                else if (i == 2) {hand.addCartes(new Cartes(j, "carreau"));}
                else if (i == 3) {hand.addCartes(new Cartes(j, "trefle"));}
                else if (i == 4) {hand.addCartes(new Cartes(j, "pique"));}
            }
        return hand;
    }

    public void display(List<Player> game) {
        for (Player display : game) {
            if (!display.getMain().getCartes().isEmpty()) {
                display.getChannel().write("**************************\r\n");
                display.getChannel().write("Il vous reste : ");
                display.displayCartes(display.getChannel(), display.getMain().getCartes());
                display.getChannel().write("Choisissez une carte parmi votre main:\n");
            }
        }
    }

    public int checkCard(Channel incoming, Cartes a) {
        int j = 0;
        for (Player play: game) {
            if (play.getChannel() == incoming) {
                for (int i = 0; i < play.getMain().getCartes().size(); i += 1) {
                    if (play.getMain().getCartes().get(i).getNb() == a.getNb() && play.getMain().getCartes().get(i).getColor().equals(a.getColor())) {
                        getRoom().getTourInt().add(j);
                        play.getMain().deleteCartes(i);
                        return 1;
                    }
                }
            }
            j += 1;
        }
        return 2;
    }

    public int isFinish(List<Player> game) {
        int finish = 0;
        for (Player play : game) {
            if (play.getMain().getCartes().isEmpty()) {
                finish += 1;
            }
        }
        if (finish == 3) {
            for (Player play : game) {
                if (play.getMain().getCartes().isEmpty()) {
                    play.getChannel().write("Vous avez perdu.\n");
                }
                else {
                    play.getChannel().write("Vous avez gagné.\n");
                }
            }
            return 1;
        }
        else {
            return 0;
        }
    }

    public void playRound() {
        int nb = 0;
        int winner = 0;
        for (int i = 0; i < 4; i += 1) {
            if (tour.get(i).getNb() > nb) {
                nb = tour.get(i).getNb();
                winner = tourInt.get(i);
            }
        }
        for (Player play: game) {
            play.getChannel().write ("Le joueur " + (winner + 1) + " a gagné la manche.\n");
            play.getChannel().write("\n");
        }
        for (Cartes a: tour) {
            game.get(winner).getMain().addCartes(a);
        }
        tour.clear();
        tourInt.clear();
        if (isFinish(game) == 0) {
            display(game);
        }
    }

    public void cartesSpeciales(Channel channel, Cartes a) {
        if (a.getNb() < 11) {
            channel.write("\nVous avez joué: ");
            a.display(channel);
        }
        else if (a.getNb() == 11) {
            channel.write("\nVous avez joué: Valet de " + a.getColor() + '\n');
        }
        else if (a.getNb() == 12) {
            channel.write("\nVous avez joué: Reine de " + a.getColor() + '\n');
        }
        else if (a.getNb() == 13) {
            channel.write("\nVous avez joué: Roi de " + a.getColor() + '\n');
        }

    }

}
