package modelo.dao;

import java.sql.*;
import java.util.*;
import modelo.entidades.*;

public class ProdutoDAO {
	public static void insere(Produto p) throws Exception {
		String sql = "insert into produto(descricao, preco, validade)" 
				+ " values(?, ?, ?)";
		try (		
			Connection con = ConnectionFactory.getConnection();
			PreparedStatement pst = con.prepareStatement(sql);
		) {
			pst.setString(1, p.getDescricao());
			pst.setDouble(2, p.getPreco());
			pst.setDate(3, new java.sql.Date(p.getValidade().getTime()));
			pst.executeUpdate();
		}
	}
	public static List<Produto> todos() throws Exception {
		String sql = "Select * from Produto order by Descricao";
		try (		
			Connection con = ConnectionFactory.getConnection();
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(sql);
		) {
			List<Produto> produtos = new ArrayList<>();
			while (rs.next()) {
				Produto p = new Produto();
				p.setId(rs.getInt("idProduto"));
				p.setDescricao(rs.getString("Descricao"));
				p.setPreco(rs.getDouble("Preco"));
				p.setValidade(rs.getDate("Validade"));
				produtos.add(p);
			}
			return produtos;
		}
	}
	public static Produto buscaPorId(int id) throws Exception {
		String sql = "Select * from Produto where idProduto = ?";
		try (
			Connection con = ConnectionFactory.getConnection();
			PreparedStatement pst = con.prepareStatement(sql);
		){
			pst.setInt(1, id);
			try ( 
				ResultSet rs = pst.executeQuery();
			){
				Produto p = null;
				if (rs.next()) {
					p = new Produto();
					p.setId(rs.getInt("idProduto"));
					p.setDescricao(rs.getString("Descricao"));
					p.setPreco(rs.getDouble("Preco"));
					p.setValidade(rs.getDate("Validade"));
				}
				return p;
			}
		}
	}
	public static boolean altera(Produto p) throws Exception {
		String sql = "update produto set descricao = ?, preco = ?, " 
				+ "validade = ? where idProduto = ?";
		try (
			Connection con = ConnectionFactory.getConnection();
			PreparedStatement pst = con.prepareStatement(sql);
		){
			pst.setString(1, p.getDescricao());
			pst.setDouble(2, p.getPreco());
			pst.setDate(3, new java.sql.Date(p.getValidade().getTime()));
			pst.setInt(4, p.getId());
			int resp = pst.executeUpdate();
			if (resp == 1) {
				return true;
			}
			return false;
		}
	}
	public static boolean exclui(int id) throws Exception {
		String sql = "delete from produto where idProduto = ?";
		try (
			Connection con = ConnectionFactory.getConnection();
			PreparedStatement pst = con.prepareStatement(sql);
		){
			pst.setInt(1, id);
			int resp = pst.executeUpdate();
			if (resp == 1) {
				return true;
			}
			return false;
		}
	}
}

