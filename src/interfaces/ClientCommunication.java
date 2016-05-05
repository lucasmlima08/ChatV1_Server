package interfaces;

import java.net.Socket;

import model.ClientRequestModel;

/**
 * @author Lucas M Lima
 *
 */
public interface ClientCommunication {
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Inicialização do Cliente:
	 *  Recebe o socket do cliente.
	 *  Inicia a Thread de autenticação.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void openCommunication(Socket client);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Autenticação do Cliente:
	 *  Comunica-se com o socket aguardadndo o identificador do cliente.
	 *  Verifica a autenticação e informa o status do cliente.
	 *  Caso autenticado, inicia as Threads de comunicação (leitura e envio).
	 *  E deixa inativa a thread de autenticação.
	 *  Caso não-autenticado, fecha a conexão.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread authentication();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de retorno da identificação do cliente:
	 *  Retorna o número identificador do cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public int getId();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Inclusão de Nova Requisição Recebida:
	 *  O servidor adiciona uma nova requisição recebida.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void addFirstRequestReceived(ClientRequestModel clientRequest);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Leitura da Requisição de Envio:
	 *  O servidor faz a leitura de uma requisição enviada pelo cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public ClientRequestModel getFirstRequestSend();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Recebimento de Dados:
	 *  Cria a thread de leitura do socket cliente.
	 *  Define a atividade de leitura do socket cliente.
	 *  Envia as informações lidas para a lista de requisições recebidas.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread receivingRequest();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Envio de Dados:
	 *  Cria a thread de escrita do socket cliente.
	 *  Define a atividade de escrita do socket cliente.
	 *  Pega as requisições da lista de envio e envia para o socket cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread sendingRequests();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Verifica a Inatividade do Cliente:
	 *  Verifica se o cliente está disponível e/ou tem requisições.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public boolean isAvailable();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de informação:
	 *  Exibe uma mensagem de informação.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void informationMessage(String message);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Fechamento da Conexão do Cliente:
	 *  Informa o status do cliente como desconectado.
	 *  Interrompe as threads de leitura e envio do socket cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void closeCommunication();
}
