/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.satyen.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Hashtable;

/**
 * This class accepts the new class class from server and creates its object.
 * This should create object of class though class is not defined in the
 * context.
 *
 * @author Satyen S Shimpi
 */
public class Listener extends ClassLoader{

    boolean shallRun = false;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        //String host = "172.28.1.81";	
        //String host = "172.28.1.96";	
        String host = "10.143.14.175";
        int port = 19999;

        StringBuffer instr = new StringBuffer();
        String TimeStamp;
        System.out.println("SocketClient initialized");

//while(true)
        try {
            InetAddress address = InetAddress.getByName(host);
            //for testing use local host
            address = InetAddress.getLocalHost();
            
            Socket connection = new Socket(address, port);
            ObjectInputStream oIn = new ObjectInputStream(connection.getInputStream());
            Hashtable in = (Hashtable) oIn.readObject();
                        
            connection.close();

            System.out.println("received class:" + in.getClass().getName());
            String keyClassName = in.keys().nextElement().toString();
            byte[] code = (byte[]) in.get(keyClassName);
            Class<?> definedClass = new Listener().defineClass(keyClassName, code, 0, code.length);
            //            in.getClass().getMethods()[0].invoke(null, new Object[] { null });

            java.lang.reflect.Method main = definedClass.getMethod("main", new Class[]{String[].class});
            main.invoke(null, new Object[]{new String[]{}});
            
        } catch (IOException f) {
            System.out.println("IOException: " + f);
            f.printStackTrace();
        } catch (Exception g) {
            System.out.println("Exception: " + g);
            g.printStackTrace();
        }
    }
}
