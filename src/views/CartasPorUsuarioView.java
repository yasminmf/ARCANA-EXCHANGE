package views;

import database.DatabaseConnection;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CartasPorUsuarioView extends JFrame {
    private static final int OFFSET_X = 0; // Ajuste horizontal
    private static final int OFFSET_Y = 70; // Ajuste vertical

    private JComboBox<String> comboUsuarios;
    private JTable tabelaCartas;
    private DefaultTableModel modeloTabela;
    private JButton btnBuscar;

    public CartasPorUsuarioView() {
        setTitle("Cartas por Usuário");
        setSize(600, 380);
        setResizable(false); // Impede redimensionamento
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(null); // Permite posicionamento absoluto

        // Definir o fundo com a imagem
        JLabel background = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("cartasporusuario.png")));
        background.setBounds(0, 0, 600, 380);
        setContentPane(background);
        background.setLayout(null);

        // Criando painel superior para seleção de usuário
        JLabel lblUsuario = new JLabel("Usuário:");
        lblUsuario.setFont(new Font("Arial", Font.BOLD, 14));
        lblUsuario.setForeground(Color.WHITE);
        lblUsuario.setBounds(45 + OFFSET_X, 25 + OFFSET_Y, 80, 25);
        background.add(lblUsuario);

        comboUsuarios = new JComboBox<>();
        carregarUsuarios();
        comboUsuarios.setFont(new Font("Arial", Font.PLAIN, 14));
        comboUsuarios.setBackground(new Color(30, 30, 30, 180)); // Fundo escuro com transparência
        comboUsuarios.setForeground(Color.WHITE);
        comboUsuarios.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        comboUsuarios.setBounds(115 + OFFSET_X, 25 + OFFSET_Y, 250, 30);
        background.add(comboUsuarios);

        btnBuscar = new JButton("Buscar Cartas");
        estilizarBotao(btnBuscar);
        btnBuscar.setBounds(395 + OFFSET_X, 25 + OFFSET_Y, 140, 30);
        btnBuscar.addActionListener(e -> carregarCartasDoUsuario());
        background.add(btnBuscar);

        // Criando modelo para a tabela
        modeloTabela = new DefaultTableModel(new String[]{"Nome", "Tipo", "Custo", "Poder", "Resistência"}, 0);
        tabelaCartas = new JTable(modeloTabela);
        tabelaCartas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaCartas.setBackground(new Color(30, 30, 30)); // Fundo escuro sólido
        tabelaCartas.setForeground(Color.WHITE);
        tabelaCartas.setGridColor(new Color(212, 175, 55));
        tabelaCartas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabelaCartas.getTableHeader().setForeground(Color.WHITE);
        tabelaCartas.getTableHeader().setBackground(new Color(50, 50, 50));

        JScrollPane scrollPane = new JScrollPane(tabelaCartas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        scrollPane.setBounds(0 + OFFSET_X, 75 + OFFSET_Y, 584, 197);
        background.add(scrollPane);

        setVisible(true);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(new Color(75, 0, 130)); // Roxo escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
    }

    private void carregarUsuarios() {
        String sql = "SELECT id_usuario, nome FROM magic.usuarios";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                comboUsuarios.addItem(id + " - " + nome);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCartasDoUsuario() {
        modeloTabela.setRowCount(0); // Limpa a tabela antes de adicionar novos dados
        String selecionado = (String) comboUsuarios.getSelectedItem();
        if (selecionado == null) {
            JOptionPane.showMessageDialog(this, "Selecione um usuário!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuario = Integer.parseInt(selecionado.split(" - ")[0]);

        String sql = "SELECT c.nome, c.tipo, c.custo, cc.poder, cc.resistencia " +
                "FROM magic.cartas c " +
                "LEFT JOIN magic.carta_criatura cc ON c.id_carta = cc.id_carta " +
                "WHERE c.id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo");
                int custo = rs.getInt("custo");

                // Pegando poder e resistência, se for NULL retorna "-"
                Integer poder = rs.getObject("poder", Integer.class);
                Integer resistencia = rs.getObject("resistencia", Integer.class);

                String poderStr = (poder != null) ? String.valueOf(poder) : "-";
                String resistenciaStr = (resistencia != null) ? String.valueOf(resistencia) : "-";

                modeloTabela.addRow(new Object[]{nome, tipo, custo, poderStr, resistenciaStr});
            }

            if (modeloTabela.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Nenhuma carta encontrada para este usuário.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar cartas do usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Aplica o tema escuro mágico
        } catch (Exception ex) {
            System.err.println("Erro ao aplicar o tema FlatLaf.");
        }

        SwingUtilities.invokeLater(CartasPorUsuarioView::new);
    }
}
