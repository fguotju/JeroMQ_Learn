package zeromq.cs;

import org.zeromq.SocketType;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Guofeng3
 * @date 2023Äê09ÔÂ12ÈÕ 14:43
 */
public class Client {
    public static void main(String[] args) {
        try (ZContext context = new ZContext()) {
            //  Socket to talk to server
            System.out.println("Connecting to hello world server");
            List<ZMQ.Socket> list = new ArrayList<>();

            for (int i = 0; i < 100; i++) {
                ZMQ.Socket socket = context.createSocket(SocketType.REQ);
                socket.connect("tcp://localhost:5555");
                list.add(socket);
            }

            list.parallelStream().forEach(Client::process);
        }
    }

    private static void process(ZMQ.Socket socket) {
        for (int requestNbr = 0; requestNbr != 2; requestNbr++) {
            String request = "Hello"+socket.toString();
            System.out.println("Sending Hello " + requestNbr +" T = " +socket.toString());
            socket.send(request.getBytes(ZMQ.CHARSET), 0);

            byte[] reply = socket.recv(0);
            System.out.println(
                    "Received " + new String(reply, ZMQ.CHARSET) + " " +
                            requestNbr
            );
        }
    }
}
