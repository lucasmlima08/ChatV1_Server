package interfaces;

import java.util.ArrayList;

/**
 * @author Lucas M Lima
 *
 */
public interface ClientRequest {
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Define o(s) cliente(s) que v�o receber a requisi��o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void setIdSend(int idSend);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Retorna os clientes que v�o receber a requisi��o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public int getIdSend();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Define a mensagem que o(s) cliente(s) v�o receber.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void setMessage(String message);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Retorna a mensagem que o(s) cliente(s) v�o receber.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public String getMessage();
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Define o(s) cliente(s) que v�o receber a requisi��o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public void setIdsClientsReceive(ArrayList<Integer> idsClientsReceived);
	
	/** * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
	 *  Retorna os clientes que v�o receber a requisi��o.
	 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
	public ArrayList<Integer> getIdsClientsReceive();
}
