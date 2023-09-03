package app;

import java.awt.*;
import java.util.List;
import java.sql.Connection;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import modelo.dao.ConnectionFactory;
import modelo.dao.ProdutoDAO;
import modelo.entidades.Produto;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class projetoJava extends JFrame {
	// Definindo os componentes
	private JLabel lblDesc, lblPrice ,lblValidity;
	private JTextField txtDesc, txtPrice, txtValidity;
	private JPanel pn;
	private JButton btnInsert, btnView, btnDelete, btnUpdate;
	
	public projetoJava() {
		setTitle("CRUD Project"); // Define o titulo
		
		lblDesc = new JLabel("Descrição:");
		lblPrice = new JLabel("Preço:");
		lblValidity = new JLabel("Validade:");
		txtDesc = new JTextField();
		txtPrice = new JTextField();
		txtValidity = new JTextField();
		btnInsert = new JButton("Inserir");
		btnView = new JButton("Visualizar DB");
		btnDelete = new JButton("Deletar");
		btnUpdate = new JButton("Update");
		
		pn = new JPanel(new GridLayout(5, 2)); // Linha / Coluna
		
		// e -> { ... } cria uma implementação do método actionPerformed. 
		btnInsert.addActionListener(e -> {
			// Remove espaços em branco para não contar como preenchimento
			String descricao = txtDesc.getText().trim();
		    String precoText = txtPrice.getText().trim();
		    String validityText = txtValidity.getText().trim();

		    // Verificar se os campos obrigatórios estão preenchidos
		    if (descricao.isEmpty() || precoText.isEmpty() || validityText.isEmpty()) {
		        JOptionPane.showMessageDialog(null, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
		        return; // Sai da ação sem inserir os dados
		    }
		    
		    Produto p = new Produto();
		    p.setDescricao(txtDesc.getText());
		    p.setPreco(Double.parseDouble(txtPrice.getText()));

		    try {
		        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		        java.util.Date utilDate = sdf.parse(txtValidity.getText());
		        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		        p.setValidade(sqlDate);

		        try (Connection connection = ConnectionFactory.getConnection()) {
		            ProdutoDAO.insere(p);
		            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		        } catch (Exception ex) {
		            ex.printStackTrace();
		            JOptionPane.showMessageDialog(null, "Erro ao cadastrar produto!", "Erro", JOptionPane.ERROR_MESSAGE);
		        }
		    } catch (ParseException ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
		    }
		});
		
		btnView.addActionListener(e -> {
		    try {
		        List<Produto> produtos = ProdutoDAO.todos();

		        // Array bidimensional para os dados da tabela
		        Object[][] data = new Object[produtos.size()][4];
		        for (int i = 0; i < produtos.size(); i++) {
		            Produto p = produtos.get(i);
		            data[i][0] = p.getId();
		            data[i][1] = p.getDescricao();
		            data[i][2] = p.getPreco();
		            data[i][3] = p.getValidadeFormatada();
		        }

		        // Nomes das colunas
		        String[] colNames = {"ID", "Descrição", "Preço", "Validade"};

		        // Cria a tabela com os dados e nomes das colunas
		        JTable table = new JTable(data, colNames);

		        // Cria um JScrollPane para a tabela
		        JScrollPane scrollPane = new JScrollPane(table);

		        // Cria uma nova janela para exibir a tabela
		        JFrame frame = new JFrame("Dados do Banco de Dados");
		        frame.getContentPane().add(scrollPane);
		        frame.pack();
		        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		        frame.setVisible(true);
		        frame.setSize(500, 300);

		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }
		});
		
		btnUpdate.addActionListener(e -> {
		    int productId = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do produto a ser atualizado:"));

		    try {
		        Produto produtoAtual = ProdutoDAO.buscaPorId(productId);
		        if (produtoAtual != null) {
		            String descricao = JOptionPane.showInputDialog("Nova descrição:", produtoAtual.getDescricao());
		            double preco = Double.parseDouble(JOptionPane.showInputDialog("Novo preço:", produtoAtual.getPreco()));
		            String dataValidade = JOptionPane.showInputDialog("Nova data de validade (dd/MM/yyyy):", produtoAtual.getValidadeFormatada());

		            produtoAtual.setDescricao(descricao);
		            produtoAtual.setPreco(preco);

		            try {
		                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		                java.util.Date utilDate = sdf.parse(dataValidade);
		                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
		                produtoAtual.setValidade(sqlDate);
		            } catch (ParseException ex) {
		                ex.printStackTrace();
		                JOptionPane.showMessageDialog(null, "Formato de data inválido!", "Erro", JOptionPane.ERROR_MESSAGE);
		                return;
		            }

		            if (ProdutoDAO.altera(produtoAtual)) {
		                JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
		            } else {
		                JOptionPane.showMessageDialog(null, "Erro ao atualizar produto.", "Erro", JOptionPane.ERROR_MESSAGE);
		            }
		        } else {
		            JOptionPane.showMessageDialog(null, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
		        }
		    } catch (Exception ex) {
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(null, "Erro ao atualizar produto!", "Erro", JOptionPane.ERROR_MESSAGE);
		    }
		});
		
		btnDelete.addActionListener(e -> {
			try {
				int deleteID = Integer.parseInt(JOptionPane.showInputDialog("Digite o ID do produto para deletar:"));
				
				if(ProdutoDAO.exclui(deleteID)) {
					JOptionPane.showMessageDialog(null, "Produto excluído", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Produto não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});
		
		TitledBorder border = BorderFactory.createTitledBorder("Registro de produto");
		border.setTitleJustification(TitledBorder.CENTER); // Centraliza no centro
		border.setTitleColor(Color.BLUE); // Muda a cor do titulo
		
		// Criando uma nova fonte com um tamanho maior
		Font fontBorder = new Font(lblDesc.getFont().getName(), Font.PLAIN, 18);

		// Atribuindo a nova fonte ao título da borda
		border.setTitleFont(fontBorder);
		
		Font fontLbl = new Font(lblDesc.getFont().getName(), Font.PLAIN, 18);

		lblDesc.setFont(fontLbl);
		lblPrice.setFont(fontLbl);
		lblValidity.setFont(fontLbl);
		
		Font fontTxt = new Font(txtDesc.getFont().getName(), Font.PLAIN, 16);
		
		txtDesc.setFont(fontTxt);
		txtPrice.setFont(fontTxt);
		txtValidity.setFont(fontTxt);

		// Crie uma margem interna com espaço à esquerda e à direita de 5 pixels
		Insets innerMargin = new Insets(0, 6, 0, 6); // top / left/ bottom/ right

		// Atribua a margem interna ao JTextField
		txtDesc.setMargin(innerMargin);
		txtPrice.setMargin(innerMargin);
		txtValidity.setMargin(innerMargin);
		
		// Define a cor do background
		pn.setBackground(Color.GRAY);
		txtDesc.setBackground(Color.LIGHT_GRAY);
		txtPrice.setBackground(Color.LIGHT_GRAY);
		txtValidity.setBackground(Color.LIGHT_GRAY);
		// Define a cor do texto para branco
		lblDesc.setForeground(Color.WHITE);
		lblPrice.setForeground(Color.WHITE);
		lblValidity.setForeground(Color.WHITE);
		// Define um cursor diferente
		btnInsert.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnUpdate.setCursor(new Cursor(Cursor.HAND_CURSOR));
		
		pn.setBorder(border);
		pn.add(lblDesc); pn.add(txtDesc);
		pn.add(lblPrice); pn.add(txtPrice);
		pn.add(lblValidity); pn.add(txtValidity);
		pn.add(btnInsert); pn.add(btnView);
		pn.add(btnDelete); pn.add(btnUpdate);
		
		add(pn, BorderLayout.CENTER);
		
		
		setVisible(true); // Tornar visivel
		setResizable(false); // Não deixar redimensionavel a tela
		//pack(); // Define um limite para os itens na tela
		setSize(500, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE); // Para encerrar quando fechar
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new projetoJava();
			}
		});
	}

}
