/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.satyen.server;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Hashtable;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

/**
 * 
 * @author Satyen S Shimpi
 */
public class ServerSocket extends ClassLoader implements Opcodes {

    static java.net.ServerSocket socket1;

    /**
     *
     */
    protected final static int port = 19999;
    static Socket connection;
    static boolean first;
    static StringBuffer process;
    static String TimeStamp;
    static String returnCode;

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            socket1 = new java.net.ServerSocket(port);
            System.out.println("SingleSocketServer Initialized");
            
//            java.lang.reflect.Method main = createClass().getMethod("main", new Class[]{String[].class});
//            main.invoke(null, new Object[]{new String[]{}});
//            if (true) return;

            String className = "Example";
            while (true) {                
                //create class byte code array
                byte[] code = createClass(className);
                        
                //create a socket
                connection = socket1.accept();
                System.out.println("connected to client");
                BufferedInputStream is = new BufferedInputStream(connection.getInputStream());
                InputStreamReader isr = new InputStreamReader(is);

                ObjectOutputStream os = new ObjectOutputStream(connection.getOutputStream());
//                BufferedOutputStream bs = new BufferedOutputStream(connection.getOutputStream());
                //send code to client
                //there might be limit of byte array size 65535
                //I think array size limit is Integer.MAX_VALUE = 3^31-1=2147483647=2GB in file size. So class file cant be 2GB :)
                Hashtable<String, byte[]> hash= new Hashtable();
                
                //if we want to send newly created class then 'true' else for existing class 'false';
                if(false){
                hash.put(className, code);
//                bs.write(code);
                os.writeObject(hash);
                os.flush();
                
//            Class<?> exampleClass = new ServerSocket().defineClass("Example", code, 0, code.length);
        
                System.out.println("new class sent to client");
//                TimeStamp = new java.util.Date().toString();
//                //returnCode = "SingleSocketServer repsonded - Process Sucessful " + (char) 1;	         
//                BufferedOutputStream bs = new BufferedOutputStream(connection.getOutputStream());
//                OutputStreamWriter osw = new OutputStreamWriter(bs, "US-ASCII");
//                osw.write(returnCode);
//                osw.flush();
                
            }else{
                Thread.sleep(1000);
                System.out.println("Sending existing class");
                //now send the existing class
                hash.clear();
                String clsNm = "dynamicClass.asm.Helloworld";
                hash.put(clsNm, getClassByteArray(clsNm));
                os.writeObject(hash);
                os.flush();
}
            }
        } catch (Exception e) {
        }
        try {
            connection.close();
            socket1.close();
        } catch (IOException e) {
        }
    }

    /**
     * Creates a new class with code given in his method
     * @param className
     * @return byte array containing compiled class
     */
    public static byte[] createClass(String className) {
        Class<?> exampleClass = null;
        // creates a ClassWriter for the Example public class,
        // which inherits from Object
        ClassWriter cw = new ClassWriter(0);
        byte[] code = cw.toByteArray();
        ServerSocket loader = new ServerSocket();

        // ------------------------------------------------------------------------
        // Same example with a GeneratorAdapter (more convenient but slower)
        // ------------------------------------------------------------------------
        System.out.println("Same example with a GeneratorAdapter (more convenient but slower)");

        cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_1, ACC_PUBLIC, className, null, "java/lang/Object", null);

        // creates a GeneratorAdapter for the (implicit) constructor
        Method m = Method.getMethod("void <init> ()");
        GeneratorAdapter mg = new GeneratorAdapter(ACC_PUBLIC, m, null, null, cw);
        mg.loadThis();
        mg.invokeConstructor(Type.getType(Object.class), m);
        mg.returnValue();
        mg.endMethod();

        // creates a GeneratorAdapter for the 'main' method
        m = Method.getMethod("void main (String[])");
        mg = new GeneratorAdapter(ACC_PUBLIC + ACC_STATIC, m, null, null, cw);
        mg.getStatic(Type.getType(System.class), "out",
                Type.getType(PrintStream.class));
        mg.push("Hello world!");
        mg.invokeVirtual(Type.getType(PrintStream.class),
                Method.getMethod("void println (String)"));
        mg.returnValue();
        mg.endMethod();

        cw.visitEnd();

        code = cw.toByteArray();
        return code;
    
    }
    
    /**
     * Gets the existing compiled class as byte array
     * @param className fully qualified class name
     * @return byte array containing compiled class
     * @throws IOException 
     */
    public static byte[] getClassByteArray(String className) throws IOException{        
        return new ClassReader(className).b;
    }
}
