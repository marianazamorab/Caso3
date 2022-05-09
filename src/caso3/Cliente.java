package caso3;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class Cliente {

	public static void main (String[] args){

		String publicKeyServidor="";
		String nombre= "nombreCliente";
		String idPaquete = "2";

		try{
			String ruta = System.getProperty("user.dir")+"/src/llavepublica.txt";
			File archivo = new File(ruta);
			FileReader fr = new FileReader(archivo);
			BufferedReader br = new BufferedReader(fr);
			publicKeyServidor = br.readLine();		

		}catch (Exception e){
			System.out.println("Hubo un error al leer la llave pública del servidor");
		}

		try{

			Socket sock = new Socket("localhost", 1234);

			InputStreamReader isr= new InputStreamReader(sock.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			PrintWriter pw = new PrintWriter(sock.getOutputStream());
			pw.println("INICIO");
			pw.flush();

			String s = br.readLine();
			String mensaje="";
			String rand= "random";

			while(s!=""){
				
				if(s.contentEquals("ACK") && mensaje==""){
					System.out.println("Cliente conectado a servidor");
					rand = randomN();
					mensaje = rand;
					pw.println(rand);
					pw.flush();

				}else if( mensaje == rand ){
					
					System.out.println("Llave privada cifrada recibida");
					/*TODO des cifrado*/
					String mensajeDescifrado = rand+ "llaveprivada"; /*Este seria el mensaje descifrado*/
					/*condicional para ver si todo bien*/
					if(mensajeDescifrado.contentEquals(rand+"llaveprivada") ){
						/*TODO generado llave simetrica y cifrado con llave publica servidor*/
						String cifrado = "cif";
						cifrado+="";
						mensaje = cifrado;
						pw.println(cifrado);
						pw.flush();

					}else{
						pw.println("TERMINAR");
						pw.flush();
					}
				}else if(s.contentEquals("ACK") && mensaje.startsWith("cif")){
					
					System.out.println("Cliente puede enviar el nombre");
					pw.println("nombre"+nombre);
					mensaje = nombre;
					pw.flush();
					
				}else if(s.contentEquals("ERROR")){
					System.out.println("El servidor no cuenta con información para este cliente");
					break;
					
				}else if(s.contentEquals("ACK") && mensaje.startsWith("nombre")){
					
					System.out.println("Nombre de cliente aceptado, puede enviar id del paquete");
					pw.println("id"+idPaquete);
					mensaje="id"+idPaquete;
					pw.flush();
					
				}else if(s.startsWith("PKT")){
					
					System.out.println("Estado del paquete recibido");
					pw.println("ACK");
					mensaje = "ACK";
					pw.flush();
				
				}else if( mensaje == "ACK"){
					
					System.out.println("Mensaje de resumen recibido");
					/*TODO revisar resumen*/
					if(s.contentEquals("resumen")){
						/*Es decir si se cumple que el resumen esté bien */
						System.out.println("info impresa en consola"); /*despliega info en consola*/
					}
					pw.println("TERMINAR");
					pw.flush();
					break;
				}
				
				s = br.readLine();
			}
			
			System.out.println("La conexión con el servidor ha finalizado");

		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String randomN(){

		String s="";

		Random random = new Random();

		for(int i=0; i<24; i++){
			s+= random.nextInt(10);
		}

		return s;

	}
}
