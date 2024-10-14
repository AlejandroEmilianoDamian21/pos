package com.systemnecs.dao;

import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;

import java.sql.*;
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

    public boolean delete(int idproducto ) throws SQLException {
        String sql = "DELETE FROM producto WHERE idproducto="+idproducto;
        return conexionBD.GUARDAR(sql);
    }

    public int guardar(Producto pro) throws SQLException {
        String sql ="";

        if (pro.getIdproducto() == 0 ){
            sql = "INSERT INTO producto (";
            sql += " codigodebarras , referencia , nombreproducto , stock , stockminimo , descripcion , estado , precio , fechavencimiento , imagen, fecharegistro";
            sql += ") VALUES (";
            sql += " '"+pro.getCodigodebarras()+"' , '"+pro.getReferencia()+"' , '"+pro.getNombreproducto()+"' , '"+pro.getStock()+"' , ";
            sql += " '"+pro.getStockminimo()+"' , '"+pro.getDescripcion()+"' , '"+pro.getEstado()+"' , '"+pro.getPrecio()+"' , ";
            sql += " '"+pro.getFechavencimiento()+"' , ? , NOW() ";
            sql += ")";
        } else {
            sql = "UPDATE producto SET \n"
                    + "	codigodebarras='"+pro.getCodigodebarras()+"', referencia='"+pro.getReferencia()+"', nombreproducto='"+pro.getNombreproducto()+"', \n"
                    + " stock=" + pro.getStock() + ", "+"stockminimo="+pro.getStockminimo()+", descripcion='"+pro.getDescripcion()+"', estado='"+pro.getEstado()+"', precio="+pro.getPrecio()+", \n"
                    + " fechavencimiento='"+pro.getFechavencimiento()+"' , imagen=? , fecharegistroactualizado= NOW() WHERE idproducto="+pro.getIdproducto()+";";
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

    public Producto getById(int idproducto) throws SQLException {
        Producto p = null;
        ResultSet rs = this.conexionBD.CONSULTAR("SELECT * FROM producto WHERE idproducto="+idproducto);
        if (rs.next()){
            p = new Producto();
            p.setIdproducto(idproducto);
            p.setCodigodebarras(rs.getString("codigodebarras").trim());
            p.setReferencia(rs.getString("referencia").trim());
            p.setNombreproducto(rs.getString("nombreproducto").trim());
            p.setStock(rs.getDouble("stock"));
            p.setStockminimo(rs.getDouble("stockminimo"));
            p.setDescripcion(rs.getString("descripcion").trim());
            p.setImagen(rs.getBytes("imagen"));
            p.setEstado(rs.getString("estado").trim());
            p.setPrecio(rs.getDouble("precio"));
            p.setFechavencimiento(rs.getDate("fechavencimiento").toLocalDate());
        }
        return p;
    }

    public List<Producto> getAll() throws SQLException{
        List<Producto> lista = new ArrayList<>();
        Producto p = null;
        ResultSet rs = this.conexionBD.CONSULTAR("SELECT idproducto, codigodebarras, referencia, nombreproducto, stock, stockminimo, descripcion, estado, fecharegistro, precio, fechavencimiento\n" +
                "	FROM public.producto;");
        while(rs.next()){
            p = new Producto();
            p.setIdproducto(rs.getInt("idproducto"));
            p.setCodigodebarras(rs.getString("codigodebarras").trim());
            p.setReferencia(rs.getString("referencia").trim());
            p.setNombreproducto(rs.getString("nombreproducto").trim());
            p.setStock(rs.getDouble("stock"));
            p.setStockminimo(rs.getDouble("stockminimo"));
            p.setDescripcion(rs.getString("descripcion").trim());
            p.setEstado(rs.getString("estado").trim());
            p.setPrecio(rs.getDouble("precio"));
            p.setFechavencimiento(rs.getDate("fechavencimiento").toLocalDate());

            lista.add(p);
        }
        return lista;
    }

}
