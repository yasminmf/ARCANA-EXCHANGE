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

public class ListaCartasView extends JFrame {
    private JTable tabelaCartas;
    private DefaultTableModel modeloTabela;
    private JButton btnCarregar;

    public ListaCartasView() {
        setTitle("Lista de Cartas");
        setSize(600, 400);
        setMinimumSize(new Dimension(500, 350)); // Define um tamanho mínimo para a tela
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.BLACK); // Fundo preto

        // Criando painel de título
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.BLACK);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Lista de Cartas", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Serif", Font.BOLD, 22));
        lblTitle.setForeground(new Color(212, 175, 55)); // Dourado

        titlePanel.add(lblTitle, BorderLayout.CENTER);

        // Criando modelo para a tabela
        modeloTabela = new DefaultTableModel(new String[]{"ID", "Nome", "Tipo", "Custo", "Poder", "Resistência", "Dono"}, 0);
        tabelaCartas = new JTable(modeloTabela);
        tabelaCartas.setFont(new Font("Arial", Font.PLAIN, 14));
        tabelaCartas.setBackground(new Color(30, 30, 30)); // Fundo escuro
        tabelaCartas.setForeground(Color.WHITE);
        tabelaCartas.setGridColor(new Color(212, 175, 55)); // Borda dourada
        tabelaCartas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tabelaCartas.getTableHeader().setForeground(Color.WHITE);
        tabelaCartas.getTableHeader().setBackground(new Color(50, 50, 50));

        JScrollPane scrollPane = new JScrollPane(tabelaCartas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));

        // Criando botão de carregar cartas
        btnCarregar = new JButton("Carregar Cartas");
        estilizarBotao(btnCarregar);
        btnCarregar.addActionListener(e -> carregarCartas());

        // Painel para o botão
        JPanel panelBotao = new JPanel();
        panelBotao.setBackground(Color.BLACK);
        panelBotao.add(btnCarregar);

        // Adicionando componentes ao layout
        add(titlePanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotao, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 14));
        botao.setBackground(new Color(75, 0, 130)); // Roxo escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
    }

    private void carregarCartas() {
        modeloTabela.setRowCount(0); // Limpa a tabela antes de adicionar novos dados

        String sql = "SELECT c.id_carta, c.nome, c.tipo, c.custo, cc.poder, cc.resistencia, " +
                "u.nome AS usuario_nome " +
                "FROM magic.cartas c " +
                "LEFT JOIN magic.carta_criatura cc ON c.id_carta = cc.id_carta " +
                "LEFT JOIN magic.usuarios u ON c.id_usuario = u.id_usuario " +
                "ORDER BY LOWER(c.nome)"; // Ordena as cartas pelo nome, ignorando maiúsculas e minúsculas

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id_carta");
                String nome = rs.getString("nome");
                String tipo = rs.getString("tipo");
                int custo = rs.getInt("custo");
                Integer poder = rs.getObject("poder", Integer.class);
                Integer resistencia = rs.getObject("resistencia", Integer.class);
                String usuarioNome = rs.getString("usuario_nome");

                // Se não for criatura, exibir "-" nos atributos de poder e resistência
                String poderStr = (poder != null) ? String.valueOf(poder) : "-";
                String resistenciaStr = (resistencia != null) ? String.valueOf(resistencia) : "-";
                String donoStr = (usuarioNome != null) ? usuarioNome : "Não associado";

                modeloTabela.addRow(new Object[]{id, nome, tipo, custo, poderStr, resistenciaStr, donoStr});
            }

            if (modeloTabela.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Nenhuma carta encontrada.", "Informação", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao buscar cartas!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Tema escuro mágico
        } catch (Exception ex) {
            System.err.println("Erro ao aplicar o tema FlatLaf.");
        }

        SwingUtilities.invokeLater(ListaCartasView::new);
    }
}
