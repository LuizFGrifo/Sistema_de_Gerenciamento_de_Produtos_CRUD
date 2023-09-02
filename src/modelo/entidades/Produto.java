package modelo.entidades;

import java.text.*;
import java.util.Date;

public class Produto {
	private int id;
	private String descricao;
	private double preco;
	private Date validade = new Date();
	
	public String getPrecoFormatado() {
		NumberFormat nf = NumberFormat.getCurrencyInstance();
		return nf.format(preco);
	}
	
	public String getValidadeFormatada() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		return sdf.format(validade);
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	
	public double getPreco() {
		return preco;
	}
	
	public void setPreco(double preco) {
		this.preco = preco;
	}
	
	public Date getValidade() {
		return validade;
	}
	
	public void setValidade(Date validade) {
		this.validade = validade;
	}
}

