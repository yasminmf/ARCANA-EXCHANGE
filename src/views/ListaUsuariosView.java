package views;

import database.DatabaseConnection;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ListaUsuariosView extends JFrame {
    private static final int OFFSET_X = 0; // Ajuste horizontal para todos os elementos
    private static final int OFFSET_Y = 15; // Ajuste vertical para todos os elementos

    private JTable tabelaUsuarios;
    private DefaultTableModel modeloTabela;
    private JButton btnCarregar, btnAtualizar;

    public ListaUsuariosView() {
        setTitle("Lista de Usuários");
        setSize(450, 350);
        setResizable(false); // Impede redimensionamento
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(null); // Permite posicionamento absoluto

        // Definir o fundo com a imagem
        JLabel background = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("listadeusuarios.png")));
        background.setBounds(0, 0, 450, 350);
        setContentPane(background);
        background.setLayout(null);

        // Criando modelo para a tabela
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "E-mail"}, 0);
        tabelaUsuarios = new JTable(modeloTabela);
        tabelaUsuarios.setFont(new Font("Arial", Font.PLAIN, 12));
        tabelaUsuarios.setBackground(new Color(30, 30, 30, 150)); // Fundo escuro com transparência
        tabelaUsuarios.setForeground(Color.WHITE);
        tabelaUsuarios.setGridColor(new Color(212, 175, 55)); // Borda dourada
        tabelaUsuarios.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tabelaUsuarios.getTableHeader().setForeground(Color.WHITE);
        tabelaUsuarios.getTableHeader().setBackground(new Color(50, 50, 50));
        tabelaUsuarios.setRowHeight(25); // Aumenta a altura das linhas para melhor legibilidade

        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        scrollPane.setBounds(0 + OFFSET_X, 30 + OFFSET_Y, 433, 200);
        background.add(scrollPane);

        // Criando botões
        btnCarregar = new JButton("Carregar Usuários");
        btnAtualizar = new JButton("Atualizar Lista");
        estilizarBotao(btnCarregar);
        estilizarBotao(btnAtualizar);

        btnCarregar.setBounds(50 + OFFSET_X, 250 + OFFSET_Y, 150, 40);
        btnAtualizar.setBounds(230 + OFFSET_X, 250 + OFFSET_Y, 150, 40);

        btnCarregar.addActionListener(e -> carregarUsuarios());
        btnAtualizar.addActionListener(e -> carregarUsuarios()); // Atualiza a lista sem precisar reabrir a janela

        background.add(btnCarregar);
        background.add(btnAtualizar);

        setVisible(true);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBackground(new Color(75, 0, 130)); // Roxo escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
    }

    private void carregarUsuarios() {
        modeloTabela.setRowCount(0); // Limpa a tabela antes de adicionar novos dados
        String sql = "SELECT id_usuario, nome, email FROM magic.usuarios";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (!rs.isBeforeFirst()) { // Se não houver resultados, exibe uma mensagem
                modeloTabela.addRow(new Object[]{"-", "Nenhum usuário encontrado", "-"});
                return;
            }

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                String email = rs.getString("email");

                modeloTabela.addRow(new Object[]{id, nome, email});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar usuários!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Tema escuro mágico
        } catch (Exception ex) {
            System.err.println("Erro ao aplicar o tema FlatLaf.");
        }

        SwingUtilities.invokeLater(ListaUsuariosView::new);
    }
}
