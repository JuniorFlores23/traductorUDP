/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package traductor;

import java.io.*;
import java.net.*;
import javax.swing.JOptionPane;
/**
 *
 * @author junior
 */
public class cliente 
{
   public static byte[] serializar(dataTR obj) throws IOException
   {
        //serializacion del objeto
       ByteArrayOutputStream bs = new ByteArrayOutputStream();
       try (ObjectOutputStream os = new ObjectOutputStream(bs)) 
       {
           os.writeObject(obj);
       }
       return bs.toByteArray();
   }
   public static dataTR deserializar(byte[] array) throws IOException, ClassNotFoundException
   {
       ByteArrayInputStream bs = new ByteArrayInputStream(array);
       dataTR obj;
       try (ObjectInputStream is = new ObjectInputStream(bs)) {
           obj = (dataTR)is.readObject();
       }
       return obj;
   }
   public static void main(String[] args) throws SocketException, UnknownHostException, IOException
   {
       final int puertoServidor = 1099;//puerto del servidor
       DatagramSocket cliente = new DatagramSocket();//socket del cliente
       byte[] buffer = new byte[1024];//buffer para el envio de datos
       
       String direccion = JOptionPane.showInputDialog("Ingrese la ip del servidor: ");
       InetAddress ip = InetAddress.getByName(direccion);
       dataTR data = new dataTR();//objeto de la clase para guardar las opciones del cliente
       data.tipo = Integer.parseInt(JOptionPane.showInputDialog("Elija su opcion:\n 1.Ing a Esp\n 2.Esp a Ing\n 3.Salir"));
       switch(data.tipo)
       {
           case 1:
               data.palabra = JOptionPane.showInputDialog("Enter the word to translate: ");
               break;
           case 2:
               data.palabra = JOptionPane.showInputDialog("Ingrese la palabra a traducir: ");
               break;
           case 3:
               JOptionPane.showMessageDialog(null, "Hasta luego....");
               System.exit(0);
           default:
               JOptionPane.showMessageDialog(null, "Opcion incorrecta....");
               System.exit(0);
       }
       
       buffer = serializar(data);//serializamos el objeto para guardarlo en el buffer
       DatagramPacket solicitud = new DatagramPacket(buffer, buffer.length, ip, puertoServidor);//preparamos el paquete con la info y los datos del servidor
      
       cliente.send(solicitud);//enviamos la solicitud
       
       DatagramPacket respuesta = new DatagramPacket(buffer, buffer.length);//preparamos pauqete para la respuesta del server
       cliente.receive(respuesta);//recivimos las respuestas
       
       String mensaje = new String(respuesta.getData(), respuesta.getOffset(), respuesta.getLength()); //convertimos a string la respues y la imprimimos
       JOptionPane.showMessageDialog(null, "Esp=ing\n"+mensaje+"\n");
       cliente.close();
   }
}
