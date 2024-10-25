package com.systemnecs.dao;

import com.systemnecs.model.Usuario;
import com.systemnecs.util.ConexionBD;
import com.systemnecs.util.Sesion;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    private final ConexionBD conexionBD;

    public UsuarioDAO(ConexionBD conexionBD) {
        this.conexionBD = conexionBD;
    }

    public Usuario getUsuario(String username, String password) throws SQLException {

        Usuario usuario = null;

        String sql = "SELECT * FROM usuario WHERE username='"+username+"' AND password='"+password+"'";
        ResultSet rs = conexionBD.CONSULTAR(sql);

        if(rs.next()){
            usuario = new Usuario();
            usuario.setIdUsuario(rs.getInt("idusuario"));
            usuario.setUsername(username);
            usuario.setPassword(password);
            usuario.setNombre(rs.getString("nombre"));

            Sesion.getUsuario(usuario);
        }

        return usuario;
    }
}
