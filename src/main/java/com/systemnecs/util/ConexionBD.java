package com.systemnecs.util;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {

    private Connection conexion;
    private Statement statement;

    //Tiki-PC
    //private static String IP = "localhost", PUERTO ="5432", BD = "ventas" , USER = "postgres" , PASS = "admin";

    //Digicom-PC
    private static final String IP = "localhost";
    private static final String PUERTO ="5432";
    private static final String BD = "VENTAS";
    private static final String USER = "postgres";
    private static final String PASS = "admin";


    public ConexionBD(){

    }
    public void conectar(){
        try {
            Class.forName("org.postgresql.Driver");
            conexion = DriverManager.getConnection("jdbc:postgresql://" + IP + ":" + PUERTO + "/"+ BD , USER, PASS);
            statement = conexion.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        }catch (ClassNotFoundException | SQLException ex){
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultSet CONSULTAR(String sql) throws  SQLException{
        ResultSet rs;
        rs = statement.executeQuery(sql);
        return rs;
    }

    public boolean GUARDAR(String sql) throws  SQLException{
        return (statement.executeUpdate(sql) > 0);
    }

    public void CERRAR(){
        try {
            if (conexion != null){
                conexion.close();
            }
            if (statement != null){
                statement.close();
            }
            System.out.println("Conexion Cerrada");
        }catch (SQLException ex){
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Connection getConexion() {
        return conexion;
    }
}
