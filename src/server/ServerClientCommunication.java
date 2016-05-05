package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;

import interfaces.*;
import model.*;

public class ServerClientCommunication implements ClientCommunication {
	
	private Socket client;
	private int idClient;
	private boolean clientConnected;
	private ArrayList<ClientRequestModel> requestsReceived, requestsSend;
	private final int timeToAuthenticationMilis = 100000;

	public ServerClientCommunication() {
		requestsReceived = new ArrayList<ClientRequestModel>();
		requestsSend = new ArrayList<ClientRequestModel>();
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Inicialização do Cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void openCommunication(Socket client) {
		this.client = client;
		clientConnected = true;
		authentication().start();
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Autenticação do Cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread authentication(){
		Thread authentication = new Thread(new Runnable() {
			public void run() {
				try {
					boolean available = false;
					long startTime = System.currentTimeMillis();
					PrintStream printStream = new PrintStream(client.getOutputStream());
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
					// Faz a leitura do identificador do cliente.
					informationMessage("Aguardando a autenticação do cliente!");
					while (!available) {
						String read = bufferedReader.readLine();
						// Verifica se o cliente não tem autorização.
						if (read.equals("blocked")){
							printStream.println("blocked");
							closeCommunication();
							available = true;
						}
						// Verifica se leu o id do cliente.
						if (!read.equals("")){
							idClient = Integer.parseInt(read);
							informationMessage("Cliente autenticado: "+idClient);
							printStream.println("accepted");
							receivingRequest().start();
							sendingRequests().start();
							available = true;
						}
						// verifica se esgotou o tempo de verificação da autenticação.
						if ((System.currentTimeMillis() - startTime) >= timeToAuthenticationMilis){
							informationMessage("Esgotado o tempo de autenticação do cliente!");
							printStream.println("blocked");
							closeCommunication();
							available = true;
						}
					}
				} catch (Exception e) {
					informationMessage("Houve um erro ao ler o identificador do cliente: " + e.getMessage());
				}
			}
		});
		return authentication;
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de retorno da identificação do cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public int getId() {
		return idClient;
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Inclusão de Nova Requisição Recebida.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void addFirstRequestReceived(ClientRequestModel clientRequest) {
		requestsReceived.add(clientRequest);
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Leitura da Requisição de Envio.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public ClientRequestModel getFirstRequestSend() {
		if (!requestsSend.isEmpty()) {
			ClientRequestModel request = requestsSend.get(0);
			requestsSend.remove(0);
			return request;
		}
		return null;
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Recebimento de Dados (Recebeu -> Envia).
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread receivingRequest() {
		Thread received = new Thread(new Runnable() {
			public void run() {
				try {
					PrintStream printStream = new PrintStream(client.getOutputStream());
					while (clientConnected) {
						ArrayList<ClientRequestModel> requestsClone = new ArrayList<>(requestsReceived);
						int index = 0;
						// Percorre as requisições recebidas e envia para o cliente.
						for (ClientRequestModel request: requestsClone) {
							printStream.println(request.getMessage());
							requestsReceived.remove(index);
						}
					}
				} catch (Exception e) {
					informationMessage("Houve um erro ao enviar a requisição recebida: " + e.getMessage());
				}
			}
		});
		return received;
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Envio de Dados (Enviou -> Recebe).
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread sendingRequests() {
		Thread send = new Thread(new Runnable() {
			public void run() {
				try {
					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
					while (clientConnected) {
						// Faz a leitura das requisições enviadas pelo cliente e joga na lista.
						String read = bufferedReader.readLine();
						if (!read.equals("")){
							ClientRequestModel request = new ClientRequestModel(idClient, read, null);
							requestsSend.add(request);
						}
					}
				} catch (Exception e) {
					informationMessage("Houve um erro ao ler a requisição do cliente: " + e.getMessage());
				}
			}
		});
		return send;
	}

	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Verifica a Inatividade do Cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public boolean isAvailable() {
		if (!clientConnected && requestsReceived.isEmpty() && requestsSend.isEmpty()) {
			return false;
		}
		return true;
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de informação.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void informationMessage(String message){
		System.out.println(message);
	}
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Fechamento da Conexão do Cliente..
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void closeCommunication() {
		informationMessage("Conexão fechada do cliente!");
		clientConnected = false;
	}
}
