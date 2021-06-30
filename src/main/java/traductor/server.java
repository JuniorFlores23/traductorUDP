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
public class server 
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
    public static void main(String[] args) throws SocketException, IOException, ClassNotFoundException
    {
        DatagramSocket socketUDP = new DatagramSocket(1099); //conexion del server
        JOptionPane.showMessageDialog(null,"Servidor iniciado");
        while(true)
        {
            byte[] buffer = new byte[1024];//buffer para la recepcion y envio de paquetes
            String mensaje;
            dataTR obj;

            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);//variable para guardar la peticion del clientee

            socketUDP.receive(peticion);//guardando la peticion del cliente
            obj = deserializar(peticion.getData());
            //JOptionPane.showMessageDialog(null, "Opcion: "+obj.tipo+"\nPalabra: "+obj.palabra);

            mensaje = buscar(obj.palabra, obj.tipo);
            byte[] buffer1 = new byte[1024];
            buffer1 = mensaje.getBytes();
            int puertoCliente = peticion.getPort();
            InetAddress direccion = peticion.getAddress();
            DatagramPacket respuesta = new DatagramPacket(buffer1, buffer1.length, direccion, puertoCliente);//paquete para enviar la respuesta al cliente
            socketUDP.send(respuesta); //enviamos respues al cliente
        
        }
    }
    
       
   public static String buscar(String palabra, int opc)
   {
       File archivo = new File("/home/junior/Documentos/diccionario.txt");
       try
       {//verificar si el archivo existe
           if(archivo.exists())
           {
               BufferedReader leerArchivo = new BufferedReader(new FileReader(archivo));
               String linealeida;
               while((linealeida = leerArchivo.readLine()) != null)
               {
                   //separa la linea en un arreglo de palabras
                   String[] palabras = linealeida.split("=");
                   //recorremos el arreglo de palabras
                   for(int i=0; i<palabras.length; i++)
                   {
                       if(palabras[i].equalsIgnoreCase(palabra))
                       {
                           return linealeida;
                       }
                   }
                   
               }
           }
            else
                return opc==1? "word not found" : "palabra no encontrada";
       }catch(Exception e)
       {
           System.out.println(e.getMessage());
           return opc==1? "word not found" : "palabra no encontrada";
       }
    return opc==1? "word not found" : "palabra no encontrada";
   }
}
