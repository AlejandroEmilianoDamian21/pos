package com.systemnecs.dao;

import com.systemnecs.model.Producto;
import com.systemnecs.util.ConexionBD;

import java.sql.ResultSet;
import java.sql.SQLException;
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
}
