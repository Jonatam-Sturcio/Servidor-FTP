package bit.servidortcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import org.json.JSONObject;

public class Conexao extends Thread {

	private DataInputStream in;
	private DataOutputStream out;
	private Socket sock;
	private Arquivo arq;

	public Conexao(Socket socket) {
		try {
			sock = socket;
			in = new DataInputStream(sock.getInputStream());
			out = new DataOutputStream(sock.getOutputStream());

		} catch (IOException ex) {
			System.err.println("IOException na classe Conexao: " + ex.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			arq = new Arquivo(sock);
			while (true) {
				JSONObject json = new JSONObject(in.readUTF());

				if ("!close".equalsIgnoreCase(json.getString("command"))) {
					System.out.println("Fechando a conexao com IP:  " + sock.getInetAddress().toString() + ":" + sock.getPort() + ".");
					sock.shutdownInput();
					sock.shutdownOutput();
					break;
				}

				if ("LIST".equalsIgnoreCase( json.getString("command"))) {
					arq.listaArquivos();
				}

				if (("GET").equalsIgnoreCase(json.getString("command"))) {
					if (arq.arquivoExiste(json)) {
						out.writeUTF("true");
						arq.enviaArquivo(json);
					} else {
						out.writeUTF("false");
					}
				}

				if (("PUT").equalsIgnoreCase(json.getString("command"))) {
					arq.recebeArquivo(json);
				}

			}
			System.out.println("Conexao com o cliente encerrada.\n--------------------------//--------------------------");
			sock.close();
			interrupt();

		} catch (IOException ex) {
			System.err.println("IOException na classe Conexao: " + ex.getMessage());
		}
	}
}
