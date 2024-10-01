package com.systemnecs.util;

import com.systemnecs.model.Usuario;

public class Sesion {

    private static Usuario usuario;

    public static Usuario getUsuario(Usuario user){
        if(usuario == null){
            usuario = user;
        }
        return usuario;
    }
}
