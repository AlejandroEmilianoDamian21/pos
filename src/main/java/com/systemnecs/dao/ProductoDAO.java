package com.systemnecs.dao;

import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {

    private ConexionBD conexionBD;

    public  ProductoDAO(ConexionBD conexionBD){
        this.conexionBD = conexionBD;
    }

    public List<Producto> getProductos() throws  SQLException {

        List<Producto> productos = new ArrayList<>();

        Producto p = null;
        ResultSet rs = conexionBD.CONSULTAR("SELECT * FROM producto");
        while (rs.next()){
            p = new Producto();

            p.setIdproducto(rs.getInt("idproducto"));
            p.setNombreproducto(rs.getString("nombreproducto").trim());
            p.setCodigodebarras(rs.getString("codigodebarras").trim());
            p.setReferencia(rs.getString("referencia").trim());
            p.setStock(rs.getDouble("stock"));
            p.setStockminimo(rs.getDouble("stockminimo"));
            p.setDescripcion(rs.getString("descripcion").trim());
            p.setEstado(rs.getString("estado").trim());
            p.setPrecio(rs.getDouble("precio"));
            p.setFechavencimiento(rs.getDate("fechavencimiento").toLocalDate());

            productos.add(p);
        }
        return productos;
    }

    public int guardar(Producto pro) throws SQLException {
        String sql ="";
        if (pro.getIdproducto() == 0 ){
        sql = "INSERT INTO producto (";
        sql += " codigodebarras , referencia , nombreproducto , stock , stockminimo , descripcion , estado , precio , fecharegistro , imagen";
        sql += ") VALUES (";
        sql += " '"+pro.getCodigodebarras()+"' , '"+pro.getReferencia()+"' , '"+pro.getNombreproducto()+"' , '"+pro.getStock()+"' , ";
        sql += " '"+pro.getStockminimo()+"' , '"+pro.getDescripcion()+"' , '"+pro.getEstado()+"' , '"+pro.getPrecio()+"' , ";
        sql += " '"+pro.getFechavencimiento()+"' , ? ";
        sql += ")";
        } else {
            sql = "UPDATE producto SET \n"
                    + "	codigodebarras='"+pro.getCodigodebarras()+"', referencia='"+pro.getReferencia()+"', nombreproducto='"+pro.getNombreproducto()+"', \n"
                    + " stock=" + pro.getStock() + ", "+"stockminimo="+pro.getStockminimo()+", descripcion='"+pro.getDescripcion()+"', estado='"+pro.getEstado()+"', precio="+pro.getPrecio()+", \n"
                    + " fechadevencimiento='"+pro.getFechavencimiento()+"' , imagen=? WHERE idproducto="+pro.getIdproducto()+";";
        }
        PreparedStatement pst = conexionBD.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pst.setBytes(1, pro.getImagen());

        int insert = pst.executeUpdate();
        if(pro.getIdproducto() == 0) {
                ResultSet rs = pst.getGeneratedKeys();
                rs.next();
                insert = rs.getInt(1);
                rs.close();
        }
        return insert;
    }

}
