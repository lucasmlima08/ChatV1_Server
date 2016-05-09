package interfaces;

import java.net.Socket;

/**
 * @author Lucas M Lima
 *
 */
public interface Server {
		
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Inicialização do Servidor:
	 *  Define as informações do servidor e abre a conexão.
	 *  Inicia as threads de comunicação com os clientes.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void startServer();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Recebimento de Clientes:
	 *  Aguarda a conexão de um novo cliente.
	 *  Chama o método de adição de novo cliente ao servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread receivingClients();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Adição de Novo Cliente:
	 *  Recebe o socket do cliente conectado.
	 *  Cria a thread de execução do cliente.
	 *  E adiciona na lista de clientes conectados.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void addClientConnected(Socket client);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *  * * * *
	 *  Thread de Comunicação com Todos os Clientes:
	 *  Percorre a lista de clientes conectados ao servidor.
	 *  Faz a leitura das requisições enviadas por cada cliente.
	 *  Inclui as requisições na lista de requisições do servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread readerRequestsClients();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Thread de Comunicação com Todos os Clientes:
	 *  Percorre a lista de requisições do servidor.
	 *  Percorre a lista de clientes conectados ao servidor.
	 *  Inclui as requisições na lista de recebimento dos clientes requeridos.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public Thread writeRequestsClients();
		
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de informação:
	 *  Exibe uma mensagem de informação.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void informationMessage(String message);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Método de Fechamento do Servidor:
	 *  Suspende todos os clientes e fecha o servidor.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void stopServer();
}
