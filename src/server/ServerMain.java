package server;

import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import interfaces.*;
import model.*;

public class ServerMain implements Server {
	
	private ServerSocket server;
	private int serverPort;
	private boolean statusServer;
	private ArrayList<ServerClientCommunication> clientsCommunication;
	private ArrayList<ClientRequestModel> clientsRequests;
	
	public ServerMain() {
		serverPort = 9292;
		clientsCommunication = new ArrayList<ServerClientCommunication>();
		clientsRequests = new ArrayList<ClientRequestModel>();
	}
	
	public static void main(String[] args) {
		ServerMain server = new ServerMain();
		server.startServer();
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Inicialização do Servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void startServer(){
		try {
			// Define as informações e abre a conexão.
			server = new ServerSocket(serverPort);
			informationMessage("Servidor aberto! | "+Inet4Address.getLocalHost().getHostAddress()+" : "+server.getLocalPort()+" |");
			statusServer = true;
			// Inicia as threads do servidor.
			receivingClients().start();
			readerRequestsClients().start();
			//writeRequestsClients().start();
		} catch (Exception e) {
			informationMessage("Erro ao iniciar o servidor: " + e.getMessage());
			statusServer = false;
		}
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Recebimento de Clientes.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread receivingClients() {
		Thread receivedClients = new Thread(new Runnable() {
			public void run() {
				// Abre a autenticação do servidor para novos clientes.
				while (statusServer) {
					informationMessage("Servidor Aguardando clientes!");
					try {
						// Aguarda e recebe um novo cliente.
						// Faz a leitura do id do Cliente.
						Socket client = server.accept();
						int idClient = 0000;
						informationMessage("Cliente Conectado! | "+client.getRemoteSocketAddress() + " - "+ idClient + " |");
						// Adiciona o cliente ao servidor.
						addClientConnected(client);
					} catch (Exception e) {
						informationMessage("Erro ao aguardar clientes: " + e.getMessage());
					}
				}
			}
		});
		return receivedClients;
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Adição de Novo Cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void addClientConnected(Socket client) {
		ServerClientCommunication clientCommunication = new ServerClientCommunication();
		clientCommunication.openCommunication(client);
		clientsCommunication.add(clientCommunication);
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * * * *
	 *  Thread de Comunicação com Todos os Clientes.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread readerRequestsClients() {
		Thread communicationClients = new Thread(new Runnable() {
			public void run() {
				while (statusServer) {
					try {
						ArrayList<ServerClientCommunication> clientsClone = new ArrayList<>();
						// Percorre a lista de clientes conectados ao servidor.
						for (ServerClientCommunication client: clientsCommunication){
							// Verifica a inatividade do cliente.
							if (client.isAvailable()){
								// Adiciona a última requisição do cliente (caso exista) na lista de requisições do servidor.
								ClientRequestModel request = client.getFirstRequestSend();
								if (request != null){
									informationMessage("Requisição lida pelo servidor: " + request.getMessage() + " ::: " + request.getIdSend());
									clientsRequests.add(request);
								}
								clientsClone.add(client);
							}
						}
						// Atualiza a lista de clientes ativos.
						clientsCommunication.clear();
						clientsCommunication.addAll(clientsClone);
					} catch (Exception e) {
						informationMessage("Erro ao aguardar clientes: " + e.getMessage());
					}
				}
			}
		});
		return communicationClients;
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Comunicação com Todos os Clientes.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread writeRequestsClients() {
		Thread communicationClients = new Thread(new Runnable() {
			public void run() {
				while (statusServer) {
					try {
						// Copia as requisições para um array auxiliar e o esvazia.
						ArrayList<ClientRequestModel> requestsCopy = new ArrayList<>(clientsRequests);
						clientsRequests.clear();
						// Percorre a lista de requisições do servidor.
						for (ClientRequestModel request: requestsCopy){
							// Envia as requisições para a lista de requisições dos clientes.
							int indexClient = 0;
							ArrayList<ServerClientCommunication> clientsCopy = new ArrayList<>(clientsCommunication);
							for (ServerClientCommunication client: clientsCopy){
								// Verifica se o cliente possui o id entre os clientes requeridos da requisição.
								if (request.getIdsClientsReceive().contains(client.getId())){
									// Adiciona a requisição na lista de requisições recebidas do cliente.
									clientsCopy.get(indexClient).addFirstRequestReceived(request);
								}
								indexClient++;
							}
						}
					} catch (Exception e) {
						informationMessage("Erro ao aguardar clientes: " + e.getMessage());
					}
				}
			}
		});
		return communicationClients;
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Fechamento do Servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void stopServer() {
		try {
			clientsCommunication.clear();
			clientsRequests.clear();
			server.close();
			informationMessage("Servidor fechado!");
		} catch (Exception e) {
			informationMessage("Erro ao fechar servidor: " + e.getMessage());
		}
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Retorna a mensagem que o(s) cliente(s) vão receber.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void informationMessage(String message) {
		System.out.println(message);
	}
}
