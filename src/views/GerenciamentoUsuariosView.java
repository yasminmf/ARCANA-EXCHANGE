package views;

import database.DatabaseConnection;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class GerenciamentoUsuariosView extends JFrame {
    private static final int OFFSET_X = -5; // Ajuste horizontal para todos os elementos
    private static final int OFFSET_Y = 15; // Ajuste vertical para todos os elementos

    private JTextField txtNome, txtEmail;
    private JButton btnAdicionar, btnRemover;

    public GerenciamentoUsuariosView() {
        setTitle("Gerenciar Usuários");
        setSize(400, 300);
        setResizable(false); // Impede redimensionamento
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(null); // Permite posicionamento absoluto

        // Definir o fundo com a imagem
        JLabel background = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("gerenciamentodeusuario.png")));
        background.setBounds(0, 0, 400, 300);
        setContentPane(background);
        background.setLayout(null);

        // Labels e campos de entrada
        adicionarLabel("Nome:", 30 + OFFSET_X, 50 + OFFSET_Y);
        txtNome = new JTextField();
        configurarCampo(txtNome, 120 + OFFSET_X, 50 + OFFSET_Y, 220, 30);

        adicionarLabel("E-mail:", 30 + OFFSET_X, 100 + OFFSET_Y);
        txtEmail = new JTextField();
        configurarCampo(txtEmail, 120 + OFFSET_X, 100 + OFFSET_Y, 220, 30);

        // Botões
        btnAdicionar = new JButton("Cadastrar");
        estilizarBotao(btnAdicionar);
        btnAdicionar.setBounds(50 + OFFSET_X, 170 + OFFSET_Y, 130, 40);
        btnAdicionar.addActionListener(e -> cadastrarUsuario(txtNome.getText().trim(), txtEmail.getText().trim()));
        background.add(btnAdicionar);

        btnRemover = new JButton("Remover");
        estilizarBotao(btnRemover);
        btnRemover.setBounds(220 + OFFSET_X, 170 + OFFSET_Y, 130, 40);
        btnRemover.addActionListener(e -> removerUsuario(txtNome.getText().trim(), txtEmail.getText().trim()));
        background.add(btnRemover);

        setVisible(true);
    }

    private void adicionarLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 80, 20);
        getContentPane().add(label);
    }

    private void configurarCampo(JComponent campo, int x, int y, int largura, int altura) {
        campo.setFont(new Font("Arial", Font.PLAIN, 12));
        campo.setBackground(new Color(30, 30, 30, 180)); // Fundo escuro com transparência
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
        campo.setBounds(x, y, largura, altura);
        getContentPane().add(campo);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBackground(new Color(75, 0, 130)); // Roxo escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
    }

    private void cadastrarUsuario(String nome, String email) {
        if (nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "INSERT INTO magic.usuarios (nome, email) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            txtNome.setText("");
            txtEmail.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void removerUsuario(String nome, String email) {
        if (nome.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "DELETE FROM magic.usuarios WHERE nome = ? AND email = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, email);
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Usuário removido com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                txtNome.setText("");
                txtEmail.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Usuário não encontrado!", "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao remover usuário!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Aplica o tema escuro mágico
        } catch (Exception ex) {
            System.err.println("Erro ao aplicar o tema FlatLaf.");
        }

        SwingUtilities.invokeLater(GerenciamentoUsuariosView::new);
    }
}
