package com.systemnecs.dao;

import com.systemnecs.model.DetalleVenta;
import com.systemnecs.model.Venta;
import com.systemnecs.util.ConexionBD;
import com.systemnecs.util.Sesion;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class VentaDAO {

    private  ConexionBD conexionBD;

    public VentaDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }
    public int guardar(Venta v) throws SQLException {
        String sql = "INSERT INTO public.ventas(\n"
                + "	idcliente, idusuario, formadepago)\n"
                + "	VALUES ("+v.getCliente().getIdcliente()+" , "+ Sesion.getUsuario(null).getIdUsuario()+" , '"+v.getFormadepago()+"');";

        this.conexionBD.getConexion().setAutoCommit(false);

        PreparedStatement pst = this.conexionBD.getConexion().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        if (pst.executeUpdate() > 0) {
            ResultSet rs = pst.getGeneratedKeys();
            rs.next();
            int idventa = rs.getInt(1);
            sql = "INSERT INTO public.detalleventa( idventa, idproducto, precioventa, cantidad) VALUES\n";
            for (DetalleVenta dv : v.getDetalleventa()) {
                sql += "(";
                sql += " "+idventa+", "+dv.getProducto().getIdproducto()+", "+dv.getPrecioventa()+", "+dv.getCantidad()+" ";
                sql += "),";
            }
            sql = sql.substring(0, sql.length()-1);
            pst = this.conexionBD.getConexion().prepareStatement(sql);
            pst.executeUpdate();

            this.conexionBD.getConexion().commit();
            return idventa;
        }

        return 0;
    }
}
