package Server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;

public class ChatServerHandler extends ChannelInboundMessageHandlerAdapter<String>{

    private static final ChannelGroup channels = new DefaultChannelGroup();
    public int client = 1;

    public static ChannelGroup getChannels() {
        return channels;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.write("[SERVER] - " + incoming.remoteAddress() + " s'est connecté !\n");
            client += 1;
        }
        channels.add(ctx.channel());
        incoming.write("[SERVER] - Vous êtes connecté.\nIl y a " + (client - 1) + " clients connectés.\n");
        RoomManager.getRoom().addToRoom(incoming, client);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel incoming = ctx.channel();
        for (Channel channel : channels) {
            channel.write("[SERVER] - " + incoming.remoteAddress() + " s'est deconnecté !\n");
        }
        channels.remove(ctx.channel());
        RoomManager.getRoom().removeFromRoom(incoming);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Client déconnecté, déconnexion automatique.");
        System.exit(1);
    }

    @Override
    public void messageReceived(ChannelHandlerContext arg0, String message) throws Exception {
        Channel incoming = arg0.channel();
        String[] str = message.split(" | \n");
        if (str.length < 2 && !message.isEmpty()) {
            incoming.write("Vous n'avez pas cette carte dans votre main.\n");
            incoming.write("Choisissez une carte parmi votre main:\n");
        }
        else if (message.isEmpty())
        {
            return ;
        }
        else {
            Cartes a = new Cartes(Integer.parseInt(str[0]), str[1]);
            if (RoomManager.getRoom().checkCard(incoming, a) == 2) {
                incoming.write("Vous n'avez pas cette carte dans votre main!.\n");
                incoming.write("Choisissez une carte parmi votre main:\n");
            }
            else {
                RoomManager.getRoom().getTour().add(a);
                RoomManager.getRoom().cartesSpeciales(incoming, a);
                if (RoomManager.getRoom().getTour().size() == 4)
                    RoomManager.getRoom().playRound();
            }
        }
    }
}
