package views;

import database.DatabaseConnection;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CadastroCartaView extends JFrame {
    private JComboBox<String> comboUsuarios;
    private JTextField txtNome, txtTipo, txtCusto, txtPoder, txtResistencia;
    private JButton btnSalvar;

    public CadastroCartaView() {
        setTitle("Cadastro de Carta");
        setSize(460, 540);
        setResizable(false); // Impede redimensionamento
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(null); // Permite posicionamento absoluto

        // Definir o fundo com a imagem
        JLabel background = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("cadastrocartas.png")));
        background.setBounds(0, 0, 470, 560);
        setContentPane(background);
        background.setLayout(null);

        int deslocamentoY = 50; // Ajuste de posição vertical
        int deslocamentoX = 20; // Ajuste de posição horizontal

        // Labels e campos de entrada deslocados
        adicionarLabel("Usuário:", 50 + deslocamentoX, 60 + deslocamentoY);
        comboUsuarios = new JComboBox<>();
        carregarUsuarios();
        configurarCampo(comboUsuarios, 50 + deslocamentoX, 80 + deslocamentoY, 320, 30);

        adicionarLabel("Nome da Carta:", 50 + deslocamentoX, 110 + deslocamentoY);
        txtNome = new JTextField();
        configurarCampo(txtNome, 50 + deslocamentoX, 130 + deslocamentoY, 320, 30);

        adicionarLabel("Tipo:", 50 + deslocamentoX, 160 + deslocamentoY);
        txtTipo = new JTextField();
        configurarCampo(txtTipo, 50 + deslocamentoX, 180 + deslocamentoY, 320, 30);

        adicionarLabel("Custo:", 50 + deslocamentoX, 210 + deslocamentoY);
        txtCusto = new JTextField();
        configurarCampo(txtCusto, 50 + deslocamentoX, 230 + deslocamentoY, 320, 30);

        adicionarLabel("Poder:", 50 + deslocamentoX, 260 + deslocamentoY);
        txtPoder = new JTextField();
        configurarCampo(txtPoder, 50 + deslocamentoX, 280 + deslocamentoY, 320, 30);

        adicionarLabel("Resistência:", 50 + deslocamentoX, 310 + deslocamentoY);
        txtResistencia = new JTextField();
        configurarCampo(txtResistencia, 50 + deslocamentoX, 330 + deslocamentoY, 320, 30);

        btnSalvar = new JButton("Salvar Carta");
        estilizarBotao(btnSalvar);
        btnSalvar.setBounds(140 + deslocamentoX, 380 + deslocamentoY, 140, 40);
        btnSalvar.addActionListener(e -> salvarCarta());
        background.add(btnSalvar);

        setVisible(true);
    }

    private void adicionarLabel(String texto, int x, int y) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 12)); // Tamanho ajustado para 12
        label.setForeground(Color.WHITE);
        label.setBounds(x, y, 120, 20);
        getContentPane().add(label);
    }

    private void configurarCampo(JComponent campo, int x, int y, int largura, int altura) {
        campo.setFont(new Font("Arial", Font.PLAIN, 12)); // Fonte reduzida para 12
        campo.setBackground(new Color(30, 30, 30, 180)); // Fundo escuro com transparência
        campo.setForeground(Color.WHITE);
        campo.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
        campo.setBounds(x, y, largura, altura);
        getContentPane().add(campo);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 12)); // Fonte ajustada para 12
        botao.setBackground(new Color(75, 0, 130)); // Roxo escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
    }

    private void carregarUsuarios() {
        String sql = "SELECT id_usuario, nome, email FROM magic.usuarios";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("id_usuario");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                comboUsuarios.addItem(id + " - " + nome + " (" + email + ")");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao carregar usuários!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void salvarCarta() {
        String selecionado = (String) comboUsuarios.getSelectedItem();
        if (selecionado == null || txtNome.getText().trim().isEmpty() || txtTipo.getText().trim().isEmpty() || txtCusto.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos obrigatórios!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idUsuario = Integer.parseInt(selecionado.split(" - ")[0]);
        String nome = txtNome.getText().trim();
        String tipo = txtTipo.getText().trim();
        int custo, poder = 0, resistencia = 0;

        try {
            custo = Integer.parseInt(txtCusto.getText().trim());
            if (!txtPoder.getText().trim().isEmpty()) {
                poder = Integer.parseInt(txtPoder.getText().trim());
            }
            if (!txtResistencia.getText().trim().isEmpty()) {
                resistencia = Integer.parseInt(txtResistencia.getText().trim());
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Custo, Poder e Resistência devem ser números inteiros!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "INSERT INTO magic.cartas (nome, tipo, custo, id_usuario) VALUES (?, ?, ?, ?) RETURNING id_carta";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setString(2, tipo);
            stmt.setInt(3, custo);
            stmt.setInt(4, idUsuario);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idCarta = rs.getInt("id_carta");

                // Se for uma Criatura, adiciona poder e resistência
                if (poder > 0 && resistencia > 0) {
                    String sqlCriatura = "INSERT INTO magic.carta_criatura (id_carta, poder, resistencia) VALUES (?, ?, ?)";

                    try (PreparedStatement stmtCriatura = conn.prepareStatement(sqlCriatura)) {
                        stmtCriatura.setInt(1, idCarta);
                        stmtCriatura.setInt(2, poder);
                        stmtCriatura.setInt(3, resistencia);
                        stmtCriatura.executeUpdate();
                    }
                }
            }

            JOptionPane.showMessageDialog(this, "Carta cadastrada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar carta!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Aplica o tema escuro mágico
        } catch (Exception ex) {
            System.err.println("Erro ao aplicar o tema FlatLaf.");
        }

        SwingUtilities.invokeLater(CadastroCartaView::new);
    }
}
