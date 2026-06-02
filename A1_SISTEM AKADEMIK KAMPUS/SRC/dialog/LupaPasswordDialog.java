package dialog;

import dao.PasswordDAO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 * LupaPasswordDialog — Dialog "Lupa Password" dua jalur:
 *
 * JALUR 1 — Verifikasi Email (Mahasiswa & Dosen):
 *   Pengguna memasukkan ID (NIM/NIDN) + email terdaftar.
 *   Jika cocok, dipersilakan memasukkan password baru.
 *
 * JALUR 2 — Hubungi Admin:
 *   Menampilkan informasi kontak admin dan tombol "Salin Email Admin"
 *   agar pengguna bisa menghubungi admin secara langsung.
 *
 * Cara pakai (dari LoginFrame):
 *   new LupaPasswordDialog(loginFrame, cbRole.getSelectedIndex()).setVisible(true);
 *   // 0 = Mahasiswa, 1 = Dosen, 2 = Admin
 */
public class LupaPasswordDialog extends JDialog {

    // ── Warna ────────────────────────────────────────────────────────
    private static final Color GREEN_PRIMARY  = new Color(13, 122, 78);
    private static final Color WHITE          = Color.WHITE;
    private static final Color BG             = new Color(245, 247, 245);
    private static final Color BORDER_COLOR   = new Color(220, 228, 224);
    private static final Color TEXT_PRIMARY   = new Color(30, 40, 35);
    private static final Color TEXT_SECONDARY = new Color(90, 110, 100);
    private static final Color BLUE           = new Color(41, 128, 185);
    private static final Color RED            = new Color(192, 57, 43);
    private static final Color ORANGE         = new Color(200, 100, 20);

    // Email kontak admin — sesuaikan dengan data nyata
    private static final String ADMIN_EMAIL   = "admin.siakad@kampus.ac.id";
    private static final String ADMIN_WA      = "0812-3456-7890";

    private final int roleIndex; // 0=Mahasiswa, 1=Dosen, 2=Admin

    public LupaPasswordDialog(JFrame parent, int roleIndex) {
        super(parent, "Lupa Password", true);
        this.roleIndex = roleIndex;
        setSize(500, 560);
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

        JLabel lblJudul = new JLabel("🔑  Lupa Password");
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lblJudul.setForeground(WHITE);
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        String sub = roleIndex == 0 ? "Mahasiswa" : roleIndex == 1 ? "Dosen" : "Admin";
        JLabel lblRole = new JLabel("Akun: " + sub);
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRole.setForeground(new Color(200, 240, 220));
        lblRole.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblJudul);
        header.add(Box.createVerticalStrut(4));
        header.add(lblRole);
        add(header, BorderLayout.NORTH);

        // ── Body: Tab Verifikasi Email vs Hubungi Admin ───────────────
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabs.setBackground(WHITE);

        if (roleIndex < 2) {
            // Mahasiswa & Dosen: ada jalur verifikasi email
            tabs.addTab("  Verifikasi Email  ", buatPanelEmail());
        }
        tabs.addTab("  Hubungi Admin  ", buatPanelHubungiAdmin());

        add(tabs, BorderLayout.CENTER);

        // ── Footer ───────────────────────────────────────────────────
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        footer.setBackground(BG);
        JButton btnTutup = makeBtn("Tutup", new Color(130, 130, 130));
        btnTutup.addActionListener(e -> dispose());
        footer.add(btnTutup);
        add(footer, BorderLayout.SOUTH);
    }

    // ─────────────────────────────────────────────────────────────────
    //  PANEL VERIFIKASI EMAIL
    // ─────────────────────────────────────────────────────────────────
    private JPanel buatPanelEmail() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(WHITE);
        panel.setBorder(new EmptyBorder(20, 36, 16, 36));
        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;

        // Info teks
        String idLabel = roleIndex == 0 ? "NIM Mahasiswa:" : "NIDN Dosen:";
        String idContoh = roleIndex == 0 ? "Contoh: 2022001001" : "Contoh: 0012345678";

        g.gridy = 0; g.insets = new Insets(0, 0, 8, 0);
        JLabel info = new JLabel("<html><i>Masukkan " + idLabel.replace(":", "") +
                " dan email yang terdaftar.<br>Jika cocok, Anda dapat mengatur password baru.</i></html>");
        info.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        info.setForeground(TEXT_SECONDARY);
        panel.add(info, g);

        g.gridy = 1; g.insets = new Insets(12, 0, 4, 0);
        panel.add(makeLabel(idLabel), g);
        JTextField txtId = makeField(idContoh);
        g.gridy = 2; g.insets = new Insets(0, 0, 12, 0);
        panel.add(txtId, g);

        g.gridy = 3; g.insets = new Insets(0, 0, 4, 0);
        panel.add(makeLabel("Email Terdaftar:"), g);
        JTextField txtEmail = makeField("Contoh: nama@email.com");
        g.gridy = 4; g.insets = new Insets(0, 0, 14, 0);
        panel.add(txtEmail, g);

        // Tombol verifikasi
        JButton btnVerif = makeBtn("Verifikasi", BLUE);
        g.gridy = 5; g.insets = new Insets(0, 0, 10, 0);
        panel.add(btnVerif, g);

        JLabel lblStatus = new JLabel(" ");
        lblStatus.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblStatus.setForeground(RED);
        lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
        g.gridy = 6; g.insets = new Insets(0, 0, 0, 0);
        panel.add(lblStatus, g);

        // ── Panel reset (awalnya tersembunyi) ─────────────────────────
        JPanel panelReset = new JPanel(new GridBagLayout());
        panelReset.setBackground(new Color(240, 255, 245));
        panelReset.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 220, 200)),
                new EmptyBorder(12, 16, 12, 16)));
        panelReset.setVisible(false);
        GridBagConstraints gr = new GridBagConstraints();
        gr.fill = GridBagConstraints.HORIZONTAL;
        gr.weightx = 1;

        gr.gridy = 0; gr.insets = new Insets(0, 0, 4, 0);
        JLabel lblSukses = new JLabel("✅  Email terverifikasi! Masukkan password baru:");
        lblSukses.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSukses.setForeground(new Color(13, 100, 60));
        panelReset.add(lblSukses, gr);

        gr.gridy = 1; gr.insets = new Insets(8, 0, 4, 0);
        panelReset.add(makeLabel("Password Baru:"), gr);
        JPasswordField txtBaru = makePassField();
        gr.gridy = 2; gr.insets = new Insets(0, 0, 8, 0);
        panelReset.add(txtBaru, gr);

        gr.gridy = 3; gr.insets = new Insets(0, 0, 4, 0);
        panelReset.add(makeLabel("Konfirmasi Password Baru:"), gr);
        JPasswordField txtKonfirm = makePassField();
        gr.gridy = 4; gr.insets = new Insets(0, 0, 10, 0);
        panelReset.add(txtKonfirm, gr);

        JButton btnReset = makeBtn("Reset Password", GREEN_PRIMARY);
        gr.gridy = 5; gr.insets = new Insets(0, 0, 0, 0);
        panelReset.add(btnReset, gr);

        g.gridy = 7; g.insets = new Insets(10, 0, 0, 0);
        panel.add(panelReset, g);

        // ── Verifikasi logic ─────────────────────────────────────────
        btnVerif.addActionListener(e -> {
            String id    = txtId.getText().trim();
            String email = txtEmail.getText().trim();
            if (id.isEmpty() || email.isEmpty()) {
                lblStatus.setForeground(RED);
                lblStatus.setText("ID dan email wajib diisi!");
                panelReset.setVisible(false);
                return;
            }
            PasswordDAO dao = new PasswordDAO();
            String emailDb = roleIndex == 0
                    ? dao.getEmailMahasiswa(id)
                    : dao.getEmailDosen(id);

            if (emailDb != null && emailDb.equalsIgnoreCase(email)) {
                lblStatus.setForeground(new Color(13, 100, 60));
                lblStatus.setText("Email cocok! Silakan atur password baru.");
                panelReset.setVisible(true);
                pack();
            } else {
                lblStatus.setForeground(RED);
                lblStatus.setText("ID atau email tidak cocok dengan data kami.");
                panelReset.setVisible(false);
            }
        });

        // ── Reset logic ───────────────────────────────────────────────
        btnReset.addActionListener(e -> {
            String id      = txtId.getText().trim();
            String email   = txtEmail.getText().trim();
            String baru    = new String(txtBaru.getPassword()).trim();
            String konfirm = new String(txtKonfirm.getPassword()).trim();

            if (baru.isEmpty() || konfirm.isEmpty()) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password baru tidak boleh kosong!");
                return;
            }
            if (!baru.equals(konfirm)) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password baru dan konfirmasi tidak sama!");
                return;
            }
            // Validasi kekuatan password
            if (baru.length() < 8) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password minimal 8 karakter.");
                return;
            }
            if (!baru.matches(".*[a-zA-Z].*")) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password harus mengandung minimal 1 huruf.");
                return;
            }
            if (!baru.matches(".*[0-9].*")) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password harus mengandung minimal 1 angka.");
                return;
            }
            if (!baru.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
                lblStatus.setForeground(RED);
                lblStatus.setText("Password harus mengandung minimal 1 karakter spesial (!@#$%...).");
                return;
            }

            boolean ok = roleIndex == 0
                    ? new PasswordDAO().resetPasswordMahasiswaByEmail(id, email, baru)
                    : new PasswordDAO().resetPasswordDosenByEmail(id, email, baru);

            if (ok) {
                JOptionPane.showMessageDialog(this,
                        "Password berhasil direset!\nSilakan login dengan password baru.",
                        "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } else {
                lblStatus.setForeground(RED);
                lblStatus.setText("Gagal reset password. Coba lagi atau hubungi admin.");
            }
        });

        return panel;
    }

    // ─────────────────────────────────────────────────────────────────
    //  PANEL HUBUNGI ADMIN
    // ─────────────────────────────────────────────────────────────────
    private JPanel buatPanelHubungiAdmin() {
        JPanel panel = new JPanel();
        panel.setBackground(WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(new EmptyBorder(24, 36, 24, 36));

        // Ikon & judul
        JLabel lblIkon = new JLabel("📧", SwingConstants.CENTER);
        lblIkon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        lblIkon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblJudul = new JLabel("Hubungi Administrator", SwingConstants.CENTER);
        lblJudul.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblJudul.setForeground(TEXT_PRIMARY);
        lblJudul.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDesc = new JLabel(
                "<html><div style='text-align:center; width:340px'>" +
                "Jika Anda tidak dapat mereset password secara mandiri, " +
                "hubungi admin SIAKAD untuk meminta reset password.<br><br>" +
                "Sertakan: <b>Nama lengkap</b>, <b>NIM/NIDN</b>, " +
                "dan <b>bukti identitas</b> (foto KTM/KTP)." +
                "</div></html>");
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDesc.setForeground(TEXT_SECONDARY);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Kartu kontak
        JPanel kartu = new JPanel(new GridBagLayout());
        kartu.setBackground(new Color(240, 250, 245));
        kartu.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 215, 195), 1),
                new EmptyBorder(14, 20, 14, 20)));
        kartu.setMaximumSize(new Dimension(400, 120));
        kartu.setAlignmentX(Component.CENTER_ALIGNMENT);

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        g.insets = new Insets(4, 0, 4, 0);

        g.gridy = 0;
        JLabel lblKontakEmail = new JLabel("📧  Email Admin: " + ADMIN_EMAIL);
        lblKontakEmail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKontakEmail.setForeground(BLUE);
        kartu.add(lblKontakEmail, g);

        g.gridy = 1;
        JLabel lblKontakWa = new JLabel("📱  WhatsApp: " + ADMIN_WA);
        lblKontakWa.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblKontakWa.setForeground(new Color(37, 150, 90));
        kartu.add(lblKontakWa, g);

        g.gridy = 2;
        JLabel lblJam = new JLabel("🕐  Jam Layanan: Senin–Jumat, 08.00–16.00 WIB");
        lblJam.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblJam.setForeground(TEXT_SECONDARY);
        kartu.add(lblJam, g);

        // Tombol salin email
        JButton btnSalin = makeBtn("Salin Email Admin", BLUE);
        btnSalin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalin.addActionListener(e -> {
            java.awt.datatransfer.StringSelection sel =
                    new java.awt.datatransfer.StringSelection(ADMIN_EMAIL);
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            JOptionPane.showMessageDialog(this,
                    "Email admin \"" + ADMIN_EMAIL + "\" telah disalin ke clipboard.",
                    "Disalin", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton btnSalinWa = makeBtn("Salin No. WhatsApp", new Color(37, 150, 90));
        btnSalinWa.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSalinWa.addActionListener(e -> {
            java.awt.datatransfer.StringSelection sel =
                    new java.awt.datatransfer.StringSelection(ADMIN_WA.replaceAll("[^0-9]", ""));
            java.awt.Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
            JOptionPane.showMessageDialog(this,
                    "Nomor WhatsApp admin telah disalin ke clipboard.",
                    "Disalin", JOptionPane.INFORMATION_MESSAGE);
        });

        // Catatan
        JLabel lblCatatan = new JLabel(
                "<html><i style='color:#888'>Catatan: Admin akan memverifikasi identitas Anda sebelum mereset password.</i></html>");
        lblCatatan.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblCatatan.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(lblIkon);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblJudul);
        panel.add(Box.createVerticalStrut(10));
        panel.add(lblDesc);
        panel.add(Box.createVerticalStrut(16));
        panel.add(kartu);
        panel.add(Box.createVerticalStrut(14));
        panel.add(btnSalin);
        panel.add(Box.createVerticalStrut(8));
        panel.add(btnSalinWa);
        panel.add(Box.createVerticalStrut(12));
        panel.add(lblCatatan);

        return panel;
    }

    // ── Helpers ──────────────────────────────────────────────────────

    private JLabel makeLabel(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(TEXT_PRIMARY);
        return l;
    }

    private JTextField makeField(String placeholder) {
        JTextField f = new JTextField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(360, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        // Placeholder teks redup
        f.setForeground(Color.GRAY);
        f.setText(placeholder);
        f.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(TEXT_PRIMARY); }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (f.getText().isEmpty()) { f.setText(placeholder); f.setForeground(Color.GRAY); }
            }
        });
        return f;
    }

    private JPasswordField makePassField() {
        JPasswordField f = new JPasswordField();
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setPreferredSize(new Dimension(360, 36));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return f;
    }

    private JButton makeBtn(String teks, Color bg) {
        JButton btn = new JButton(teks) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
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