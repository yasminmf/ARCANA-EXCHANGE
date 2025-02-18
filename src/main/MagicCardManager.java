package main;

import com.formdev.flatlaf.FlatDarkLaf;
import javax.swing.*;
import java.awt.*;
import views.*;

public class MagicCardManager extends JFrame {
    private JComboBox<String> comboOpcoes;
    private JButton btnOk;

    public MagicCardManager() {
        setTitle("Arcana Exchange - Loja de Trocas Mágicas");
        setSize(950, 540); // Ajustado para um tamanho melhor
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centraliza a janela
        setLayout(null); // Permite posicionamento absoluto
        setResizable(false); // Impede redimensionamento

        // Definir o fundo com a imagem
        JLabel background = new JLabel(new ImageIcon(getClass().getClassLoader().getResource("arcana.png")));
        background.setBounds(0, 0, 950, 540);
        setContentPane(background);
        background.setLayout(null);

        // Criando ComboBox para selecionar a ação
        String[] opcoes = {
                "GERENCIAR USUÁRIOS",
                "CADASTRAR CARTAS",
                "TROCAR CARTAS",
                "LISTAR CARTAS",
                "LISTAR USUÁRIOS",
                "CARTAS POR USUÁRIO"
        };

        comboOpcoes = new JComboBox<>(opcoes);
        comboOpcoes.setFont(new Font("Arial", Font.BOLD, 12));
        comboOpcoes.setBackground(new Color(50, 30, 15, 100)); // Tom escuro da borda com 30% de transparência
        comboOpcoes.setForeground(Color.WHITE); // Texto branco
        comboOpcoes.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
        comboOpcoes.setBounds(360, 340, 220, 40); // Posicionamento dentro do papiro

        // Aplicando o renderizador personalizado
        comboOpcoes.setRenderer(new CustomComboBoxRenderer());

        background.add(comboOpcoes);

        // Criando botão OK
        btnOk = new JButton("OK");
        estilizarBotao(btnOk);
        btnOk.setBounds(437, 400, 60, 40); // Posicionamento dentro do papiro
        btnOk.addActionListener(e -> executarAcao());
        background.add(btnOk);
    }

    private void estilizarBotao(JButton botao) {
        botao.setFont(new Font("Arial", Font.BOLD, 30));
        botao.setBackground(new Color(75, 0, 130)); // Roxo escuro
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setBorder(BorderFactory.createLineBorder(new Color(212, 175, 55), 2)); // Borda dourada
    }

    private void executarAcao() {
        String opcaoSelecionada = (String) comboOpcoes.getSelectedItem();

        if (opcaoSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione uma opção!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        switch (opcaoSelecionada) {
            case "GERENCIAR USUÁRIOS":
                new GerenciamentoUsuariosView().setVisible(true);
                break;
            case "CADASTRAR CARTAS":
                new CadastroCartaView().setVisible(true);
                break;
            case "TROCAR CARTAS":
                new TrocaCartasView().setVisible(true);
                break;
            case "LISTAR CARTAS":
                new BuscarCartasView().setVisible(true);
                break;
            case "LISTAR USUÁRIOS":
                new ListaUsuariosView().setVisible(true);
                break;
            case "CARTAS POR USUÁRIO":
                new CartasPorUsuarioView().setVisible(true);
                break;
            default:
                JOptionPane.showMessageDialog(this, "Opção inválida!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf()); // Aplica o tema escuro
        } catch (Exception ex) {
            System.err.println("Erro ao aplicar o tema FlatLaf.");
        }

        SwingUtilities.invokeLater(() -> new MagicCardManager().setVisible(true));
    }

    // Classe para renderizar o JComboBox e destacar opções em roxo
    static class CustomComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setHorizontalAlignment(SwingConstants.CENTER); // Centraliza o texto
            label.setFont(new Font("Arial", Font.BOLD, 12));

            if (isSelected) {
                label.setBackground(new Color(75, 0, 130)); // Fundo roxo escuro ao passar o mouse
                label.setForeground(Color.WHITE); // Texto branco
            } else {
                label.setBackground(new Color(50, 30, 15, 100)); // Fundo escuro padrão
                label.setForeground(Color.WHITE);
            }

            return label;
        }
    }
}
