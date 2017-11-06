package Client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundMessageHandlerAdapter;
import java.util.Scanner;

public class ChatClientHandler extends ChannelInboundMessageHandlerAdapter<String>{

    @Override
    public void exceptionCaught(ChannelHandlerContext arg0, Throwable cause) {
        arg0.close();
        System.exit(1);
    }

    @Override
    public void messageReceived(ChannelHandlerContext arg0, String arg1) throws Exception {
        System.out.println(arg1);
        if (arg1.contains("Choisissez une carte parmi votre main:")) {
            Scanner scanner = new Scanner (System.in);
            String phrase = scanner.nextLine();
            arg0.channel().write(phrase);
        }
        else if (arg1.contains("perdu") || arg1.contains("gagné")) {
            System.out.println("La partie est finie.");
            System.exit(1);
        }

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("Serveur déconnecté, déconnexion automatique.");
        System.exit(1);
    }

}
