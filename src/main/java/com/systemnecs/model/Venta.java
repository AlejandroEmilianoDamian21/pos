package com.systemnecs.model;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Venta {

    private final IntegerProperty idventa = new SimpleIntegerProperty();
    private Cliente cliente;
    private Usuario usuario;
    private final ObjectProperty<LocalDateTime> fecharegistro = new SimpleObjectProperty<>();
    private final IntegerProperty numerofactura = new SimpleIntegerProperty();
    private final StringProperty formadepago = new SimpleStringProperty();

    public int getIdventa() {
        return idventa.get();
    }

    public IntegerProperty idventaProperty() {
        return idventa;
    }

    public void setIdventa(int idventa) {
        this.idventa.set(idventa);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public LocalDateTime getFecharegistro() {
        return fecharegistro.get();
    }

    public ObjectProperty<LocalDateTime> fecharegistroProperty() {
        return fecharegistro;
    }

    public void setFecharegistro(LocalDateTime fecharegistro) {
        this.fecharegistro.set(fecharegistro);
    }

    public int getNumerofactura() {
        return numerofactura.get();
    }

    public IntegerProperty numerofacturaProperty() {
        return numerofactura;
    }

    public void setNumerofactura(int numerofactura) {
        this.numerofactura.set(numerofactura);
    }

    public String getFormadepago() {
        return formadepago.get();
    }

    public StringProperty formadepagoProperty() {
        return formadepago;
    }

    public void setFormadepago(String formadepago) {
        this.formadepago.set(formadepago);
    }
}
