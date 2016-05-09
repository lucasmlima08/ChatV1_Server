package interfaces;

import java.net.Socket;

/**
 * @author Lucas M Lima
 *
 */
public interface Server {
		
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Inicializa��o do Servidor:
	 *  Define as informa��es do servidor e abre a conex�o.
	 *  Inicia as threads de comunica��o com os clientes.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void startServer();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Recebimento de Clientes:
	 *  Aguarda a conex�o de um novo cliente.
	 *  Chama o m�todo de adi��o de novo cliente ao servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread receivingClients();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de Adi��o de Novo Cliente:
	 *  Recebe o socket do cliente conectado.
	 *  Cria a thread de execu��o do cliente.
	 *  E adiciona na lista de clientes conectados.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void addClientConnected(Socket client);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * * * *
	 *  Thread de Comunica��o com Todos os Clientes:
	 *  Percorre a lista de clientes conectados ao servidor.
	 *  Faz a leitura das requisi��es enviadas por cada cliente.
	 *  Inclui as requisi��es na lista de requisi��es do servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread readerRequestsClients();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Comunica��o com Todos os Clientes:
	 *  Percorre a lista de requisi��es do servidor.
	 *  Percorre a lista de clientes conectados ao servidor.
	 *  Inclui as requisi��es na lista de recebimento dos clientes requeridos.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread writeRequestsClients();
		
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de informa��o:
	 *  Exibe uma mensagem de informa��o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void informationMessage(String message);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  M�todo de Fechamento do Servidor:
	 *  Suspende todos os clientes e fecha o servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void stopServer();
}
