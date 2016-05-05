package interfaces;

import java.net.Socket;

import model.ClientRequestModel;

/**
 * @author Lucas M Lima
 *
 */
public interface ClientCommunication {
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de Inicializa��o do Cliente:
	 *  Recebe o socket do cliente.
	 *  Inicia a Thread de autentica��o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void openCommunication(Socket client);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Autentica��o do Cliente:
	 *  Comunica-se com o socket aguardadndo o identificador do cliente.
	 *  Verifica a autentica��o e informa o status do cliente.
	 *  Caso autenticado, inicia as Threads de comunica��o (leitura e envio).
	 *  E deixa inativa a thread de autentica��o.
	 *  Caso n�o-autenticado, fecha a conex�o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread authentication();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de retorno da identifica��o do cliente:
	 *  Retorna o n�mero identificador do cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public int getId();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de Inclus�o de Nova Requisi��o Recebida:
	 *  O servidor adiciona uma nova requisi��o recebida.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void addFirstRequestReceived(ClientRequestModel clientRequest);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de Leitura da Requisi��o de Envio:
	 *  O servidor faz a leitura de uma requisi��o enviada pelo cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public ClientRequestModel getFirstRequestSend();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Recebimento de Dados:
	 *  Cria a thread de leitura do socket cliente.
	 *  Define a atividade de leitura do socket cliente.
	 *  Envia as informa��es lidas para a lista de requisi��es recebidas.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread receivingRequest();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Envio de Dados:
	 *  Cria a thread de escrita do socket cliente.
	 *  Define a atividade de escrita do socket cliente.
	 *  Pega as requisi��es da lista de envio e envia para o socket cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread sendingRequests();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Verifica a Inatividade do Cliente:
	 *  Verifica se o cliente est� dispon�vel e/ou tem requisi��es.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public boolean isAvailable();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de informa��o:
	 *  Exibe uma mensagem de informa��o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void informationMessage(String message);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Fechamento da Conex�o do Cliente:
	 *  Informa o status do cliente como desconectado.
	 *  Interrompe as threads de leitura e envio do socket cliente.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void closeCommunication();
}
