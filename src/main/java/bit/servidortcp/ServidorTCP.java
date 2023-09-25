package bit.servidortcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {

	public static void main(String[] args) {
		int port = 50000;
		try {
			ServerSocket listenSock = new ServerSocket(port, 5);
			while (true) {
				System.out.println("\tAguardando conexao do cliente...");
				Socket clientSock = listenSock.accept();
				System.out.println("Conectado ao Ip: " + clientSock.getInetAddress().toString() + ":" + clientSock.getPort());

				Conexao c = new Conexao(clientSock);
				c.start();
			}
		} catch (IOException e) {
			System.err.println("IOException na classe ServidorTCP: " + e.getMessage());
		}
	}
}
