
import javax.swing.*;
import java.awt.*;
import java.security.*;
import javax.crypto.Cipher;
import java.util.Base64;

public class rsaCrypt extends JFrame {

    JTextArea input, encrypted, decrypted;
    PublicKey publicKey;
    PrivateKey privateKey;

    Color bg = new Color(15, 23, 42);
    Color card = new Color(30, 41, 59);
    Color blue = new Color(59, 130, 246);
    Color green = new Color(34, 197, 94);

    rsaCrypt() {

        try {
            KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
            gen.initialize(2048);

            KeyPair pair = gen.generateKeyPair();
            publicKey = pair.getPublic();
            privateKey = pair.getPrivate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("RSA Secure Encryption");
        setSize(850, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(bg);

        // Heading
        JLabel title = new JLabel("RSA SECURE VAULT", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
            "Asymmetric Data Encryption System", JLabel.CENTER);
        subtitle.setForeground(Color.LIGHT_GRAY);
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
        header.setBackground(bg);
        header.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        header.add(title);
        header.add(subtitle);

        add(header, BorderLayout.NORTH);

        // Main panel
        JPanel main = new JPanel(new GridLayout(3, 1, 12, 12));
        main.setBackground(bg);
        main.setBorder(BorderFactory.createEmptyBorder(10, 25, 15, 25));

        input = new JTextArea();
        encrypted = new JTextArea();
        decrypted = new JTextArea();

        main.add(createSection("01  |  ENTER ORIGINAL MESSAGE", input));
        main.add(createSection("02  |  ENCRYPTED CIPHERTEXT", encrypted));
        main.add(createSection("03  |  DECRYPTED MESSAGE", decrypted));

        add(main, BorderLayout.CENTER);

        // Buttons
        JButton encryptBtn = createButton("ENCRYPT", blue);
        JButton decryptBtn = createButton("DECRYPT", green);
        JButton clearBtn = createButton("CLEAR", new Color(220, 38, 38));

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttons.setBackground(bg);

        buttons.add(encryptBtn);
        buttons.add(decryptBtn);
        buttons.add(clearBtn);

        encryptBtn.addActionListener(e -> encrypt());
        decryptBtn.addActionListener(e -> decrypt());
        clearBtn.addActionListener(e -> clear());

        add(buttons, BorderLayout.SOUTH);

        setVisible(true);
    }

    JPanel createSection(String heading, JTextArea area) {

        JPanel panel = new JPanel(new BorderLayout(5, 8));
        panel.setBackground(card);
        panel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        JLabel label = new JLabel(heading);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(147, 197, 253));

        area.setFont(new Font("Consolas", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBackground(new Color(15, 23, 42));
        area.setForeground(Color.WHITE);
        area.setCaretColor(Color.WHITE);

        panel.add(label, BorderLayout.NORTH);
        panel.add(new JScrollPane(area), BorderLayout.CENTER);

        return panel;
    }

    JButton createButton(String text, Color color) {

        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(160, 42));

        return button;
    }

    void encrypt() {

        try {
            String message = input.getText();

            if (message.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please enter a message!");
                return;
            }

            Cipher cipher = Cipher.getInstance(
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] result = cipher.doFinal(message.getBytes("UTF-8"));

            encrypted.setText(
                Base64.getEncoder().encodeToString(result));

            JOptionPane.showMessageDialog(this,
                "Encryption Successful!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Encryption Error: " + e.getMessage());
        }
    }

    void decrypt() {

        try {
            String message = encrypted.getText().trim();

            if (message.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Please encrypt a message first!");
                return;
            }

            byte[] data = Base64.getDecoder().decode(message);

            Cipher cipher = Cipher.getInstance(
                "RSA/ECB/OAEPWithSHA-256AndMGF1Padding");

            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] result = cipher.doFinal(data);

            decrypted.setText(new String(result, "UTF-8"));

            JOptionPane.showMessageDialog(this,
                "Decryption Successful!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Invalid encrypted message!");
        }
    }

    void clear() {
        input.setText("");
        encrypted.setText("");
        decrypted.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new rsaCrypt());
    }
}
