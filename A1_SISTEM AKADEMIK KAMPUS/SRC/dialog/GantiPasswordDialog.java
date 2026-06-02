package dialog;

import dao.PasswordDAO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * GantiPasswordDialog — Dialog modal ganti password.
 *
 * Cara pakai:
 *   new GantiPasswordDialog(parentFrame, "MAHASISWA", mhs.getNim()).setVisible(true);
 *   new GantiPasswordDialog(parentFrame, "DOSEN",     dosen.getNidn()).setVisible(true);
 *   new GantiPasswordDialog(parentFrame, "ADMIN",     username).setVisible(true);
 *
 * Validasi password mahasiswa: min 8 karakter, harus ada huruf + angka + karakter spesial.
 */
public class GantiPasswordDialog extends JDialog {

    private static final Color GREEN_PRIMARY  = new Color(13, 122, 78);
    private static final Color WHITE          = Color.WHITE;
    private static final Color BG             = new Color(245, 247, 245);
    private static final Color BORDER_C       = new Color(220, 228, 224);
    private static final Color TEXT_PRIMARY   = new Color(30, 40, 35);
    private static final Color TEXT_SECONDARY = new Color(90, 110, 100);
    private static final Color RED            = new Color(192, 57, 43);
    private static final Color GREEN_OK       = new Color(13, 100, 60);

    private final String role;
    private final String userId;

    public GantiPasswordDialog(JFrame parent, String role, String userId) {
        super(parent, "Ganti Password", true);
        this.role   = role.toUpperCase();
        this.userId = userId;
        setSize(440, 460);
        setLocationRelativeTo(parent);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        // ── Header ───────────────────────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(GREEN_PRIMARY);
        header.setBorder(new EmptyBorder(18, 24, 18, 24));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JLabel lblJudul = new JLabel("🔒  Ganti Password");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudul.setForeground(WHITE);
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("Buat password baru yang kuat dan mudah diingat");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(200, 240, 220));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblJudul);
        header.add(Box.createVerticalStrut(4));
        header.add(lblSub);
        add(header, BorderLayout.NORTH);

        // ── Form ─────────────────────────────────────────────────────
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(WHITE);
        form.setBorder(new EmptyBorder(22, 36, 16, 36));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        // Info ID
        g.gridy = 0; g.insets = new Insets(0, 0, 16, 0);
        JLabel lblId = new JLabel(idLabel() + ": " + userId);
        lblId.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblId.setForeground(TEXT_SECONDARY);
        form.add(lblId, g);

        // Password Lama
        g.gridy = 1; g.insets = new Insets(0, 0, 4, 0);
        form.add(makeLabel("Password Lama:"), g);
        JPasswordField txtLama = makePassField();
        g.gridy = 2; g.insets = new Insets(0, 0, 14, 0);
        form.add(txtLama, g);

        // Password Baru
        g.gridy = 3; g.insets = new Insets(0, 0, 4, 0);
        form.add(makeLabel("Password Baru:"), g);
        JPasswordField txtBaru = makePassField();
        g.gridy = 4; g.insets = new Insets(0, 0, 4, 0);
        form.add(txtBaru, g);

        // Indikator kekuatan password (hanya untuk mahasiswa)
        JLabel lblKekuatan = new JLabel(" ");
        lblKekuatan.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g.gridy = 5; g.insets = new Insets(0, 0, 10, 0);
        form.add(lblKekuatan, g);

        // Konfirmasi
        g.gridy = 6; g.insets = new Insets(0, 0, 4, 0);
        form.add(makeLabel("Konfirmasi Password Baru:"), g);
        JPasswordField txtKonfirm = makePassField();
        g.gridy = 7; g.insets = new Insets(0, 0, 20, 0);
        form.add(txtKonfirm, g);

        // Status / error
        JLabel lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(RED);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 8; g.insets = new Insets(0, 0, 0, 0);
        form.add(lblStatus, g);

        add(form, BorderLayout.CENTER);

        // ── Hint syarat password (untuk mahasiswa) ───────────────────
        if (role.equals("MAHASISWA")) {
            JLabel lblHint = new JLabel(
                "<html><i style='color:#888'>Min. 8 karakter, kombinasi huruf, angka, dan karakter spesial (!@#$%...)</i></html>");
            lblHint.setFont(new Font("Segoe UI", Font.PLAIN, 10));

            // Masukkan hint ke dalam form (gridy=9), bukan ke BorderLayout.NORTH
            g.gridy = 9; g.insets = new Insets(0, 0, 0, 0);
            form.add(lblHint, g);

            // Update indikator saat ketik
            txtBaru.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                void update() {
                    String p = new String(txtBaru.getPassword());
                    int skor = hitungSkorPassword(p);
                    if (p.isEmpty()) {
                        lblKekuatan.setText(" ");
                    } else if (skor < 2) {
                        lblKekuatan.setForeground(RED);
                        lblKekuatan.setText("● Lemah — tambahkan angka dan karakter spesial");
                    } else if (skor < 4) {
                        lblKekuatan.setForeground(new Color(200, 120, 0));
                        lblKekuatan.setText("●● Sedang — sudah lumayan, bisa lebih kuat");
                    } else {
                        lblKekuatan.setForeground(GREEN_OK);
                        lblKekuatan.setText("●●● Kuat ✓");
                    }
                }
                public void insertUpdate(javax.swing.event.DocumentEvent e)  { update(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e)  { update(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
            });
        }

        // ── Tombol ───────────────────────────────────────────────────
        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
        botPanel.setBackground(BG);
        JButton btnSimpan = makeBtn("Simpan Password", GREEN_PRIMARY);
        JButton btnBatal  = makeBtn("Batal", new Color(140, 140, 140));
        botPanel.add(btnSimpan);
        botPanel.add(btnBatal);
        add(botPanel, BorderLayout.SOUTH);

        btnBatal.addActionListener(e -> dispose());

        btnSimpan.addActionListener(e -> {
            String lama    = new String(txtLama.getPassword()).trim();
            String baru    = new String(txtBaru.getPassword()).trim();
            String konfirm = new String(txtKonfirm.getPassword()).trim();

            if (lama.isEmpty() || baru.isEmpty() || konfirm.isEmpty()) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Semua field wajib diisi!");
                return;
            }
            if (!baru.equals(konfirm)) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password baru dan konfirmasi tidak sama!");
                return;
            }

            // Validasi kekuatan hanya untuk mahasiswa
            if (role.equals("MAHASISWA")) {
                String errPass = validasiPasswordMahasiswa(baru);
                if (errPass != null) {
                    lblStatus.setForeground(RED);
                    lblStatus.setText(errPass);
                    return;
                }
            } else {
                if (baru.length() < 6) {
                    lblStatus.setForeground(RED);
                    lblStatus.setText("Password minimal 6 karakter.");
                    return;
                }
            }

            PasswordDAO dao = new PasswordDAO();
            boolean ok = false;
            switch (role) {
                case "MAHASISWA": ok = dao.gantiPasswordMahasiswa(userId, lama, baru); break;
                case "DOSEN":     ok = dao.gantiPasswordDosen(userId, lama, baru);     break;
                case "ADMIN":     ok = dao.gantiPasswordAdmin(userId, lama, baru);     break;
            }

            if (ok) {
                JOptionPane.showMessageDialog(this,
                    "Password berhasil diubah!\nSilakan login kembali dengan password baru.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password lama salah atau terjadi kesalahan.");
            }
        });
    }

    // ── Validasi password mahasiswa ───────────────────────────────────
    private String validasiPasswordMahasiswa(String p) {
        if (p.length() < 8)                          return "Minimal 8 karakter.";
        if (!p.matches(".*[a-zA-Z].*"))              return "Harus ada minimal 1 huruf.";
        if (!p.matches(".*[0-9].*"))                 return "Harus ada minimal 1 angka.";
        if (!p.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*"))
                                                     return "Harus ada minimal 1 karakter spesial (!@#$%...).";
        return null; // valid
    }

    /** Skor 0–5: setiap kriteria terpenuhi +1 */
    private int hitungSkorPassword(String p) {
        int s = 0;
        if (p.length() >= 8)                          s++;
        if (p.matches(".*[a-z].*"))                   s++;
        if (p.matches(".*[A-Z].*"))                   s++;
        if (p.matches(".*[0-9].*"))                   s++;
        if (p.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) s++;
        return s;
    }

    // ── Helpers ──────────────────────────────────────────────────────
    private String idLabel() {
        switch (role) {
            case "MAHASISWA": return "NIM";
            case "DOSEN":     return "NIDN";
            default:          return "Username";
        }
    }

    private JLabel makeLabel(String t) {
        JLabel l = new JLabel(t);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    private JPasswordField makePassField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(340, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return f;
    }

    private JButton makeBtn(String teks, Color bg) {
        JButton btn = new JButton(teks) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false); btn.setContentAreaFilled(false); btn.setBorderPainted(false);
        btn.setBackground(bg); btn.setForeground(WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 22, 8, 22));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }
}