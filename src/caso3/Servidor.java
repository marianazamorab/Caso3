package caso3;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {
	
	static ElementosTabla prueba = new ElementosTabla("nombreCliente", "2", "PKT_ENVIADO");
	static ElementosTabla prueba2 = new ElementosTabla("nombreCliente", "3", "PKT_ENVIADO");

	private static String llavePrivada = "llaveprivada";
	private static int numeroClientes=5;
	private static ElementosTabla[] tabla= new ElementosTabla[numeroClientes];

	public static void main (String[] args){
				
		tabla[0] = (prueba);
		tabla[1] = (prueba2);

		String nombreActual= "";

		try{
			ServerSocket receptor = new ServerSocket(1234);
			Socket sock = receptor.accept();

			InputStreamReader isr= new InputStreamReader(sock.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			PrintWriter pw = new PrintWriter(sock.getOutputStream());
			String s = br.readLine();

			while(s!="" && !s.contentEquals("TERMINAR")){

				if(s.contentEquals("INICIO")){
					System.out.println("Cliente conectado");
					pw.println("ACK");
					pw.flush();

				}else if(s.length() == 24){
					
					System.out.println("Numero de reto recibido");
					/*TODO cifrado */

					pw.println(s+llavePrivada);
					pw.flush();

				}else if(s.startsWith("cif")){
					
					System.out.println("Llave simétrica recibida");
					/*TODO extraer llave simetrica*/
					pw.println("ACK");
					pw.flush();		

				}else if(s.startsWith("nombre")){
					
					System.out.println("Nombre de cliente recibido");
					String nombreCliente = s.substring(6);

					for(int i=0; i<tabla.length; i++){
						
						if(tabla[i].nombreCliente.contentEquals(nombreCliente)){
							nombreActual=nombreCliente;
							pw.println("ACK");
							pw.flush();
							break;

						}else{
							pw.println("ERROR");
							pw.flush();
						}	
					}
				}else if(s.startsWith("id")){
					
					System.out.println("Id del paquete recibido");
					for(int i=0; i<tabla.length; i++){
						
						if(tabla[i].nombreCliente.contentEquals(nombreActual) && tabla[i].idPaquete.contentEquals(s.substring(2))){
							pw.println(tabla[i].estadoPaquete);
							pw.flush();	
							break;
						}else{
							pw.println("ERROR");
							pw.flush();
						}
					}
				}else if(s.contentEquals("ACK")){
					
					System.out.println("Servidor puede enviar mensaje de resumen");
					/*TODO mensaje de resumen*/
					String resumen= "resumen";
					pw.println(resumen);
					pw.flush();
				}

				s= br.readLine();
			}

			System.out.println("Terminó la conexión con el cliente");
		}catch (Exception e){
			e.printStackTrace();
		}

	}
	
}


