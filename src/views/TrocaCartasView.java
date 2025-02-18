package views;

import database.DatabaseConnection;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TrocaCartasView extends JFrame {
    private static final int OFFSET_X = 0; // Ajuste horizontal para todos os elementos
    private static final int OFFSET_Y = 25; // Ajuste vertical para todos os elementos

    private JComboBox<String> comboUsuario1, comboUsuario2, comboCarta1, comboCarta2;
    private JButton btnTrocar;

    public TrocaCartasView() {
        setTitle("Troca de Cartas");
        setSize(450, 350);
        setResizable(false); // Impede redimensionamento
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(null); // Permite posicionamento absoluto

        // Definir o fundo com a imagem
        JLabel background = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("trocarcartas.png")));
        background.setBounds(0, 0, 450, 350);
        setContentPane(background);
        background.setLayout(null);

        // Criando labels e campos de entrada
        adicionarLabel("Usuário 1:", 40 + OFFSET_X, 40 + OFFSET_Y);
        comboUsuario1 = new JComboBox<>();
        carregarUsuarios(comboUsuario1);
        configurarCampo(comboUsuario1, 160 + OFFSET_X, 40 + OFFSET_Y, 230, 30);

        adicionarLabel("Carta de Usuário 1:", 40 + OFFSET_X, 80 + OFFSET_Y);
        comboCarta1 = new JComboBox<>();
        configurarCampo(comboCarta1, 160 + OFFSET_X, 80 + OFFSET_Y, 230, 30);

        adicionarLabel("Usuário 2:", 40 + OFFSET_X, 120 + OFFSET_Y);
        comboUsuario2 = new JComboBox<>();
        carregarUsuarios(comboUsuario2);
        configurarCampo(comboUsuario2, 160 + OFFSET_X, 120 + OFFSET_Y, 230, 30);

        adicionarLabel("Carta de Usuário 2:", 40 + OFFSET_X, 160 + OFFSET_Y);
        comboCarta2 = new JComboBox<>();
        configurarCampo(comboCarta2, 160 + OFFSET_X, 160 + OFFSET_Y, 230, 30);

        // Criando botão de troca
        btnTrocar = new JButton("Confirmar Troca");
        estilizarBotao(btnTrocar);
        btnTrocar.setBounds(140 + OFFSET_X, 220 + OFFSET_Y, 160, 40);
        btnTrocar.addActionListener(e -> realizarTroca());
        background.add(btnTrocar);

        // Listeners para carregar cartas dos usuários selecionados
        comboUsuario1.addActionListener(e -> carregarCartasUsuario(comboUsuario1, comboCarta1));
        comboUsuario2.addActionListener(e -> carregarCartasUsuario(comboUsuario2, comboCarta2));

        setVisible(true);
    }

    private void adicionarLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 120, 20);
        getContentPane().add(label);
    }

    private void configurarCampo(JComponent campo, int x, int y, int largura, int altura) {
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        campo.setBackground(new Color(30, 30, 30, 180)); // Fundo escuro com transparência
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        campo.setBounds(x, y, largura, altura);
        getContentPane().add(campo);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBackground(new Color(75, 0, 130)); // Roxo escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2));
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void carregarUsuarios(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
        String sql = "SELECT id_usuario, nome FROM magic.usuarios";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                comboBox.addItem(id + " - " + nome);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void carregarCartasUsuario(JComboBox<String> comboUsuario, JComboBox<String> comboCarta) {
        comboCarta.removeAllItems();
        String selecionado = (String) comboUsuario.getSelectedItem();
        if (selecionado == null) return;

        int idUsuario = Integer.parseInt(selecionado.split(" - ")[0]);
        String sql = "SELECT id_carta, nome FROM magic.cartas WHERE id_usuario = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idUsuario);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int idCarta = rs.getInt("id_carta");
                String nome = rs.getString("nome");
                comboCarta.addItem(idCarta + " - " + nome);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar cartas do usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void realizarTroca() {
        String selecionadoUsuario1 = (String) comboUsuario1.getSelectedItem();
        String selecionadoUsuario2 = (String) comboUsuario2.getSelectedItem();
        String selecionadoCarta1 = (String) comboCarta1.getSelectedItem();
        String selecionadoCarta2 = (String) comboCarta2.getSelectedItem();

        if (selecionadoUsuario1 == null || selecionadoUsuario2 == null || selecionadoCarta1 == null || selecionadoCarta2 == null) {
            JOptionPane.showMessageDialog(this, "Selecione todos os campos para realizar a troca!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuario1 = Integer.parseInt(selecionadoUsuario1.split(" - ")[0]);
        int idUsuario2 = Integer.parseInt(selecionadoUsuario2.split(" - ")[0]);
        int idCarta1 = Integer.parseInt(selecionadoCarta1.split(" - ")[0]);
        int idCarta2 = Integer.parseInt(selecionadoCarta2.split(" - ")[0]);

        if (idUsuario1 == idUsuario2) {
            JOptionPane.showMessageDialog(this, "Um usuário não pode trocar cartas consigo mesmo!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sqlTroca = "UPDATE magic.cartas SET id_usuario = CASE " +
                "WHEN id_carta = ? THEN ? " +
                "WHEN id_carta = ? THEN ? " +
                "ELSE id_usuario END " +
                "WHERE id_carta IN (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sqlTroca)) {
            stmt.setInt(1, idCarta1);
            stmt.setInt(2, idUsuario2);
            stmt.setInt(3, idCarta2);
            stmt.setInt(4, idUsuario1);
            stmt.setInt(5, idCarta1);
            stmt.setInt(6, idCarta2);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Troca realizada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar a troca!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
