package main;

import dao.*;
import dialog.GantiPasswordDialog;
import dialog.LupaPasswordDialog;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import model.*;
import util.DatabaseConnection;
/**
 * MainApp — Sistem Informasi Akademik
 * Tampilan terinspirasi SATU USU (satu.usu.ac.id)
 * Warna utama: Hijau #0d7a4e, putih bersih, abu tipis
 */
public class MainApp {

    // ── Palet warna SATU USU ────────────────────────────────────────────────
    static final Color GREEN_PRIMARY = new Color(13, 122, 78); // hijau utama SATU
    static final Color GREEN_DARK = new Color(8, 90, 57); // hijau gelap (hover)
    static final Color GREEN_LIGHT = new Color(230, 247, 239); // hijau sangat muda
    static final Color GREEN_MEDIUM = new Color(166, 219, 196); // hijau sedang
    static final Color WHITE = Color.WHITE;
    static final Color BG = new Color(245, 247, 245); // background halaman
    static final Color SIDEBAR_BG = new Color(255, 255, 255); // sidebar putih
    static final Color SIDEBAR_ACTIVE = new Color(230, 247, 239); // item aktif
    static final Color BORDER = new Color(220, 228, 224); // border tipis
    static final Color TEXT_PRIMARY = new Color(30, 40, 35); // teks utama
    static final Color TEXT_SECONDARY = new Color(90, 110, 100); // teks sekunder
    static final Color TEXT_MUTED = new Color(140, 160, 150); // teks redup
    static final Color TOPBAR_TEXT = new Color(230, 255, 244); // teks di header
    static final Color BLUE = new Color(41, 128, 185);
    static final Color ORANGE = new Color(230, 126, 34);
    static final Color RED = new Color(192, 57, 43);
    static final Color GOLD = new Color(212, 172, 13);

    // ── Font helper ───────────────────────────────────────────────────────────
    static Font fBold(int s) {
        return new Font("Segoe UI", Font.BOLD, s);
    }

    static Font fPlain(int s) {
        return new Font("Segoe UI", Font.PLAIN, s);
    }

    static Font fItalic(int s) {
        return new Font("Segoe UI", Font.ITALIC, s);
    }

    // ═══════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {
        }

        Connection conn = DatabaseConnection.getConnection();
        if (conn == null) {
            JOptionPane.showMessageDialog(null,
                    "Gagal terhubung ke database!\n\nPeriksa:\n1. PostgreSQL sudah berjalan?\n"
                            + "2. Nama database, username, password sudah benar?\n"
                            + "3. postgresql-xx.jar ada di classpath?",
                    "Error Koneksi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PANEL UTILITIES
    // ═══════════════════════════════════════════════════════════════════════

    /** Tombol hijau modern ala SATU USU */
    static JButton buatTombol(String teks, Color bg) {
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
        btn.setFont(fBold(12));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
                btn.repaint();
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
                btn.repaint();
            }
        });
        return btn;
    }

    /** Tombol outline (border saja, background putih) */
    static JButton buatTombolOutline(String teks, Color color) {
        JButton btn = new JButton(teks) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.setColor(color);
                g2.setStroke(new BasicStroke(1.5f));
                g2.draw(new RoundRectangle2D.Float(0.75f, 0.75f, getWidth() - 1.5f, getHeight() - 1.5f, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBackground(WHITE);
        btn.setForeground(color);
        btn.setFont(fBold(12));
        btn.setBorder(BorderFactory.createEmptyBorder(7, 16, 7, 16));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 20));
                btn.repaint();
            }

            public void mouseExited(MouseEvent e) {
                btn.setBackground(WHITE);
                btn.repaint();
            }
        });
        return btn;
    }

    /** Tabel modern, header hijau SATU USU */
    static JTable buatTabel(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(fPlain(12));
        t.setRowHeight(32);
        t.setGridColor(BORDER);
        t.setShowGrid(true);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(GREEN_LIGHT);
        t.setSelectionForeground(TEXT_PRIMARY);
        t.setBackground(WHITE);
        t.setFillsViewportHeight(true);

        JTableHeader h = t.getTableHeader();
        h.setBackground(GREEN_PRIMARY);
        h.setForeground(WHITE);
        h.setFont(fBold(12));
        h.setPreferredSize(new Dimension(h.getWidth(), 38));
        h.setReorderingAllowed(false);
        h.setBorder(BorderFactory.createEmptyBorder());
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object val, boolean sel, boolean foc, int row, int col) {
                JLabel lbl = (JLabel) super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                lbl.setBackground(GREEN_PRIMARY);
                lbl.setForeground(WHITE);
                lbl.setFont(fBold(12));
                lbl.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 0, 1, GREEN_DARK),
                        BorderFactory.createEmptyBorder(0, 10, 0, 10)));
                lbl.setHorizontalAlignment(SwingConstants.LEFT);
                lbl.setOpaque(true);
                return lbl;
            }
        });

        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable tbl, Object val, boolean sel, boolean foc, int row, int col) {
                Component c = super.getTableCellRendererComponent(tbl, val, sel, foc, row, col);
                if (!sel)
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(245, 250, 247));
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                c.setFont(fPlain(12));
                return c;
            }
        });
        return t;
    }

    static JScrollPane scrollOf(Component c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(BorderFactory.createLineBorder(BORDER));
        sp.getViewport().setBackground(WHITE);
        return sp;
    }

    /** Kartu putih dengan judul bergaris hijau bawah */
    static JPanel buatKartu(String judul) {
        JPanel p = new JPanel(new BorderLayout(0, 10));
        p.setBackground(WHITE);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(16, 20, 16, 20)));
        if (judul != null && !judul.isEmpty()) {
            JLabel lbl = new JLabel(judul);
            lbl.setFont(fBold(14));
            lbl.setForeground(GREEN_PRIMARY);
            lbl.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, GREEN_PRIMARY));
            lbl.setPreferredSize(new Dimension(lbl.getPreferredSize().width, 32));
            p.add(lbl, BorderLayout.NORTH);
        }
        return p;
    }

    static JLabel buatLabelInfo(String teks) {
        JLabel l = new JLabel(teks);
        l.setFont(fItalic(11));
        l.setForeground(TEXT_MUTED);
        l.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        return l;
    }

    static void showInfo(String msg, boolean ok) {
        JOptionPane.showMessageDialog(null, msg,
                ok ? "Berhasil" : "Gagal",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    static void showInfo(String msg, boolean ok, Component parent) {
        JOptionPane.showMessageDialog(parent, msg,
                ok ? "Berhasil" : "Gagal",
                ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    static void showWarning(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // TOPBAR — hijau SATU USU ala screenshot
    // ═══════════════════════════════════════════════════════════════════════
    static JPanel buatTopbar(String namaPortal, String namaUser, String idUser, JFrame parentFrame) {
        JPanel bar = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(GREEN_PRIMARY);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bar.setPreferredSize(new Dimension(1, 58));
        bar.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

        // Kiri: logo + nama portal
        JPanel kiri = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        kiri.setOpaque(false);
        // Bulatan logo
        JPanel logoCircle = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 50));
                g2.fillOval(0, 0, 34, 34);
                g2.setColor(WHITE);
                g2.setFont(fBold(14));
                FontMetrics fm = g2.getFontMetrics();
                String s = "S";
                g2.drawString(s, (34 - fm.stringWidth(s)) / 2, (34 + fm.getAscent() - fm.getDescent()) / 2);
                g2.dispose();
            }
        };
        logoCircle.setOpaque(false);
        logoCircle.setPreferredSize(new Dimension(34, 34));
        JLabel lblPortal = new JLabel(namaPortal);
        lblPortal.setFont(fBold(16));
        lblPortal.setForeground(WHITE);
        kiri.add(logoCircle);
        kiri.add(lblPortal);
        bar.add(kiri, BorderLayout.WEST);

        // Tengah: info semester
        JPanel tengah = new JPanel(new GridLayout(2, 1, 0, 0));
        tengah.setOpaque(false);
        String semester = "Semester Genap T.A. 2025/2026";
        String tglHari = new java.text.SimpleDateFormat("EEEE, dd MMMM yyyy", new java.util.Locale("id", "ID"))
                .format(new java.util.Date());
        JLabel lblSmt = new JLabel(semester, SwingConstants.CENTER);
        lblSmt.setFont(fPlain(11));
        lblSmt.setForeground(TOPBAR_TEXT);
        JLabel lblTgl = new JLabel(tglHari, SwingConstants.CENTER);
        lblTgl.setFont(fBold(12));
        lblTgl.setForeground(WHITE);
        tengah.add(lblSmt);
        tengah.add(lblTgl);
        bar.add(tengah, BorderLayout.CENTER);

        // Kanan: kartu user + logout
        JPanel kanan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        kanan.setOpaque(false);

        // Kartu user berbentuk pill putih
        JPanel userCard = new JPanel(new GridLayout(2, 1, 0, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(WHITE);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        userCard.setOpaque(false);
        userCard.setBorder(BorderFactory.createEmptyBorder(4, 14, 4, 14));
        userCard.setPreferredSize(new Dimension(180, 44));
        String displayNama = namaUser.length() > 18 ? namaUser.substring(0, 16) + "..." : namaUser;
        JLabel lblNama = new JLabel(displayNama);
        lblNama.setFont(fBold(12));
        lblNama.setForeground(GREEN_PRIMARY);
        JLabel lblId = new JLabel(idUser);
        lblId.setFont(fPlain(11));
        lblId.setForeground(GREEN_DARK);
        userCard.add(lblNama);
        userCard.add(lblId);

        JButton btnLogout = new JButton("Logout") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnLogout.setOpaque(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setForeground(WHITE);
        btnLogout.setFont(fBold(12));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(6, 14, 6, 14));
        btnLogout.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLogout.addActionListener(e -> {
            parentFrame.dispose();
            new LoginFrame().setVisible(true);
        });
        final String[] detectedRole = { "ADMIN" };
        if (idUser.matches("\\d{10,}") && namaPortal.contains("Mahasiswa")) {
            detectedRole[0] = "MAHASISWA";
        } else if (idUser.matches("\\d{10}") && namaPortal.contains("Dosen")) {
            detectedRole[0] = "DOSEN";
        }
        // Simpan userId final untuk lambda
        final String finalUserId = idUser;
 
        JButton btnGantiPass = new JButton("🔒 Ganti Password") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fill(new java.awt.geom.RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btnGantiPass.setOpaque(false);
        btnGantiPass.setContentAreaFilled(false);
        btnGantiPass.setBorderPainted(false);
        btnGantiPass.setForeground(WHITE);
        btnGantiPass.setFont(fPlain(12));
        btnGantiPass.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        btnGantiPass.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnGantiPass.addActionListener(e ->
            new GantiPasswordDialog(parentFrame, detectedRole[0], finalUserId).setVisible(true)
        );
        kanan.add(btnGantiPass);
        kanan.add(userCard);
        kanan.add(btnLogout);
        bar.add(kanan, BorderLayout.EAST);

        return bar;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // SIDEBAR — putih bersih ala SATU USU
    // ═══════════════════════════════════════════════════════════════════════
    static JPanel buatSidebar(String[] menuLabels, String[] menuIcons,
            JButton[] btnArr, CardLayout cards, JPanel contentArea) {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(WHITE);
        sidebar.setPreferredSize(new Dimension(200, 600));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER));

        sidebar.add(Box.createVerticalStrut(8));
        for (int i = 0; i < menuLabels.length; i++) {
        String icon = (i < menuIcons.length) ? menuIcons[i] : "";
        JButton btn = buatTombolSidebar(menuLabels[i], icon);
        btnArr[i] = btn;

        final int idx = i;
        final String card = menuLabels[i];

        btn.addActionListener(e -> {
        setSidebarAktif(btnArr[idx], btnArr);
        cards.show(contentArea, card);
        });

        sidebar.add(btn);
    }
        sidebar.add(Box.createVerticalGlue());
        return sidebar;
    }

    static JButton buatTombolSidebar(String teks, String ikon) {
        JButton btn = new JButton((ikon != null && !ikon.isEmpty() ? ikon + "  " : "") + teks) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(6, 2, getWidth() - 12, getHeight() - 4, 8, 8));
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(fPlain(13));
        btn.setForeground(TEXT_PRIMARY);
        btn.setBackground(WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 10));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(SIDEBAR_ACTIVE)) {
                    btn.setBackground(new Color(240, 248, 244));
                    btn.repaint();
                }
            }

            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(SIDEBAR_ACTIVE)) {
                    btn.setBackground(WHITE);
                    btn.repaint();
                }
            }
        });
        return btn;
    }

    static void setSidebarAktif(JButton aktif, JButton[] semua) {
        for (JButton b : semua) {
            b.setBackground(WHITE);
            b.setFont(fPlain(13));
            b.setForeground(TEXT_PRIMARY);
        }
        aktif.setBackground(SIDEBAR_ACTIVE);
        aktif.setFont(fBold(13));
        aktif.setForeground(GREEN_PRIMARY);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // LOGIN FRAME
    // ═══════════════════════════════════════════════════════════════════════
    static class LoginFrame extends JFrame {
        public LoginFrame() {
            setTitle("Sistem Informasi Akademik — Login");
            setSize(460, 580);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            initUI();
        }

        private void initUI() {
            setLayout(new BorderLayout());
            getContentPane().setBackground(BG);

            // Banner atas hijau
            JPanel banner = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(GREEN_PRIMARY);
                    g2.fillRect(0, 0, getWidth(), getHeight());
                }
            };
            banner.setLayout(new BoxLayout(banner, BoxLayout.Y_AXIS));
            banner.setBorder(BorderFactory.createEmptyBorder(28, 24, 24, 24));
            banner.setPreferredSize(new Dimension(460, 120));

            JLabel lblSia = new JLabel("SISTEM INFORMASI AKADEMIK");
            lblSia.setFont(fBold(18));
            lblSia.setForeground(WHITE);
            lblSia.setAlignmentX(Component.CENTER_ALIGNMENT);

            JLabel lblSub = new JLabel("Kelompok 4 — Portal Akademik");
            lblSub.setFont(fPlain(12));
            lblSub.setForeground(TOPBAR_TEXT);
            lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Garis emas dekoratif
            JPanel garisGold = new JPanel();
            garisGold.setBackground(GOLD);
            garisGold.setMaximumSize(new Dimension(Integer.MAX_VALUE, 3));
            garisGold.setAlignmentX(Component.CENTER_ALIGNMENT);

            banner.add(lblSia);
            banner.add(Box.createVerticalStrut(6));
            banner.add(lblSub);
            banner.add(Box.createVerticalStrut(14));
            banner.add(garisGold);
            add(banner, BorderLayout.NORTH);

            // Form area
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(WHITE);
            form.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER),
                    BorderFactory.createEmptyBorder(28, 40, 24, 40)));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(6, 0, 6, 0);

            JLabel lblJudul = new JLabel("Masuk ke Portal");
            lblJudul.setFont(fBold(17));
            lblJudul.setForeground(TEXT_PRIMARY);
            gbc.gridy = 0;
            form.add(lblJudul, gbc);

            JLabel lblSub2 = new JLabel("Pilih peran dan masukkan kredensial Anda");
            lblSub2.setFont(fPlain(12));
            lblSub2.setForeground(TEXT_SECONDARY);
            gbc.gridy = 1;
            gbc.insets = new Insets(0, 0, 18, 0);
            form.add(lblSub2, gbc);

            gbc.insets = new Insets(6, 0, 4, 0);
            addLabel(form, "Login sebagai:", gbc, 2);
            JComboBox<String> cbRole = new JComboBox<>(new String[] { "Mahasiswa", "Dosen", "Admin" });
            styleCombo(cbRole);
            gbc.gridy = 3;
            gbc.insets = new Insets(0, 0, 10, 0);
            form.add(cbRole, gbc);

            JLabel lblUser = addLabel(form, "NIM Mahasiswa:", gbc, 4);
            JTextField txtUser = buatField();
            gbc.gridy = 5;
            gbc.insets = new Insets(0, 0, 10, 0);
            form.add(txtUser, gbc);

            JLabel lblPass = addLabel(form, "Password:", gbc, 6);
            JPasswordField txtPass = new JPasswordField();
            styleFieldBase(txtPass);
            gbc.gridy = 7;
            gbc.insets = new Insets(0, 0, 4, 0);
            form.add(txtPass, gbc);

            JLabel lblHint = new JLabel(
                    "<html><i style='color:#90a090'>Password = yang Anda buat saat registrasi</i></html>");
            lblHint.setFont(fPlain(11));
            gbc.gridy = 8;
            gbc.insets = new Insets(0, 0, 20, 0);
            form.add(lblHint, gbc);

            JButton btnLogin = buatTombol("  Masuk  ", GREEN_PRIMARY);
            btnLogin.setFont(fBold(14));
            btnLogin.setPreferredSize(new Dimension(380, 44));
            gbc.gridy = 9;
            gbc.insets = new Insets(0, 0, 10, 0);
            form.add(btnLogin, gbc);

            JButton btnDaftar = buatTombolOutline("Daftar Mahasiswa Baru", GREEN_PRIMARY);
            btnDaftar.setPreferredSize(new Dimension(380, 38));
            gbc.gridy = 10;
            gbc.insets = new Insets(0, 0, 0, 0);
            form.add(btnDaftar, gbc);
            JButton btnLupa = new JButton("Lupa Password?");
        btnLupa.setFont(fPlain(12));
        btnLupa.setForeground(new Color(13, 122, 78));     // GREEN_PRIMARY
        btnLupa.setBackground(WHITE);
        btnLupa.setBorder(BorderFactory.createEmptyBorder(4, 0, 4, 0));
        btnLupa.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLupa.setContentAreaFilled(false);
        btnLupa.setFocusPainted(false);
        gbc.gridy = 11;
        gbc.insets = new Insets(4, 0, 0, 0);
        form.add(btnLupa, gbc);
 
        // Event tombol Lupa Password
        btnLupa.addActionListener(e -> {
            int role = cbRole.getSelectedIndex();  // 0=Mahasiswa, 1=Dosen, 2=Admin
            new LupaPasswordDialog(this, role).setVisible(true);
        });

            add(form, BorderLayout.CENTER);

            // Footer
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            footer.setBackground(BG);
            JLabel lf = new JLabel("© 2025 Kelompok 4 — Sistem Informasi Akademik");
            lf.setFont(fPlain(10));
            lf.setForeground(TEXT_MUTED);
            footer.add(lf);
            add(footer, BorderLayout.SOUTH);

            // Event: ubah label sesuai role
            cbRole.addActionListener(e -> {
                int idx = cbRole.getSelectedIndex();
                if (idx == 0) {
                    lblUser.setText("NIM Mahasiswa:");
                    lblPass.setText("Password:");
                    lblHint.setText(
                            "<html><i style='color:#90a090'>Password = yang Anda buat saat registrasi</i></html>");
                } else if (idx == 1) {
                    lblUser.setText("NIDN Dosen:");
                    lblPass.setText("Password Dosen:");
                    lblHint.setText("<html><i style='color:#90a090'>Password default = NIDN</i></html>");
                } else {
                    lblUser.setText("Username Admin:");
                    lblPass.setText("Password Admin:");
                    lblHint.setText("");
                }
            });

            ActionListener doLogin = e -> {
                String user = txtUser.getText().trim();
                String pass = new String(txtPass.getPassword()).trim();
                if (user.isEmpty() || pass.isEmpty()) {
                    showWarning("ID dan Password wajib diisi!");
                    return;
                }
                int role = cbRole.getSelectedIndex();
                if (role == 2) {
                    String nama = new AdminDAO().loginAdmin(user, pass);
                    if (nama != null) {
                        dispose();
                        new AdminFrame(nama).setVisible(true);
                    } else
                        showWarning("Username atau password admin salah!");
                } else if (role == 1) {
                    Dosen d = new DosenDAO().loginDosen(user, pass);
                    if (d != null) {
                        dispose();
                        new DosenFrame(d).setVisible(true);
                    } else
                        showWarning("NIDN atau password dosen salah!");
                } else {
                    Mahasiswa m = new MahasiswaDAO().loginMahasiswa(user, pass);
                    if (m != null) {
                        dispose();
                        new MahasiswaFrame(m).setVisible(true);
                    } else
                        showWarning("NIM atau Password mahasiswa salah!");
                }
            };
            btnLogin.addActionListener(doLogin);
            txtPass.addActionListener(doLogin);
            btnDaftar.addActionListener(e -> new RegistrasiDialog(this).setVisible(true));
        }

        private JLabel addLabel(JPanel p, String teks, GridBagConstraints gbc, int row) {
            JLabel l = new JLabel(teks);
            l.setFont(fBold(12));
            l.setForeground(TEXT_PRIMARY);
            GridBagConstraints g2 = (GridBagConstraints) gbc.clone();
            g2.gridy = row;
            g2.insets = new Insets(8, 0, 2, 0);
            p.add(l, g2);
            return l;
        }

        private JTextField buatField() {
            JTextField f = new JTextField();
            styleFieldBase(f);
            return f;
        }

        private void styleFieldBase(JTextField f) {
            f.setFont(fPlain(13));
            f.setPreferredSize(new Dimension(380, 38));
            f.setBackground(WHITE);
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(4, 12, 4, 12)));
        }

        private void styleCombo(JComboBox<?> cb) {
            cb.setFont(fPlain(13));
            cb.setPreferredSize(new Dimension(380, 38));
            cb.setBackground(WHITE);
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // DIALOG REGISTRASI MAHASISWA BARU
    // ═══════════════════════════════════════════════════════════════════════
    static class RegistrasiDialog extends JDialog {
        public RegistrasiDialog(JFrame parent) {
            super(parent, "Registrasi Mahasiswa Baru", true);
            setSize(520, 640);
            setLocationRelativeTo(parent);
            setResizable(false);
            initUI();
        }

            private void initUI() {
            setLayout(new BorderLayout());
 
            JPanel header = new JPanel();
            header.setBackground(GREEN_PRIMARY);
            header.setBorder(BorderFactory.createEmptyBorder(16, 24, 16, 24));
            JLabel lbl = new JLabel("Form Registrasi Mahasiswa Baru");
            lbl.setFont(fBold(16));
            lbl.setForeground(WHITE);
            header.add(lbl);
            add(header, BorderLayout.NORTH);
 
            JPanel form = new JPanel(new GridBagLayout());
            form.setBackground(WHITE);
            form.setBorder(BorderFactory.createEmptyBorder(18, 28, 12, 28));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 4, 5, 4);
 
            JTextField txtNama     = buatField(20);
            JComboBox<String> cbJK = new JComboBox<>(new String[]{"L", "P"});
            cbJK.setFont(fPlain(12));
            JTextField txtTempat   = buatField(20);
            JTextField txtLahir    = buatField2("2000-01-01");
            JTextField txtAlamat   = buatField(20);
            JTextField txtTelp     = buatField(20);
            JTextField txtEmail    = buatField(20);
            JTextField txtAngkatan = buatField2("2025");
 
            // ── Password fields ───────────────────────────────────────────
            JPasswordField txtPass   = new JPasswordField(20);
            JPasswordField txtPass2  = new JPasswordField(20);
            txtPass.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
            txtPass2.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
 
            // Indikator kekuatan password
            JLabel lblKekuatan = new JLabel(" ");
            lblKekuatan.setFont(fPlain(11));
 
            JComboBox<String> cbProdi = new JComboBox<>();
            cbProdi.setFont(fPlain(12));
            cbProdi.addItem("-- Pilih Program Studi --");
            java.util.Map<String, Integer> prodiMap = new java.util.LinkedHashMap<>();
            try {
                ResultSet rs = new MahasiswaDAO().getAllProdi();
                while (rs.next()) {
                    String lb = rs.getString("nama_prodi") + " (" + rs.getString("nama_fakultas") + ")";
                    prodiMap.put(lb, rs.getInt("id_prodi"));
                    cbProdi.addItem(lb);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
 
            String[]     labels = { "Nama Lengkap *:", "Jenis Kelamin *:", "Tempat Lahir *:",
                    "Tanggal Lahir * (yyyy-MM-dd):", "Alamat:", "No. Telp:", "Email:",
                    "Angkatan *:", "Program Studi *:",
                    "Password * (min 8 karakter):", "Konfirmasi Password *:" };
            JComponent[] inputs = { txtNama, cbJK, txtTempat, txtLahir, txtAlamat,
                    txtTelp, txtEmail, txtAngkatan, cbProdi, txtPass, txtPass2 };
 
            for (int i = 0; i < labels.length; i++) {
                JLabel l = new JLabel(labels[i]);
                l.setFont(fBold(12));
                l.setForeground(TEXT_PRIMARY);
                gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0.38;
                form.add(l, gbc);
                gbc.gridx = 1; gbc.weightx = 0.62;
                inputs[i].setFont(fPlain(12));
                form.add(inputs[i], gbc);
 
                // Sisipkan indikator kekuatan setelah field password
                if (i == labels.length - 2) {
                    gbc.gridx = 1; gbc.gridy = i + 1;
                    // Geser semua baris berikutnya — ini ditangani loop
                }
            }
 
            // Indikator kekuatan password — muncul di bawah field password
            gbc.gridx = 1; gbc.gridy = labels.length; gbc.weightx = 0.62;
            gbc.insets = new Insets(0, 4, 6, 4);
            form.add(lblKekuatan, gbc);
 
            // Hint syarat password
            JLabel lblHint = new JLabel(
                "<html><i style='color:#888'>Min 8 karakter, kombinasi huruf + angka + karakter spesial (!@#$...)</i></html>");
            lblHint.setFont(fPlain(10));
            gbc.gridy = labels.length + 1;
            form.add(lblHint, gbc);
 
            add(new JScrollPane(form), BorderLayout.CENTER);
 
            // ── Indikator kekuatan real-time ──────────────────────────────
            txtPass.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                void update() {
                    String p = new String(txtPass.getPassword());
                    int skor = 0;
                    if (p.length() >= 8)          skor++;
                    if (p.matches(".*[a-z].*"))   skor++;
                    if (p.matches(".*[A-Z].*"))   skor++;
                    if (p.matches(".*[0-9].*"))   skor++;
                    if (p.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) skor++;
                    if (p.isEmpty()) {
                        lblKekuatan.setText(" ");
                    } else if (skor <= 2) {
                        lblKekuatan.setForeground(RED);
                        lblKekuatan.setText("● Lemah");
                    } else if (skor <= 3) {
                        lblKekuatan.setForeground(ORANGE);
                        lblKekuatan.setText("●● Sedang");
                    } else {
                        lblKekuatan.setForeground(GREEN_PRIMARY);
                        lblKekuatan.setText("●●● Kuat ✓");
                    }
                }
                public void insertUpdate(javax.swing.event.DocumentEvent e) { update(); }
                public void removeUpdate(javax.swing.event.DocumentEvent e) { update(); }
                public void changedUpdate(javax.swing.event.DocumentEvent e) { update(); }
            });
 
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 14));
            btnPanel.setBackground(BG);
            JButton btnDaftar = buatTombol("Daftar Sekarang", GREEN_PRIMARY);
            JButton btnBatal  = buatTombolOutline("Batal", TEXT_SECONDARY);
            btnPanel.add(btnDaftar);
            btnPanel.add(btnBatal);
            add(btnPanel, BorderLayout.SOUTH);
 
            btnBatal.addActionListener(e -> dispose());
            btnDaftar.addActionListener(e -> {
                if (txtNama.getText().trim().isEmpty() || cbProdi.getSelectedIndex() == 0) {
                    showInfo("Field bertanda * wajib diisi!", false);
                    return;
                }
                String pass    = new String(txtPass.getPassword()).trim();
                String pass2   = new String(txtPass2.getPassword()).trim();
                if (pass.isEmpty()) {
                    showInfo("Password wajib diisi!", false);
                    return;
                }
                if (pass.length() < 8) {
                    showInfo("Password minimal 8 karakter!", false);
                    return;
                }
                if (!pass.matches(".*[a-zA-Z].*") || !pass.matches(".*[0-9].*")) {
                    showInfo("Password harus kombinasi huruf dan angka!", false);
                    return;
                }
                if (!pass.equals(pass2)) {
                    showInfo("Password dan konfirmasi tidak cocok!", false);
                    return;
                }
                try {
                    Mahasiswa m = new Mahasiswa();
                    m.setNamaMahasiswa(txtNama.getText().trim());
                    m.setJenisKelamin((String) cbJK.getSelectedItem());
                    m.setTempatLahir(txtTempat.getText().trim());
                    m.setTanggalLahir(java.sql.Date.valueOf(txtLahir.getText().trim()));
                    m.setAlamat(txtAlamat.getText().trim());
                    m.setNoTelp(txtTelp.getText().trim());
                    m.setEmail(txtEmail.getText().trim());
                    m.setAngkatan(Integer.parseInt(txtAngkatan.getText().trim()));
                    m.setIdProdi(prodiMap.getOrDefault((String) cbProdi.getSelectedItem(), 1));
                    m.setPasswordMahasiswa(pass); // ← password asli, akan di-hash di DAO
 
                    String nim = new MahasiswaDAO().registrasiMahasiswaBaru(m);
                    if (nim != null) {
                        JOptionPane.showMessageDialog(this,
                            "Registrasi berhasil!\n\nNIM Anda: " + nim
                            + "\n\nGunakan NIM + password yang Anda buat untuk login.\n"
                            + "Simpan NIM Anda baik-baik!",
                            "Registrasi Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                    } else {
                        showInfo("Registrasi gagal! Periksa kembali data.", false);
                    }
                } catch (NumberFormatException ex) {
                    showWarning("Angkatan harus angka! Contoh: 2025");
                } catch (IllegalArgumentException ex) {
                    showWarning("Format tanggal salah! Gunakan: yyyy-MM-dd");
                } catch (Exception ex) {
                    showInfo("Error: " + ex.getMessage(), false);
                }
            });
        }

        private JTextField buatField(int cols) {
            JTextField f = new JTextField(cols);
            f.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(4, 10, 4, 10)));
            return f;
        }

        private JTextField buatField2(String def) {
            JTextField f = buatField(20);
            f.setText(def);
            return f;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // MAHASISWA FRAME
    // ═══════════════════════════════════════════════════════════════════════
    static class MahasiswaFrame extends JFrame {
        private final Mahasiswa mhs;
        private final Connection conn;

        public MahasiswaFrame(Mahasiswa mhs) {
            this.mhs = mhs;
            this.conn = DatabaseConnection.getConnection();
            setTitle("SATU Mahasiswa — " + mhs.getNamaMahasiswa() + " [" + mhs.getNim() + "]");
            setSize(1180, 740);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            initUI();
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    DatabaseConnection.closeConnection(conn);
                }
            });
        }

        private void initUI() {
            setLayout(new BorderLayout(0, 0));
            getContentPane().setBackground(BG);

            CardLayout cards = new CardLayout();
            JPanel content = new JPanel(cards);
            content.setBackground(BG);

            String[] menus = { "Beranda", "KRS Saya", "Pilih Mata Kuliah", "Nilai", "Jadwal Kuliah", "Presensi", "Transkip Nilai" };
            String[] icons = { "⌂", "", "➕", "", "", "✓", "📊" };
            JButton[] btns = new JButton[menus.length];

            content.add(buatBerandaMahasiswa(), "Beranda");
            content.add(buatPanelKrsMahasiswa(), "KRS Saya");
            content.add(buatPanelPilihKrs(), "Pilih Mata Kuliah");
            content.add(buatPanelNilai(mhs.getNim()), "Nilai");
            content.add(buatPanelJadwalMahasiswa(), "Jadwal Kuliah");
            content.add(buatPanelPresensi(mhs.getNim()), "Presensi");
            content.add(buatPanelTranskrip(), "Transkrip Akademik");

            JPanel sidebar = buatSidebar(menus, icons, btns, cards, content);

            // Topbar
            JPanel topbar = buatTopbar("SATU Mahasiswa", mhs.getNamaMahasiswa(), mhs.getNim(), this);
            add(topbar, BorderLayout.NORTH);

            setSidebarAktif(btns[0], btns);

            JPanel body = new JPanel(new BorderLayout(0, 0));
            body.add(sidebar, BorderLayout.WEST);
            body.add(content, BorderLayout.CENTER);
            add(body, BorderLayout.CENTER);
        }
        
        private JPanel buatBerandaMahasiswa() {
            JPanel panel = new JPanel(new BorderLayout(14, 14));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

            // Profile strip
            JPanel profileStrip = new JPanel(new BorderLayout(14, 0));
            profileStrip.setBackground(WHITE);
            profileStrip.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(BORDER),
                    BorderFactory.createEmptyBorder(16, 20, 16, 20)));

            JPanel ava = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(GREEN_LIGHT);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    g2.setColor(GREEN_MEDIUM);
                    g2.setFont(fBold(22));
                    String init = mhs.getNamaMahasiswa().substring(0, 1).toUpperCase();
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(init, (getWidth() - fm.stringWidth(init)) / 2,
                            (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
                    g2.dispose();
                }
            };
            ava.setPreferredSize(new Dimension(56, 56));
            ava.setOpaque(false);

            JPanel info = new JPanel(new GridLayout(3, 1, 0, 2));
            info.setOpaque(false);
            JLabel lblNama = new JLabel(mhs.getNamaMahasiswa());
            lblNama.setFont(fBold(15));
            lblNama.setForeground(TEXT_PRIMARY);
            JPanel badges = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
            badges.setOpaque(false);
            badges.add(buatBadgeGray("NIM: " + mhs.getNim()));
            badges.add(buatBadgeGray("Status: " + mhs.getStatus()));
            badges.add(buatBadgeGreen("KRS"));
            info.add(lblNama);
            info.add(badges);
            info.add(new JLabel(""));

            profileStrip.add(ava, BorderLayout.WEST);
            profileStrip.add(info, BorderLayout.CENTER);
            panel.add(profileStrip, BorderLayout.NORTH);

            // Grid kartu
            JPanel grid = new JPanel(new GridLayout(2, 3, 14, 14));
            grid.setBackground(BG);

            NilaiDAO nilaiDAO = new NilaiDAO();
            double ipk = nilaiDAO.hitungIpk(mhs.getNim());
            double ips = nilaiDAO.hitungIps(mhs.getNim());
            int sksLulus = nilaiDAO.getTotalSksLulus(mhs.getNim());

            grid.add(buatKartuInfo("Data Mahasiswa", GREEN_PRIMARY,
         "NIM", mhs.getNim(),
            "Angkatan", String.valueOf(mhs.getAngkatan()),
            "Status", mhs.getStatus()));

            grid.add(buatKartuStat(
            "IPK Kumulatif",
            String.format("%.2f", ipk),
            GREEN_PRIMARY,
            "dari skala 4.00"));

            grid.add(buatKartuStat(
            "IPS Semester",
            String.format("%.2f", ips),
            GREEN_MEDIUM,
            "semester aktif"));

        grid.add(buatKartuStat(
        "SKS Lulus",
        String.valueOf(sksLulus),
        ORANGE,
        "sudah ditempuh"));

        grid.add(buatKartuRingkasan(
        "Jadwal Kuliah",
        BLUE,
        "Klik menu 'Jadwal Kuliah' untuk melihat\njadwal perkuliahan Anda."));

    grid.add(buatKartuRingkasan(
        "Panduan KRS",
        GREEN_DARK,
        "1. Klik 'Pilih Mata Kuliah'\n2. Pilih kelas yang diinginkan\n3. Klik 'Ajukan KRS'\n4. Tunggu persetujuan dosen"));

            panel.add(grid, BorderLayout.CENTER);
            return panel;
        }

        private JPanel buatPanelKrsMahasiswa() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JPanel top = buatPanelTop("KRS Saya — Semester Aktif");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
            JButton btnRefresh = buatTombol("↻  Refresh", GREEN_PRIMARY);
            JButton btnBatalkan = buatTombol("✕  Batalkan KRS", RED);
            tools.add(btnRefresh);
            tools.add(btnBatalkan);
            top.add(tools, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);

            String[] kolom = { "ID KRS", "Mata Kuliah", "SKS", "Dosen", "Kelas", "TA", "Semester", "Status" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                        int col) {
                    Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    if (!sel) {
                        String st = (String) model.getValueAt(t.convertRowIndexToModel(row), 7);
                        if ("Disetujui".equals(st))
                            c.setBackground(new Color(220, 247, 230));
                        else if ("Ditolak".equals(st))
                            c.setBackground(new Color(255, 224, 224));
                        else
                            c.setBackground(new Color(255, 252, 215));
                    }
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    return c;
                }
            });
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            JPanel legend = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
            legend.setOpaque(false);
            legend.add(buatBadge("Menunggu", new Color(255, 252, 215), new Color(120, 100, 0)));
            legend.add(buatBadge("Disetujui", new Color(220, 247, 230), new Color(0, 100, 50)));
            legend.add(buatBadge("Ditolak", new Color(255, 224, 224), RED));
            JLabel lblSks = new JLabel("  Total SKS disetujui: 0");
            lblSks.setFont(fBold(12));
            lblSks.setForeground(GREEN_PRIMARY);
            legend.add(lblSks);
            panel.add(legend, BorderLayout.SOUTH);

            KrsDAO dao = new KrsDAO();
            Runnable load = () -> {
                model.setRowCount(0);
                int sks = 0;
                for (KRS k : dao.getKrsAktifByNim(mhs.getNim())) {
                    model.addRow(new Object[] { k.getIdKrs(), k.getNamaMataKuliah(), k.getJumlahSks(),
                            k.getNamaDosen(), k.getNamaKelas(), k.getTahunAkademik(), k.getSemesterTA(),
                            k.getStatusKrs() });
                    if ("Disetujui".equals(k.getStatusKrs()))
                        sks += k.getJumlahSks();
                }
                lblSks.setText("  Total SKS disetujui: " + sks);
            };
            load.run();
            btnRefresh.addActionListener(e -> load.run());
            btnBatalkan.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih KRS yang ingin dibatalkan!");
                    return;
                }
                if ("Disetujui".equals(model.getValueAt(row, 7))) {
                    showWarning("KRS yang sudah disetujui tidak bisa dibatalkan sendiri.");
                    return;
                }
                int conf = JOptionPane.showConfirmDialog(panel,
                        "Yakin membatalkan KRS " + model.getValueAt(row, 1) + "?",
                        "Konfirmasi", JOptionPane.YES_NO_OPTION);
                if (conf == JOptionPane.YES_OPTION) {
                    boolean ok = dao.hapusKrs((int) model.getValueAt(row, 0));
                    if (ok) {
                        model.removeRow(row);
                        showInfo("KRS dibatalkan.", true);
                    } else
                        showInfo("Gagal membatalkan!", false);
                }
            });
            return panel;
        }

        private JPanel buatPanelPilihKrs() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JPanel top = buatPanelTop("Pilih Mata Kuliah — Pengajuan KRS");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
            JButton btnRefresh = buatTombol("↻  Refresh", BLUE);
            JButton btnAjukan = buatTombol("✔  Ajukan KRS", GREEN_PRIMARY);
            tools.add(btnRefresh);
            tools.add(btnAjukan);
            top.add(tools, BorderLayout.EAST);

            JLabel hint = new JLabel(
                    "<html><i style='color:#90a090'>Pilih kelas lalu klik 'Ajukan KRS'. Pengajuan menunggu persetujuan dosen.</i></html>");
            hint.setFont(fPlain(11));
            JPanel topWrap = new JPanel(new BorderLayout(0, 6));
            topWrap.setOpaque(false);
            topWrap.add(top, BorderLayout.NORTH);
            topWrap.add(hint, BorderLayout.SOUTH);
            panel.add(topWrap, BorderLayout.NORTH);

            String[] kolom = { "ID Kelas", "Mata Kuliah", "SKS", "Dosen", "Kelas", "Kuota", "Peserta", "TA",
                    "Semester" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                        int col) {
                    Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    if (!sel) {
                        int mr = t.convertRowIndexToModel(row);
                        int kuota = (int) model.getValueAt(mr, 5), peserta = (int) model.getValueAt(mr, 6);
                        c.setBackground(peserta >= kuota ? new Color(255, 224, 224)
                                : (row % 2 == 0 ? WHITE : new Color(245, 250, 247)));
                    }
                    ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                    return c;
                }
            });
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            JLabel lblStatus = buatLabelInfo("Kelas tersedia untuk semester aktif:");
            panel.add(lblStatus, BorderLayout.SOUTH);

            KrsDAO dao = new KrsDAO();
            Runnable load = () -> {
                model.setRowCount(0);
                try {
                    ResultSet rs = dao.getKelasAktifTersedia(mhs.getNim());
                    while (rs.next()) {
                        int kuota = rs.getInt("kuota"), peserta = rs.getInt("peserta");
                        model.addRow(new Object[] { rs.getInt("id_kelas"), rs.getString("nama_mata_kuliah"),
                                rs.getInt("jumlah_sks"), rs.getString("nama_dosen"), rs.getString("nama_kelas"),
                                kuota, peserta, rs.getString("tahun_akademik"), rs.getString("semester") });
                    }
                    lblStatus.setText("  Kelas tersedia: " + model.getRowCount() + "  |  Merah = kelas penuh");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            };
            load.run();
            btnRefresh.addActionListener(e -> load.run());
            btnAjukan.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih mata kuliah terlebih dahulu!");
                    return;
                }
                int kuota = (int) model.getValueAt(row, 5), peserta = (int) model.getValueAt(row, 6);
                if (peserta >= kuota) {
                    showWarning("Kelas ini sudah penuh! (" + peserta + "/" + kuota + ")");
                    return;
                }
                int conf = JOptionPane.showConfirmDialog(panel,
                        "Ajukan KRS untuk:\n" + model.getValueAt(row, 1) + " — Kelas " + model.getValueAt(row, 4)
                                + "\n\nPengajuan akan menunggu persetujuan dosen.",
                        "Konfirmasi Pengajuan KRS", JOptionPane.YES_NO_OPTION);
                if (conf != JOptionPane.YES_OPTION)
                    return;
                boolean ok = dao.ajukanKrs(mhs.getNim(), (int) model.getValueAt(row, 0));
                showInfo(ok ? "KRS berhasil diajukan!\nCek tab 'KRS Saya' untuk melihat status."
                        : "Gagal atau sudah pernah diajukan.", ok);
                if (ok)
                    load.run();
            });
            return panel;
        }

        private JPanel buatPanelJadwalMahasiswa() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
            panel.add(buatPanelTop("Jadwal Kuliah — Semester Aktif"), BorderLayout.NORTH);

            String[] kolom = { "Mata Kuliah", "Dosen", "Ruangan", "Hari", "Jam Mulai", "Jam Selesai", "TA",
                    "Semester" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            panel.add(scrollOf(tabel), BorderLayout.CENTER);
            for (Jadwal j : new JadwalDAO().getJadwalByNim(mhs.getNim()))
                model.addRow(new Object[] { j.getNamaMataKuliah(), j.getNamaDosen(), j.getNamaRuangan(),
                        j.getHari(), j.getJamMulai(), j.getJamSelesai(), j.getTahunAkademik(), j.getSemester() });
            panel.add(buatLabelInfo("Jadwal ditampilkan berdasarkan KRS yang sudah Disetujui."), BorderLayout.SOUTH);
            return panel;
        }
        
        private JPanel buatPanelTranskrip() {

    JPanel panel = new JPanel(new BorderLayout(10,10));
    panel.setBackground(BG);
    panel.setBorder(
        BorderFactory.createEmptyBorder(
            18,18,18,18));

    panel.add(
        buatPanelTop("Transkrip Akademik"),
        BorderLayout.NORTH);

    String[] kolom = {
        "No",
        "Mata Kuliah",
        "SKS",
        "Nilai Huruf",
        "Bobot"
    };

    DefaultTableModel model =
        new DefaultTableModel(kolom,0){

        public boolean isCellEditable(
            int r,int c){
            return false;
        }
    };

    JTable tabel = buatTabel(model);

    NilaiDAO dao = new NilaiDAO();

    List<Nilai> list =
        dao.getTranskrip(mhs.getNim());

    int no = 1;

    for(Nilai n : list){

        model.addRow(new Object[]{
            no++,
            n.getNamaMataKuliah(),
            n.getJumlahSks(),
            n.getNilaiHuruf(),
            n.getBobotNilai()
        });
    }

    panel.add(
        scrollOf(tabel),
        BorderLayout.CENTER);

    double ipk =
        dao.hitungIpk(mhs.getNim());

    int sks =
        dao.getTotalSksLulus(
            mhs.getNim());

    JLabel footer = new JLabel(
        "Total SKS Lulus : "
        + sks
        + "     |     IPK : "
        + String.format("%.2f", ipk));

    footer.setFont(fBold(13));

    footer.setBorder(
        BorderFactory.createEmptyBorder(
            10,10,10,10));

    panel.add(
        footer,
        BorderLayout.SOUTH);

    return panel;
}
    }

    

    // ═══════════════════════════════════════════════════════════════════════
    // DOSEN FRAME
    // ═══════════════════════════════════════════════════════════════════════
    static class DosenFrame extends JFrame {
        private final Dosen dosen;
        private final Connection conn;

        public DosenFrame(Dosen dosen) {
            this.dosen = dosen;
            this.conn = DatabaseConnection.getConnection();
            setTitle("Portal Dosen — " + dosen.getNamaDosen());
            setSize(1180, 740);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            initUI();
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    DatabaseConnection.closeConnection(conn);
                }
            });
        }

        private void initUI() {
            setLayout(new BorderLayout(0, 0));
            getContentPane().setBackground(BG);

            CardLayout cards = new CardLayout();
            JPanel content = new JPanel(cards);
            content.setBackground(BG);

            String[] menus = { "Beranda", "Persetujuan KRS", "Jadwal Saya", "Tambah Kelas & Jadwal", "Input Nilai",
                    "Presensi Mahasiswa" };
            String[] icons = { "⌂", "", "", "➕", "✓", "" };
            JButton[] btns = new JButton[menus.length];

            content.add(buatBerandaDosen(), "Beranda");
            content.add(buatPanelPersetujuanKrs(), "Persetujuan KRS");
            content.add(buatPanelJadwalDosen(), "Jadwal Saya");
            content.add(buatPanelTambahKelas(), "Tambah Kelas & Jadwal");
            content.add(buatPanelInputNilaiDosen(), "Input Nilai"); 
            content.add(buatPanelPresensiDosen(), "Presensi Mahasiswa");

            add(buatTopbar("Portal Dosen", dosen.getNamaDosen(), dosen.getNidn(), this), BorderLayout.NORTH);
            JPanel sidebar = buatSidebar(menus, icons, btns, cards, content);
            setSidebarAktif(btns[0], btns);

            JPanel body = new JPanel(new BorderLayout(0, 0));
            body.add(sidebar, BorderLayout.WEST);
            body.add(content, BorderLayout.CENTER);
            add(body, BorderLayout.CENTER);
        }

        private JPanel buatBerandaDosen() {
            JPanel panel = new JPanel(new BorderLayout(14, 14));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

            JLabel judul = new JLabel("Selamat Datang, " + dosen.getNamaDosen() + "!");
            judul.setFont(fBold(17));
            judul.setForeground(TEXT_PRIMARY);
            panel.add(judul, BorderLayout.NORTH);

            JPanel grid = new JPanel(new GridLayout(1, 3, 14, 14));
            grid.setBackground(BG);

            // Kartu info dosen
            grid.add(buatKartuInfo("Data Dosen", GREEN_PRIMARY,
                    "NIDN", dosen.getNidn(),
                    "Jabatan", dosen.getJabatan() != null ? dosen.getJabatan() : "-"));

            // KRS pending
            DosenDAO dao = new DosenDAO();
            int pending = dao.getKrsPendingByNidn(dosen.getNidn()).size();
            grid.add(buatKartuStat("KRS Menunggu Persetujuan", String.valueOf(pending),
                    pending > 0 ? RED : GREEN_PRIMARY, "pengajuan masuk"));

            // Menu cepat
            grid.add(buatKartuRingkasan("Menu Cepat", GREEN_PRIMARY,
                    "• Persetujuan KRS\n• Lihat jadwal saya\n• Input presensi\n• Tambah kelas baru"));

            panel.add(grid, BorderLayout.CENTER);
            return panel;
        }

        private JPanel buatPanelPersetujuanKrs() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JPanel top = buatPanelTop("Persetujuan KRS Mahasiswa");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
            JButton btnPending = buatTombol("KRS Pending", ORANGE);
            JButton btnSemua = buatTombol("Semua KRS", BLUE);
            JButton btnSetujui = buatTombol("✔  Setujui", GREEN_PRIMARY);
            JButton btnTolak = buatTombol("✕  Tolak", RED);
            tools.add(btnPending);
            tools.add(btnSemua);
            tools.add(Box.createHorizontalStrut(8));
            tools.add(btnSetujui);
            tools.add(btnTolak);
            top.add(tools, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);

            String[] kolom = { "ID KRS", "Mahasiswa", "Mata Kuliah", "SKS", "Kelas", "TA", "Semester", "Status" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            tabel.setDefaultRenderer(Object.class, statusRenderer(model, 7));
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            JLabel lblInfo = buatLabelInfo("Klik 'KRS Pending' untuk lihat pengajuan yang menunggu persetujuan.");
            panel.add(lblInfo, BorderLayout.SOUTH);

            DosenDAO dao = new DosenDAO();
            Runnable loadPending = () -> {
                model.setRowCount(0);
                List<KRS> list = dao.getKrsPendingByNidn(dosen.getNidn());
                for (KRS k : list)
                    model.addRow(new Object[] { k.getIdKrs(), k.getNamaMahasiswa(), k.getNamaMataKuliah(),
                            k.getJumlahSks(), k.getNamaKelas(), k.getTahunAkademik(), k.getSemesterTA(),
                            k.getStatusKrs() });
                lblInfo.setText("  KRS menunggu persetujuan: " + list.size() + " data");
            };
            Runnable loadSemua = () -> {
                model.setRowCount(0);
                List<KRS> list = dao.getAllKrsByNidn(dosen.getNidn());
                for (KRS k : list)
                    model.addRow(new Object[] { k.getIdKrs(), k.getNamaMahasiswa(), k.getNamaMataKuliah(),
                            k.getJumlahSks(), k.getNamaKelas(), k.getTahunAkademik(), k.getSemesterTA(),
                            k.getStatusKrs() });
                lblInfo.setText("  Total KRS kelas Anda: " + list.size() + " data");
            };
            btnPending.addActionListener(e -> loadPending.run());
            btnSemua.addActionListener(e -> loadSemua.run());
            loadPending.run();

            btnSetujui.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih KRS!");
                    return;
                }
                boolean ok = dao.updateStatusKrs((int) model.getValueAt(row, 0), "Disetujui");
                if (ok)
                    model.setValueAt("Disetujui", row, 7);
                showInfo(ok ? "KRS disetujui!" : "Gagal!", ok);
            });
            btnTolak.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih KRS!");
                    return;
                }
                if (JOptionPane.showConfirmDialog(panel, "Yakin menolak KRS ini?", "Konfirmasi",
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    boolean ok = dao.updateStatusKrs((int) model.getValueAt(row, 0), "Ditolak");
                    if (ok)
                        model.setValueAt("Ditolak", row, 7);
                    showInfo(ok ? "KRS ditolak." : "Gagal!", ok);
                }
            });
            return panel;
        }

        private JPanel buatPanelJadwalDosen() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JPanel top = buatPanelTop("Jadwal Mengajar Saya — Semester Aktif");
            JButton btnRefresh = buatTombol("↻  Refresh", GREEN_PRIMARY);
            top.add(btnRefresh, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);

            String[] kolom = { "Mata Kuliah", "Ruangan", "Hari", "Jam Mulai", "Jam Selesai", "TA", "Semester" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            DosenDAO dao = new DosenDAO();
            Runnable load = () -> {
                model.setRowCount(0);
                for (Jadwal j : dao.getJadwalByNidn(dosen.getNidn()))
                    model.addRow(new Object[] { j.getNamaMataKuliah(), j.getNamaRuangan(),
                            j.getHari(), j.getJamMulai(), j.getJamSelesai(), j.getTahunAkademik(), j.getSemester() });
            };
            load.run();
            btnRefresh.addActionListener(e -> load.run());
            return panel;
        }

        private JPanel buatPanelTambahKelas() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
            panel.add(buatPanelTop("Tambah Kelas & Jadwal Baru"), BorderLayout.NORTH);

            JPanel kartu = buatKartu(null);
            kartu.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(8, 10, 8, 10);

            DosenDAO dao = new DosenDAO();
            JComboBox<String> cbMk = new JComboBox<>();
            cbMk.addItem("-- Pilih Mata Kuliah --");
            java.util.Map<String, String> mkMap = new java.util.LinkedHashMap<>();
            try {
                ResultSet rs = dao.getAllMataKuliah();
                while (rs.next()) {
                    String lb = rs.getString("nama_mata_kuliah") + " (" + rs.getInt("jumlah_sks") + " SKS)";
                    mkMap.put(lb, rs.getString("kode_mk"));
                    cbMk.addItem(lb);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            JComboBox<String> cbNamaKelas = new JComboBox<>(new String[] { "A", "B", "C", "D" });
            JTextField txtKuota = new JTextField("40", 8);
            JComboBox<String> cbHari = new JComboBox<>(
                    new String[] { "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" });
            JTextField txtJamMulai = new JTextField("08:00", 8);
            JTextField txtJamSelesai = new JTextField("10:30", 8);

            JComboBox<String> cbRuangan = new JComboBox<>();
            cbRuangan.addItem("-- Pilih Ruangan --");
            java.util.Map<String, Integer> ruanganMap = new java.util.LinkedHashMap<>();
            try {
                ResultSet rs = dao.getRuangan();
                while (rs.next()) {
                    String lb = rs.getString("nama_ruangan");
                    ruanganMap.put(lb, rs.getInt("id_ruangan"));
                    cbRuangan.addItem(lb);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            String[][] rows = { { "Mata Kuliah:" }, { "Nama Kelas:" }, { "Kuota Mahasiswa:" },
                    { "Hari:" }, { "Jam Mulai (HH:mm):" }, { "Jam Selesai (HH:mm):" }, { "Ruangan:" } };
            JComponent[] comps = { cbMk, cbNamaKelas, txtKuota, cbHari, txtJamMulai, txtJamSelesai, cbRuangan };

            for (int i = 0; i < rows.length; i++) {
                JLabel l = new JLabel(rows[i][0]);
                l.setFont(fBold(12));
                l.setForeground(TEXT_PRIMARY);
                gbc.gridx = 0;
                gbc.gridy = i;
                gbc.weightx = 0.3;
                kartu.add(l, gbc);
                comps[i].setFont(fPlain(12));
                gbc.gridx = 1;
                gbc.weightx = 0.7;
                kartu.add(comps[i], gbc);
            }
            JButton btnSimpan = buatTombol("Simpan Kelas & Jadwal", GREEN_PRIMARY);
            btnSimpan.setFont(fBold(13));
            gbc.gridx = 0;
            gbc.gridy = rows.length;
            gbc.gridwidth = 2;
            gbc.insets = new Insets(18, 10, 10, 10);
            kartu.add(btnSimpan, gbc);

            JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
            wrap.setBackground(BG);
            wrap.add(kartu);
            panel.add(wrap, BorderLayout.CENTER);
            panel.add(buatLabelInfo("Kelas baru otomatis masuk ke Tahun Akademik yang sedang aktif."),
                    BorderLayout.SOUTH);

            btnSimpan.addActionListener(e -> {
                if (cbMk.getSelectedIndex() == 0 || cbRuangan.getSelectedIndex() == 0) {
                    showWarning("Pilih Mata Kuliah dan Ruangan terlebih dahulu!");
                    return;
                }
                try {
                    String kodeMk = mkMap.get((String) cbMk.getSelectedItem());
                    String namaKelas = (String) cbNamaKelas.getSelectedItem();
                    int kuota = Integer.parseInt(txtKuota.getText().trim());
                    String hari = (String) cbHari.getSelectedItem();
                    String jamMulai = txtJamMulai.getText().trim();
                    String jamSelesai = txtJamSelesai.getText().trim();
                    int idRuangan = ruanganMap.get((String) cbRuangan.getSelectedItem());

                    int idKelas = dao.tambahKelasReturnId(kodeMk, dosen.getNidn(), namaKelas, kuota);
                    if (idKelas == -1) {
                        showInfo("Gagal membuat kelas! Kelas " + namaKelas + " mungkin sudah ada.", false);
                        return;
                    }
                    boolean ok = dao.tambahJadwal(idKelas, idRuangan, hari, jamMulai, jamSelesai);
                    showInfo(ok ? "Kelas & jadwal berhasil ditambahkan!\n" + cbMk.getSelectedItem()
                            + " — Kelas " + namaKelas + "\n" + hari + " " + jamMulai + "–" + jamSelesai
                            : "Kelas dibuat tapi jadwal gagal. Cek konflik ruangan.", ok);
                } catch (NumberFormatException ex) {
                    showWarning("Kuota harus berupa angka!");
                }
            });
            return panel;
        }

        private JPanel buatPanelInputNilaiDosen() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
 
            // ── Header ────────────────────────────────────────────────────
            JPanel top = buatPanelTop("Input Nilai Mahasiswa");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
 
            DosenDAO dosenDAO = new DosenDAO();
 
            // Dropdown pilih kelas
            JComboBox<String> cbKelas = new JComboBox<>();
            cbKelas.addItem("-- Pilih Kelas --");
            java.util.Map<String, Integer> kelasMap = new java.util.LinkedHashMap<>();
            try {
                ResultSet rs = dosenDAO.getJadwalKelasDetail(dosen.getNidn());
                while (rs.next()) {
                    String lb = rs.getString("nama_mata_kuliah") + " - Kelas " + rs.getString("nama_kelas");
                    // Hindari duplikat kelas (bisa ada beberapa jadwal)
                    if (!kelasMap.containsKey(lb)) {
                        kelasMap.put(lb, rs.getInt("id_kelas"));
                        cbKelas.addItem(lb);
                    }
                }
            } catch (Exception ex) { ex.printStackTrace(); }
            cbKelas.setFont(fPlain(12));
            cbKelas.setPreferredSize(new Dimension(320, 32));
 
            JButton btnLoad   = buatTombol("Load Mahasiswa", BLUE);
            JButton btnSimpan = buatTombol("Simpan Nilai", GREEN_PRIMARY);
 
            tools.add(new JLabel("Kelas:"));
            tools.add(cbKelas);
            tools.add(btnLoad);
            tools.add(btnSimpan);
            top.add(tools, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);
 
            // ── Tabel ────────────────────────────────────────────────────
            String[] kolom = { "id_krs", "NIM", "Nama Mahasiswa",
                               "Tugas (0-100)", "UTS (0-100)", "UAS (0-100)",
                               "Nilai Angka", "Huruf", "Status" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    // Hanya kolom Tugas, UTS, UAS yang bisa diisi
                    return c == 3 || c == 4 || c == 5;
                }
            };
            JTable tabel = buatTabel(model);
            tabel.setRowHeight(34);
 
            // Sembunyikan kolom id_krs (index 0)
            tabel.getColumnModel().getColumn(0).setMinWidth(0);
            tabel.getColumnModel().getColumn(0).setMaxWidth(0);
            tabel.getColumnModel().getColumn(0).setWidth(0);
 
            // Warna kolom nilai angka dan huruf
            tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                    if (!sel) {
                        if (col == 7) { // kolom Huruf
                            String h = val != null ? val.toString() : "";
                            switch (h) {
                                case "A": c.setBackground(new Color(209,240,220)); break;
                                case "B": c.setBackground(new Color(230,247,210)); break;
                                case "C": c.setBackground(new Color(255,252,210)); break;
                                case "D": c.setBackground(new Color(255,235,205)); break;
                                case "E": c.setBackground(new Color(255,215,215)); break;
                                default:  c.setBackground(row%2==0 ? WHITE : new Color(245,250,247));
                            }
                        } else {
                            c.setBackground(row%2==0 ? WHITE : new Color(245,250,247));
                        }
                    }
                    ((JLabel)c).setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
                    return c;
                }
            });
 
            panel.add(scrollOf(tabel), BorderLayout.CENTER);
 
            JLabel lblInfo = buatLabelInfo(
                "Bobot: Tugas 30% + UTS 30% + UAS 40%  |  " +
                "A≥85  B≥70  C≥60  D≥50  E<50");
            panel.add(lblInfo, BorderLayout.SOUTH);
 
            NilaiDAO nilaiDAO = new NilaiDAO();
            KrsDAO   krsDAO   = new KrsDAO();
 
            // ── Load mahasiswa kelas terpilih ─────────────────────────────
            btnLoad.addActionListener(e -> {
                if (cbKelas.getSelectedIndex() == 0) {
                    showWarning("Pilih kelas terlebih dahulu!");
                    return;
                }
                int idKelas = kelasMap.get((String) cbKelas.getSelectedItem());
                model.setRowCount(0);
                try {
                    // Ambil semua mahasiswa KRS Disetujui di kelas ini
                    ResultSet rs = new PresensiDAO().getMahasiswaByKelas(idKelas);
                    while (rs.next()) {
                        int idKrs = rs.getInt("id_krs");
                        String nim = rs.getString("nim");
                        String nama = rs.getString("nama_mahasiswa");
 
                        // Cek apakah nilai sudah ada
                        List<Nilai> existing = nilaiDAO.getNilaiByNim(nim);
                        boolean sudahAda = existing.stream()
                                .anyMatch(n -> n.getIdKrs() == idKrs);
 
                        if (sudahAda) {
                            Nilai n = existing.stream()
                                    .filter(x -> x.getIdKrs() == idKrs)
                                    .findFirst().get();
                            model.addRow(new Object[]{
                                idKrs, nim, nama,
                                n.getNilaiTugas(), n.getNilaiUts(), n.getNilaiUas(),
                                String.format("%.1f", n.getNilaiAngka()),
                                n.getNilaiHuruf(), "✓ Sudah ada"
                            });
                        } else {
                            model.addRow(new Object[]{
                                idKrs, nim, nama,
                                0.0, 0.0, 0.0, "-", "-", "Belum diinput"
                            });
                        }
                    }
                    lblInfo.setText("  Loaded " + model.getRowCount()
                        + " mahasiswa. Isi nilai lalu klik Simpan. (✓ = nilai sudah ada)");
                } catch (Exception ex) { ex.printStackTrace(); }
            });
 
            // ── Hitung nilai angka & huruf real-time saat edit ───────────
            model.addTableModelListener(ev -> {
                int row = ev.getFirstRow();
                int col = ev.getColumn();
                if (col < 3 || col > 5 || row < 0) return;
                try {
                    double tugas = parseNilai(model.getValueAt(row, 3));
                    double uts   = parseNilai(model.getValueAt(row, 4));
                    double uas   = parseNilai(model.getValueAt(row, 5));
                    double angka = tugas*0.3 + uts*0.3 + uas*0.4;
                    angka = Math.min(100, Math.max(0, angka));
                    String huruf = angka>=85?"A": angka>=70?"B": angka>=60?"C": angka>=50?"D":"E";
                    model.setValueAt(String.format("%.1f", angka), row, 6);
                    model.setValueAt(huruf, row, 7);
                } catch (Exception ignored) {}
            });
 
            // ── Simpan semua nilai ────────────────────────────────────────
            btnSimpan.addActionListener(e -> {
                if (model.getRowCount() == 0) {
                    showWarning("Load mahasiswa dulu!");
                    return;
                }
                if (tabel.isEditing()) tabel.getCellEditor().stopCellEditing();
 
                int berhasil = 0, gagal = 0, diupdate = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    String status = model.getValueAt(i, 8).toString();
                    boolean sudahAda = status.startsWith("✓ Sudah ada");
 
                    try {
                        Nilai n = new Nilai();
                        n.setIdKrs((int) model.getValueAt(i, 0));
                        n.setNilaiTugas(parseNilai(model.getValueAt(i, 3)));
                        n.setNilaiUts(parseNilai(model.getValueAt(i, 4)));
                        n.setNilaiUas(parseNilai(model.getValueAt(i, 5)));
                        n.setIsFinal(true);
 
                        boolean ok;
                        if (sudahAda) {
                            // Update nilai yang sudah ada
                            ok = nilaiDAO.updateNilai(n);
                            if (ok) {
                                model.setValueAt("✓ Diperbarui", i, 8);
                                diupdate++;
                            } else {
                                gagal++;
                            }
                        } else {
                            // Insert nilai baru
                            ok = nilaiDAO.simpanNilai(n);
                            if (ok) {
                                model.setValueAt("✓ Tersimpan", i, 8);
                                berhasil++;
                            } else {
                                gagal++;
                            }
                        }
                    } catch (Exception ex) {
                        gagal++;
                        ex.printStackTrace();
                    }
                }
                showInfo("Selesai!\n✔ Baru disimpan: " + berhasil
                    + "\n✎ Diperbarui: " + diupdate
                    + "\n✕ Gagal: " + gagal, berhasil > 0 || diupdate > 0);
            });
 
            return panel;
        }
 
        /** Helper: parse nilai dari Object (bisa String atau Double) */
        private double parseNilai(Object val) {
            if (val == null) return 0;
            try { return Double.parseDouble(val.toString()); }
            catch (NumberFormatException e) { return 0; }
        }
// ═══ END PATCH 2 ════════════════════════════════════════════════════════════
 
 
// ══════════════════════════════════════════════════════════
// PATCH 3 — DosenDAO.loginDosen() update untuk hash support
// ══════════════════════════════════════════════════════════
// Ganti method loginDosen() yang ada dengan ini:
 
    public Dosen loginDosen(String nidn, String password) {
        String sql = "SELECT * FROM kelompok4.dosen WHERE nidn=?";
        try (Connection conn = util.DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nidn);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            String stored = rs.getString("password_dosen");
 
            boolean ok = password.equals(stored);
            if (!ok) return null;
 
            Dosen d = new Dosen();
            d.setNidn(rs.getString("nidn"));
            d.setNamaDosen(rs.getString("nama_dosen"));
            d.setEmail(rs.getString("email"));
            d.setJabatan(rs.getString("jabatan"));
            d.setIdProdi(rs.getInt("id_prodi"));
            return d;
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }
 
    private void upgradePasswordDosen(String nidn, String hashed) {
        String sql = "UPDATE kelompok4.dosen SET password_dosen=? WHERE nidn=?";
        try (Connection conn = util.DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashed);
            ps.setString(2, nidn);
            ps.executeUpdate();
            System.out.println("[SECURITY] Password dosen " + nidn + " di-upgrade ke hash.");
        } catch (Exception e) { e.printStackTrace(); }
    }

        private JPanel buatPanelPresensiDosen() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JLabel judul = new JLabel("Input Presensi Mahasiswa");
            judul.setFont(fBold(15));
            judul.setForeground(TEXT_PRIMARY);

            JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
            toolbar.setOpaque(false);

            JComboBox<String> cbJadwal = new JComboBox<>();
            cbJadwal.addItem("-- Pilih Kelas/Jadwal --");
            java.util.Map<String, int[]> jadwalMap = new java.util.LinkedHashMap<>();
            DosenDAO dosenDAO = new DosenDAO();
            try {
                ResultSet rs = dosenDAO.getJadwalKelasDetail(dosen.getNidn());
                while (rs.next()) {
                    String lb = rs.getString("nama_mata_kuliah") + " - Kelas " + rs.getString("nama_kelas")
                            + " | " + rs.getString("hari") + " " + rs.getString("jam_mulai");
                    jadwalMap.put(lb, new int[] { rs.getInt("id_kelas"), rs.getInt("id_jadwal") });
                    cbJadwal.addItem(lb);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            cbJadwal.setFont(fPlain(12));
            cbJadwal.setPreferredSize(new Dimension(360, 32));
            String today = new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
            JTextField txtTanggal = new JTextField(today, 12);
            txtTanggal.setFont(fPlain(12));
            JButton btnLoad = buatTombol("Load Mahasiswa", BLUE);
            JButton btnSimpan = buatTombol("Simpan Presensi", GREEN_PRIMARY);

            JPanel topWrap = new JPanel(new BorderLayout(0, 8));
            topWrap.setOpaque(false);
            topWrap.add(judul, BorderLayout.NORTH);
            toolbar.add(new JLabel("Kelas:"));
            toolbar.add(cbJadwal);
            toolbar.add(new JLabel("Tanggal:"));
            toolbar.add(txtTanggal);
            toolbar.add(btnLoad);
            toolbar.add(btnSimpan);
            topWrap.add(toolbar, BorderLayout.SOUTH);
            panel.add(topWrap, BorderLayout.NORTH);

            String[] kolom = { "ID KRS", "NIM", "Nama Mahasiswa", "Status Kehadiran", "Keterangan" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return c == 3 || c == 4;
                }
            };
            JTable tabel = buatTabel(model);
            tabel.setRowHeight(34);
            tabel.getColumnModel().getColumn(3).setCellEditor(
                    new DefaultCellEditor(new JComboBox<>(new String[] { "Hadir", "Izin", "Sakit", "Alpha" })));
            tabel.removeColumn(tabel.getColumnModel().getColumn(0));
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            JLabel lblInfo = buatLabelInfo("Pilih kelas & tanggal → Load Mahasiswa → atur status → Simpan Presensi.");
            panel.add(lblInfo, BorderLayout.SOUTH);

            PresensiDAO presensiDAO = new PresensiDAO();
            btnLoad.addActionListener(e -> {
                if (cbJadwal.getSelectedIndex() == 0) {
                    showWarning("Pilih kelas/jadwal!");
                    return;
                }
                int idKelas = jadwalMap.get((String) cbJadwal.getSelectedItem())[0];
                model.setRowCount(0);
                try {
                    ResultSet rs = presensiDAO.getMahasiswaByKelas(idKelas);
                    while (rs.next())
                        model.addRow(new Object[] { rs.getInt("id_krs"), rs.getString("nim"),
                                rs.getString("nama_mahasiswa"), "Hadir", "" });
                    lblInfo.setText(model.getRowCount() == 0
                            ? "  Tidak ada mahasiswa Disetujui di kelas ini."
                            : "  Loaded " + model.getRowCount() + " mahasiswa. Ubah status lalu Simpan.");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            btnSimpan.addActionListener(e -> {
                if (model.getRowCount() == 0) {
                    showWarning("Load mahasiswa dulu!");
                    return;
                }
                if (tabel.isEditing())
                    tabel.getCellEditor().stopCellEditing();
                String key = (String) cbJadwal.getSelectedItem();
                int idJadwal = jadwalMap.get(key)[1];
                int berhasil = 0, gagal = 0;
                for (int i = 0; i < model.getRowCount(); i++) {
                    Presensi p = new Presensi();
                    p.setIdKrs((int) model.getValueAt(i, 0));
                    p.setIdJadwal(idJadwal);
                    try {
                        p.setTanggal(java.sql.Date.valueOf(txtTanggal.getText().trim()));
                    } catch (Exception ex) {
                        showWarning("Format tanggal salah! Gunakan yyyy-MM-dd");
                        return;
                    }
                    p.setStatus((String) model.getValueAt(i, 3));
                    Object ket = model.getValueAt(i, 4);
                    p.setKeterangan(ket != null ? ket.toString() : "");
                    if (presensiDAO.tambahPresensi(p))
                        berhasil++;
                    else
                        gagal++;
                }
                showInfo("Presensi disimpan!\n✔ Berhasil: " + berhasil + "\n✕ Gagal/duplikat: " + gagal, berhasil > 0);
                lblInfo.setText("  Tersimpan: " + berhasil + " | Gagal/duplikat: " + gagal);
            });
            return panel;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // ADMIN FRAME
    // ═══════════════════════════════════════════════════════════════════════
    static class AdminFrame extends JFrame {
        private final String namaAdmin;
        private final Connection conn;

        public AdminFrame(String namaAdmin) {
            this.namaAdmin = namaAdmin;
            this.conn = DatabaseConnection.getConnection();
            setTitle("Panel Admin — Sistem Informasi Akademik [" + namaAdmin + "]");
            setSize(1200, 760);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            initUI();
            addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    DatabaseConnection.closeConnection(conn);
                }
            });
        }

        private void initUI() {
            setLayout(new BorderLayout(0, 0));
            getContentPane().setBackground(BG);

            CardLayout cards = new CardLayout();
            JPanel content = new JPanel(cards);
            content.setBackground(BG);

            String[] menus = { "Beranda", "Data Mahasiswa", "Data Dosen", "Kelola KRS", "Nilai", "Jadwal", "Presensi" };
            String[] icons = { "⌂", "", "", "", "", "" , ""};
            JButton[] btns = new JButton[menus.length];

            content.add(buatBerandaAdmin(), "Beranda");
            content.add(buatPanelMahasiswaAdmin(), "Data Mahasiswa");
            content.add(buatPanelDosenAdmin(), "Data Dosen"); 
            content.add(buatPanelKrsAdmin(), "Kelola KRS");
            content.add(buatPanelNilai(null), "Nilai");
            content.add(buatPanelJadwalAdmin(), "Jadwal");
            content.add(buatPanelPresensi(null), "Presensi");

            add(buatTopbar("Panel Admin", namaAdmin, "Administrator", this), BorderLayout.NORTH);
            JPanel sidebar = buatSidebar(menus, icons, btns, cards, content);
            setSidebarAktif(btns[0], btns);

            JPanel body = new JPanel(new BorderLayout(0, 0));
            body.add(sidebar, BorderLayout.WEST);
            body.add(content, BorderLayout.CENTER);
            add(body, BorderLayout.CENTER);
        }

        private JPanel buatBerandaAdmin() {
            JPanel panel = new JPanel(new BorderLayout(14, 14));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(22, 22, 22, 22));

            JLabel judul = new JLabel("Selamat Datang, " + namaAdmin + "!");
            judul.setFont(fBold(17));
            judul.setForeground(TEXT_PRIMARY);
            panel.add(judul, BorderLayout.NORTH);

            JPanel grid = new JPanel(new GridLayout(2, 3, 14, 14));
            grid.setBackground(BG);

            int jmlMhs = new MahasiswaDAO().getAllMahasiswa().size();
            int jmlKrs = new KrsDAO().getAllKrs().size();
            int jmlJadwal = new JadwalDAO().getAllJadwalAktif().size();

            grid.add(buatKartuStat("Total Mahasiswa", String.valueOf(jmlMhs), GREEN_PRIMARY, "mahasiswa terdaftar"));
            grid.add(buatKartuStat("Total KRS", String.valueOf(jmlKrs), BLUE, "pengajuan KRS"));
            grid.add(buatKartuStat("Jadwal Aktif", String.valueOf(jmlJadwal), ORANGE, "jadwal perkuliahan"));
            grid.add(buatKartuRingkasan("Manajemen Mahasiswa", GREEN_PRIMARY,
                    "• Lihat semua mahasiswa\n• Cari mahasiswa\n• Edit / hapus data"));
            grid.add(buatKartuRingkasan("Manajemen KRS", BLUE,
                    "• Lihat semua KRS\n• Setujui / tolak KRS\n• Hapus KRS"));
            grid.add(buatKartuRingkasan("Nilai & Presensi", ORANGE,
                    "• Lihat nilai per mahasiswa\n• Hitung IPK\n• Rekap presensi & absensi"));

            panel.add(grid, BorderLayout.CENTER);
            return panel;
        }

        private JPanel buatPanelMahasiswaAdmin() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JPanel top = buatPanelTop("Data Mahasiswa");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
            JTextField txtCari = buatTextField(16);
            JButton btnCari = buatTombol("Cari", BLUE);
            JButton btnRefresh = buatTombol("↻  Semua", GREEN_PRIMARY);
            JButton btnEdit = buatTombol("Edit Status", ORANGE);
            JButton btnHapus = buatTombol("✕  Hapus", RED);
            tools.add(new JLabel("Cari Nama:"));
            tools.add(txtCari);
            tools.add(btnCari);
            tools.add(btnRefresh);
            tools.add(Box.createHorizontalStrut(8));
            tools.add(btnEdit);
            tools.add(btnHapus);
            top.add(tools, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);

            String[] kolom = { "NIM", "Nama Mahasiswa", "Jenis Kelamin", "Angkatan", "Email", "No. Telp", "Status" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            MahasiswaDAO dao = new MahasiswaDAO();
            Runnable load = () -> {
                model.setRowCount(0);
                for (Mahasiswa m : dao.getAllMahasiswa())
                    model.addRow(new Object[] { m.getNim(), m.getNamaMahasiswa(),
                            "L".equals(m.getJenisKelamin()) ? "Laki-laki" : "Perempuan",
                            m.getAngkatan(), m.getEmail(), m.getNoTelp(), m.getStatus() });
            };
            load.run();
            btnRefresh.addActionListener(e -> load.run());
            btnCari.addActionListener(e -> {
                model.setRowCount(0);
                for (Mahasiswa m : dao.cariMahasiswaByNama(txtCari.getText().trim()))
                    model.addRow(new Object[] { m.getNim(), m.getNamaMahasiswa(),
                            "L".equals(m.getJenisKelamin()) ? "Laki-laki" : "Perempuan",
                            m.getAngkatan(), m.getEmail(), m.getNoTelp(), m.getStatus() });
            });
            btnHapus.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih mahasiswa yang ingin dihapus!");
                    return;
                }
                if (JOptionPane.showConfirmDialog(panel,
                        "Yakin hapus " + model.getValueAt(row, 1) + "?\nSemua KRS, nilai, presensi ikut terhapus!",
                        "Konfirmasi", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    boolean ok = dao.hapusMahasiswa((String) model.getValueAt(row, 0));
                    if (ok) {
                        model.removeRow(row);
                        showInfo("Mahasiswa dihapus!", true);
                    } else
                        showInfo("Gagal menghapus! Cek data terkait.", false);
                }
            });
            btnEdit.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih mahasiswa!");
                    return;
                }
                String[] statuses = { "Aktif", "Cuti", "DO", "Lulus" };
                String status = (String) JOptionPane.showInputDialog(panel,
                        "Ubah status " + model.getValueAt(row, 1) + ":",
                        "Edit Status", JOptionPane.QUESTION_MESSAGE, null, statuses, model.getValueAt(row, 6));
                if (status != null) {
                    String nim = (String) model.getValueAt(row, 0);
                    boolean ok = dao.updateStatusMahasiswa(nim, status);
                    if (ok) {
                        model.setValueAt(status, row, 6);
                        showInfo("Status diperbarui!", true);
                    } else
                        showInfo("Gagal update!", false);
                }
            });
            return panel;
        }

                private JPanel buatPanelDosenAdmin() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
 
            // ── Header + toolbar ─────────────────────────────────────────
            JPanel top = buatPanelTop("Data Dosen");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
 
            JTextField txtCari = buatTextField(16);
            JButton btnCari    = buatTombol("Cari", BLUE);
            JButton btnRefresh = buatTombol("↻  Semua", GREEN_PRIMARY);
            JButton btnTambah  = buatTombol("+ Tambah Dosen", GREEN_PRIMARY);
            JButton btnEdit    = buatTombol("✎  Edit", ORANGE);
            JButton btnHapus   = buatTombol("✕  Hapus", RED);
            JButton btnReset   = buatTombol("🔑 Reset Password", new Color(100, 60, 160));
 
            tools.add(new JLabel("Cari Nama:"));
            tools.add(txtCari);
            tools.add(btnCari);
            tools.add(btnRefresh);
            tools.add(Box.createHorizontalStrut(8));
            tools.add(btnTambah);
            tools.add(btnEdit);
            tools.add(btnHapus);
            tools.add(btnReset);
            top.add(tools, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);
 
            // ── Tabel ────────────────────────────────────────────────────
            String[] kolom = { "NIDN", "Nama Dosen", "Jenis Kelamin", "Email", "No. Telp", "Jabatan" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable tabel = buatTabel(model);
            panel.add(scrollOf(tabel), BorderLayout.CENTER);
 
            DosenDAO dao = new DosenDAO();
 
            // ── Load semua dosen ─────────────────────────────────────────
            Runnable loadSemua = () -> {
                model.setRowCount(0);
                for (Dosen d : dao.getAllDosen())
                    model.addRow(new Object[]{
                        d.getNidn(), d.getNamaDosen(),
                        "L".equals(d.getJenisKelamin()) ? "Laki-laki" : "Perempuan",
                        d.getEmail(), d.getNoTelp(), d.getJabatan()
                    });
            };
            loadSemua.run();
 
            // ── Cari ─────────────────────────────────────────────────────
            btnCari.addActionListener(e -> {
                String kata = txtCari.getText().trim().toLowerCase();
                model.setRowCount(0);
                for (Dosen d : dao.getAllDosen()) {
                    if (d.getNamaDosen().toLowerCase().contains(kata)
                            || d.getNidn().contains(kata))
                        model.addRow(new Object[]{
                            d.getNidn(), d.getNamaDosen(),
                            "L".equals(d.getJenisKelamin()) ? "Laki-laki" : "Perempuan",
                            d.getEmail(), d.getNoTelp(), d.getJabatan()
                        });
                }
            });
            btnRefresh.addActionListener(e -> { txtCari.setText(""); loadSemua.run(); });
 
            // ── Tambah Dosen ─────────────────────────────────────────────
            btnTambah.addActionListener(e -> {
                JTextField fNidn    = new JTextField(15);
                JTextField fNama    = new JTextField(20);
                JComboBox<String> fJK = new JComboBox<>(new String[]{"L", "P"});
                JTextField fEmail   = new JTextField(20);
                JTextField fTelp    = new JTextField(15);
                JTextField fJabatan = new JTextField(20);
 
                JPanel form = new JPanel(new java.awt.GridLayout(0, 2, 6, 8));
                form.add(new JLabel("NIDN *:")); form.add(fNidn);
                form.add(new JLabel("Nama Dosen *:")); form.add(fNama);
                form.add(new JLabel("Jenis Kelamin:")); form.add(fJK);
                form.add(new JLabel("Email:")); form.add(fEmail);
                form.add(new JLabel("No. Telp:")); form.add(fTelp);
                form.add(new JLabel("Jabatan:")); form.add(fJabatan);
                form.add(new JLabel("")); form.add(new JLabel("<html><i style='color:#888'>Password awal = NIDN</i></html>"));
 
                int res = JOptionPane.showConfirmDialog(panel, form, "Tambah Dosen Baru",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    if (fNidn.getText().trim().isEmpty() || fNama.getText().trim().isEmpty()) {
                        JOptionPane.showMessageDialog(panel, "NIDN dan Nama wajib diisi!", "Peringatan",
                                JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    Dosen d = new Dosen();
                    d.setNidn(fNidn.getText().trim());
                    d.setNamaDosen(fNama.getText().trim());
                    d.setJenisKelamin((String) fJK.getSelectedItem());
                    d.setEmail(fEmail.getText().trim());
                    d.setNoTelp(fTelp.getText().trim());
                    d.setJabatan(fJabatan.getText().trim());
 
                    if (dao.tambahDosen(d)) {
                        JOptionPane.showMessageDialog(panel,
                            "Dosen berhasil ditambahkan!\nPassword awal: " + d.getNidn(),
                            "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        loadSemua.run();
                    } else {
                        JOptionPane.showMessageDialog(panel,
                            "Gagal menambahkan dosen. NIDN mungkin sudah terdaftar.",
                            "Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
 
            // ── Edit Dosen ───────────────────────────────────────────────
            btnEdit.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(panel, "Pilih dosen yang ingin diedit!", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String nidn = model.getValueAt(row, 0).toString();
                Dosen target = dao.getAllDosen().stream()
                        .filter(d -> d.getNidn().equals(nidn)).findFirst().orElse(null);
                if (target == null) return;
 
                JTextField fNama    = new JTextField(target.getNamaDosen(), 20);
                JComboBox<String> fJK = new JComboBox<>(new String[]{"L", "P"});
                fJK.setSelectedItem(target.getJenisKelamin());
                JTextField fEmail   = new JTextField(target.getEmail() != null ? target.getEmail() : "", 20);
                JTextField fTelp    = new JTextField(target.getNoTelp() != null ? target.getNoTelp() : "", 15);
                JTextField fJabatan = new JTextField(target.getJabatan() != null ? target.getJabatan() : "", 20);
 
                JPanel form = new JPanel(new java.awt.GridLayout(0, 2, 6, 8));
                form.add(new JLabel("NIDN (tidak bisa diubah):")); form.add(new JLabel(nidn));
                form.add(new JLabel("Nama Dosen *:")); form.add(fNama);
                form.add(new JLabel("Jenis Kelamin:")); form.add(fJK);
                form.add(new JLabel("Email:")); form.add(fEmail);
                form.add(new JLabel("No. Telp:")); form.add(fTelp);
                form.add(new JLabel("Jabatan:")); form.add(fJabatan);
 
                int res = JOptionPane.showConfirmDialog(panel, form, "Edit Data Dosen",
                        JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (res == JOptionPane.OK_OPTION) {
                    target.setNamaDosen(fNama.getText().trim());
                    target.setJenisKelamin((String) fJK.getSelectedItem());
                    target.setEmail(fEmail.getText().trim());
                    target.setNoTelp(fTelp.getText().trim());
                    target.setJabatan(fJabatan.getText().trim());
 
                    if (dao.updateDosen(target)) {
                        JOptionPane.showMessageDialog(panel, "Data dosen berhasil diperbarui.",
                                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        loadSemua.run();
                    } else {
                        JOptionPane.showMessageDialog(panel, "Gagal memperbarui data.",
                                "Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
 
            // ── Hapus Dosen ──────────────────────────────────────────────
            btnHapus.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(panel, "Pilih dosen yang ingin dihapus!", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String nidn = model.getValueAt(row, 0).toString();
                String nama = model.getValueAt(row, 1).toString();
                int conf = JOptionPane.showConfirmDialog(panel,
                        "Hapus dosen " + nama + " (" + nidn + ")?\n" +
                        "⚠ Pastikan dosen tidak memiliki kelas atau data terkait.",
                        "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (conf == JOptionPane.YES_OPTION) {
                    if (dao.hapusDosen(nidn)) {
                        JOptionPane.showMessageDialog(panel, "Dosen berhasil dihapus.",
                                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                        loadSemua.run();
                    } else {
                        JOptionPane.showMessageDialog(panel,
                                "Gagal menghapus. Dosen mungkin masih memiliki kelas atau data terkait.",
                                "Gagal", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
 
            // ── Reset Password ───────────────────────────────────────────
            btnReset.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    JOptionPane.showMessageDialog(panel, "Pilih dosen untuk direset passwordnya!", "Info",
                            JOptionPane.INFORMATION_MESSAGE);
                    return;
                }
                String nidn = model.getValueAt(row, 0).toString();
                String nama = model.getValueAt(row, 1).toString();
                int conf = JOptionPane.showConfirmDialog(panel,
                        "Reset password " + nama + "?\nPassword akan dikembalikan ke NIDN: " + nidn,
                        "Konfirmasi Reset", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (conf == JOptionPane.YES_OPTION) {
                    if (dao.resetPasswordDosen(nidn)) {
                        JOptionPane.showMessageDialog(panel,
                                "Password berhasil direset ke NIDN.\nBeritahu dosen untuk segera ganti password.",
                                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(panel, "Gagal reset password.", "Gagal",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
 
            return panel;
        }


        private JPanel buatPanelKrsAdmin() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JPanel top = buatPanelTop("Kelola KRS Mahasiswa");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
            JTextField txtNim = buatTextField(12);
            JButton btnCari = buatTombol("Cari NIM", BLUE);
            JButton btnSemua = buatTombol("Semua KRS", GREEN_PRIMARY);
            JButton btnSetujui = buatTombol("✔  Setujui", GREEN_PRIMARY);
            JButton btnTolak = buatTombol("Tolak", ORANGE);
            JButton btnHapus = buatTombol("✕  Hapus", RED);
            tools.add(new JLabel("NIM:"));
            tools.add(txtNim);
            tools.add(btnCari);
            tools.add(btnSemua);
            tools.add(Box.createHorizontalStrut(8));
            tools.add(btnSetujui);
            tools.add(btnTolak);
            tools.add(btnHapus);
            top.add(tools, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);

            String[] kolom = { "ID KRS", "Mahasiswa", "Mata Kuliah", "SKS", "Dosen", "Kelas", "TA", "Semester",
                    "Status" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            tabel.setDefaultRenderer(Object.class, statusRenderer(model, 8));
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            JLabel lblInfo = buatLabelInfo("Klik 'Semua KRS' untuk tampilkan seluruh data.");
            panel.add(lblInfo, BorderLayout.SOUTH);

            KrsDAO dao = new KrsDAO();
            Runnable loadSemua = () -> {
                model.setRowCount(0);
                for (KRS k : dao.getAllKrs())
                    model.addRow(new Object[] { k.getIdKrs(), k.getNamaMahasiswa(), k.getNamaMataKuliah(),
                            k.getJumlahSks(), k.getNamaDosen(), k.getNamaKelas(), k.getTahunAkademik(),
                            k.getSemesterTA(), k.getStatusKrs() });
                lblInfo.setText("  Total: " + model.getRowCount() + " KRS");
            };
            btnSemua.addActionListener(e -> loadSemua.run());
            btnCari.addActionListener(e -> {
                String nim = txtNim.getText().trim();
                if (nim.isEmpty())
                    return;
                model.setRowCount(0);
                List<KRS> list = dao.getKrsAktifByNim(nim);
                for (KRS k : list)
                    model.addRow(new Object[] { k.getIdKrs(), k.getNamaMahasiswa(), k.getNamaMataKuliah(),
                            k.getJumlahSks(), k.getNamaDosen(), k.getNamaKelas(), k.getTahunAkademik(),
                            k.getSemesterTA(), k.getStatusKrs() });
                lblInfo.setText("  KRS untuk NIM " + nim + ": " + list.size() + " data");
            });
            btnSetujui.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih KRS!");
                    return;
                }
                boolean ok = dao.updateStatusKrs((int) model.getValueAt(row, 0), "Disetujui");
                if (ok)
                    model.setValueAt("Disetujui", row, 8);
                showInfo(ok ? "KRS disetujui!" : "Gagal!", ok);
            });
            btnTolak.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih KRS!");
                    return;
                }
                boolean ok = dao.updateStatusKrs((int) model.getValueAt(row, 0), "Ditolak");
                if (ok)
                    model.setValueAt("Ditolak", row, 8);
                showInfo(ok ? "KRS ditolak." : "Gagal!", ok);
            });
            btnHapus.addActionListener(e -> {
                int row = tabel.getSelectedRow();
                if (row < 0) {
                    showWarning("Pilih KRS!");
                    return;
                }
                if (JOptionPane.showConfirmDialog(panel,
                        "Yakin hapus KRS ini? Data presensi & nilai terkait juga dihapus!",
                        "Konfirmasi", JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
                    boolean ok = dao.hapusKrs((int) model.getValueAt(row, 0));
                    if (ok) {
                        model.removeRow(row);
                        showInfo("KRS dihapus!", true);
                    } else
                        showInfo("Gagal!", false);
                }
            });
            return panel;
        }

        private JPanel buatPanelJadwalAdmin() {
            JPanel panel = new JPanel(new BorderLayout(10, 10));
            panel.setBackground(BG);
            panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

            JPanel top = buatPanelTop("Jadwal Kuliah — Semester Aktif");
            JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
            tools.setOpaque(false);
            JComboBox<String> cbHari = new JComboBox<>(
                    new String[] { "Semua Hari", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu" });
            cbHari.setFont(fPlain(12));
            JButton btnFilter = buatTombol("Filter", BLUE);
            JButton btnRefresh = buatTombol("↻  Semua", GREEN_PRIMARY);
            JButton btnUjian = buatTombol("Jadwal UTS/UAS", ORANGE);
            tools.add(cbHari);
            tools.add(btnFilter);
            tools.add(btnRefresh);
            tools.add(btnUjian);
            top.add(tools, BorderLayout.EAST);
            panel.add(top, BorderLayout.NORTH);

            String[] kolom = { "Mata Kuliah", "Dosen", "Ruangan", "Hari", "Jam Mulai", "Jam Selesai", "TA",
                    "Semester" };
            DefaultTableModel model = new DefaultTableModel(kolom, 0) {
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };
            JTable tabel = buatTabel(model);
            panel.add(scrollOf(tabel), BorderLayout.CENTER);

            JadwalDAO dao = new JadwalDAO();
            Runnable loadAll = () -> {
                model.setRowCount(0);
                for (Jadwal j : dao.getAllJadwalAktif())
                    model.addRow(new Object[] { j.getNamaMataKuliah(), j.getNamaDosen(), j.getNamaRuangan(),
                            j.getHari(), j.getJamMulai(), j.getJamSelesai(), j.getTahunAkademik(), j.getSemester() });
            };
            loadAll.run();
            btnRefresh.addActionListener(e -> loadAll.run());
            btnFilter.addActionListener(e -> {
                String hari = (String) cbHari.getSelectedItem();
                if ("Semua Hari".equals(hari)) {
                    loadAll.run();
                    return;
                }
                model.setRowCount(0);
                for (Jadwal j : dao.getJadwalByHari(hari))
                    model.addRow(new Object[] { j.getNamaMataKuliah(), j.getNamaDosen(), j.getNamaRuangan(),
                            j.getHari(), j.getJamMulai(), j.getJamSelesai(), "", "" });
            });
            btnUjian.addActionListener(e -> {
                String[] jenis = { "UTS", "UAS" };
                String pilihan = (String) JOptionPane.showInputDialog(panel, "Pilih jenis ujian:", "Jadwal Ujian",
                        JOptionPane.QUESTION_MESSAGE, null, jenis, jenis[0]);
                if (pilihan == null)
                    return;
                JDialog d = new JDialog();
                d.setTitle("Jadwal " + pilihan);
                d.setSize(820, 420);
                d.setLocationRelativeTo(panel);
                String[] kolUjian = { "Mata Kuliah", "Dosen", "Jenis", "Tanggal", "Jam Mulai", "Jam Selesai", "Ruangan",
                        "Pengawas" };
                DefaultTableModel mU = new DefaultTableModel(kolUjian, 0);
                for (Jadwal j : dao.getJadwalUjian(pilihan))
                    mU.addRow(new Object[] { j.getNamaMataKuliah(), j.getNamaDosen(), j.getJenisUjian(),
                            j.getTanggal(), j.getJamMulai(), j.getJamSelesai(), j.getNamaRuangan(), j.getPengawas() });
                d.add(scrollOf(buatTabel(mU)));
                d.setVisible(true);
            });
            return panel;
        }
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PANEL NILAI — shared
    // ═══════════════════════════════════════════════════════════════════════
    static JPanel buatPanelNilai(String nimTetap) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel top = buatPanelTop(nimTetap != null ? "Nilai Akademik Saya" : "Nilai Mahasiswa");
        JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tools.setOpaque(false);
        JTextField txtNim = buatTextField(14);
        if (nimTetap != null) {
            txtNim.setText(nimTetap);
            txtNim.setEditable(false);
        }
        JButton btnLihat = buatTombol("Lihat Nilai", BLUE);
        JButton btnIpk = buatTombol("Hitung IPK", GREEN_PRIMARY);
        if (nimTetap == null) {
            tools.add(new JLabel("NIM:"));
            tools.add(txtNim);
        }
        tools.add(btnLihat);
        tools.add(btnIpk);
        top.add(tools, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        String[] kolom = { "Mata Kuliah", "SKS", "Tugas", "UTS", "UAS", "Nilai Angka", "Nilai Huruf", "Bobot", "Final",
                "TA", "Semester" };
        DefaultTableModel model = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabel = buatTabel(model);
        tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel && col == 6) {
                    String h = val != null ? val.toString() : "";
                    if ("A".equals(h))
                        c.setBackground(new Color(209, 240, 220));
                    else if ("B".equals(h))
                        c.setBackground(new Color(230, 247, 210));
                    else if ("C".equals(h))
                        c.setBackground(new Color(255, 252, 210));
                    else if ("D".equals(h))
                        c.setBackground(new Color(255, 235, 205));
                    else if ("E".equals(h))
                        c.setBackground(new Color(255, 215, 215));
                    else
                        c.setBackground(row % 2 == 0 ? WHITE : new Color(245, 250, 247));
                } else if (!sel)
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(245, 250, 247));
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        panel.add(scrollOf(tabel), BorderLayout.CENTER);

        JPanel ipkBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 8));
        ipkBar.setBackground(GREEN_LIGHT);
        ipkBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER));
        JLabel lblIpk = new JLabel("IPK: —");
        lblIpk.setFont(fBold(14));
        lblIpk.setForeground(GREEN_PRIMARY);
        JLabel lblLeg = new JLabel("  Warna nilai: A=Hijau  B=Hijau muda  C=Kuning  D=Oranye  E=Merah");
        lblLeg.setFont(fPlain(11));
        lblLeg.setForeground(TEXT_SECONDARY);
        ipkBar.add(lblIpk);
        ipkBar.add(lblLeg);
        panel.add(ipkBar, BorderLayout.SOUTH);

        NilaiDAO dao = new NilaiDAO();
        Runnable load = () -> {
            String nim = txtNim.getText().trim();
            if (nim.isEmpty())
                return;
            model.setRowCount(0);
            for (Nilai n : dao.getNilaiByNim(nim))
                model.addRow(new Object[] { n.getNamaMataKuliah(), n.getJumlahSks(),
                        n.getNilaiTugas(), n.getNilaiUts(), n.getNilaiUas(),
                        String.format("%.2f", n.getNilaiAngka()), n.getNilaiHuruf(),
                        String.format("%.2f", n.getBobotNilai()), n.isIsFinal() ? "Ya" : "Tidak",
                        n.getTahunAkademik(), n.getSemester() });
        };
        btnLihat.addActionListener(e -> load.run());
        btnIpk.addActionListener(e -> {
            String nim = txtNim.getText().trim();
            if (nim.isEmpty())
                return;
            double ipk = dao.hitungIpk(nim);
            lblIpk.setText(String.format("IPK: %.2f / 4.00", ipk));
            load.run();
        });
        if (nimTetap != null)
            load.run();
        return panel;
    }

    // ═══════════════════════════════════════════════════════════════════════
    // PANEL PRESENSI — shared
    // ═══════════════════════════════════════════════════════════════════════
    static JPanel buatPanelPresensi(String nimTetap) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));

        JPanel top = buatPanelTop(nimTetap != null ? "Rekap Presensi Saya" : "Rekap Presensi Mahasiswa");
        JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        tools.setOpaque(false);
        JTextField txtNim = buatTextField(14);
        if (nimTetap != null) {
            txtNim.setText(nimTetap);
            txtNim.setEditable(false);
        }
        JButton btnRekap = buatTombol("Lihat Rekap", BLUE);
        JButton btnWarning = buatTombol("Bawah 75%", RED);
        JButton btnInput = buatTombol("Input Presensi", ORANGE);

        if (nimTetap == null) {
            tools.add(new JLabel("NIM:"));
            tools.add(txtNim);
            tools.add(btnWarning);
            tools.add(btnInput);
        }
        tools.add(btnRekap);
        top.add(tools, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        String[] kolom = { "Mata Kuliah", "TA", "Semester", "Total", "Hadir", "Izin", "Sakit", "Alpha", "% Hadir" };
        DefaultTableModel model = new DefaultTableModel(kolom, 0) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        JTable tabel = buatTabel(model);
        tabel.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel && col == 8) {
                    try {
                        double pct = Double.parseDouble(val.toString().replace("%", "").trim());
                        c.setBackground(pct < 75 ? new Color(255, 215, 215) : new Color(209, 240, 220));
                    } catch (Exception ignored) {
                    }
                } else if (!sel)
                    c.setBackground(row % 2 == 0 ? WHITE : new Color(245, 250, 247));
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        });
        panel.add(scrollOf(tabel), BorderLayout.CENTER);
        panel.add(buatLabelInfo("Merah = kehadiran < 75% (tidak dapat mengikuti ujian)"), BorderLayout.SOUTH);

        PresensiDAO dao = new PresensiDAO();
        Runnable loadRekap = () -> {
            model.setRowCount(0);
            try {
                ResultSet rs = dao.getRekapPresensi(txtNim.getText().trim());
                while (rs.next())
                    model.addRow(new Object[] {
                            rs.getString("nama_mahasiswa") + " — " + rs.getString("nama_mata_kuliah"),
                            rs.getString("tahun_akademik"), rs.getString("semester"),
                            rs.getInt("total_pertemuan"), rs.getInt("hadir"),
                            rs.getInt("izin"), rs.getInt("sakit"), rs.getInt("alpha"),
                            String.format("%.1f%%", rs.getDouble("persen_kehadiran")) });
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        };
        btnRekap.addActionListener(e -> loadRekap.run());
        if (nimTetap != null)
            loadRekap.run();
        if (nimTetap == null) {
            btnWarning.addActionListener(e -> {
                model.setRowCount(0);
                try {
                    ResultSet rs = dao.getMahasiswaBawah75Persen();
                    while (rs.next())
                        model.addRow(new Object[] {
                                rs.getString("nama_mahasiswa") + " — " + rs.getString("nama_mata_kuliah"),
                                "-", "-", "-", "-", "-", "-", "-",
                                String.format("%.1f%%", rs.getDouble("persen_kehadiran")) });
                    if (model.getRowCount() == 0)
                        JOptionPane.showMessageDialog(panel, "Semua mahasiswa memenuhi batas 75%! ✔", "Info",
                                JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            });
            btnInput.addActionListener(e -> bukaFormInputPresensi(dao, panel));
        }
        return panel;
    }

    static void bukaFormInputPresensi(PresensiDAO dao, Component parent) {
        JDialog dialog = new JDialog();
        dialog.setTitle("Input Presensi Manual");
        dialog.setSize(420, 340);
        dialog.setLocationRelativeTo(parent);
        dialog.setModal(true);
        dialog.getContentPane().setBackground(WHITE);
        dialog.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 14, 8, 14);

        JTextField txtIdKrs = buatTextField(14);
        JTextField txtIdJadwal = buatTextField(14);
        JTextField txtTanggal = buatTextField(14);
        txtTanggal.setText("2025-09-16");
        JComboBox<String> cbStatus = new JComboBox<>(new String[] { "Hadir", "Izin", "Sakit", "Alpha" });
        cbStatus.setFont(fPlain(12));
        JTextField txtKet = buatTextField(14);

        String[] labels = { "ID KRS:", "ID Jadwal:", "Tanggal (yyyy-MM-dd):", "Status:", "Keterangan (opsional):" };
        JComponent[] inputs = { txtIdKrs, txtIdJadwal, txtTanggal, cbStatus, txtKet };
        for (int i = 0; i < labels.length; i++) {
            JLabel l = new JLabel(labels[i]);
            l.setFont(fBold(12));
            l.setForeground(TEXT_PRIMARY);
            gbc.gridx = 0;
            gbc.gridy = i;
            gbc.weightx = 0.4;
            dialog.add(l, gbc);
            inputs[i].setFont(fPlain(12));
            gbc.gridx = 1;
            gbc.weightx = 0.6;
            dialog.add(inputs[i], gbc);
        }
        JButton btnSimpan = buatTombol("Simpan Presensi", GREEN_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = labels.length;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(16, 14, 12, 14);
        dialog.add(btnSimpan, gbc);
        btnSimpan.addActionListener(e -> {
            try {
                Presensi p = new Presensi();
                p.setIdKrs(Integer.parseInt(txtIdKrs.getText().trim()));
                p.setIdJadwal(Integer.parseInt(txtIdJadwal.getText().trim()));
                p.setTanggal(java.sql.Date.valueOf(txtTanggal.getText().trim()));
                p.setStatus((String) cbStatus.getSelectedItem());
                p.setKeterangan(txtKet.getText().trim());
                boolean ok = dao.tambahPresensi(p);
                showInfo(ok ? "Presensi berhasil dicatat!" : "Gagal atau duplikat!", ok, dialog);
                if (ok)
                    dialog.dispose();
            } catch (Exception ex) {
                showWarning("Format data tidak valid! Periksa kembali.");
            }
        });
        dialog.setVisible(true);
    }

    // ═══════════════════════════════════════════════════════════════════════
    // HELPER BUILDERS
    // ═══════════════════════════════════════════════════════════════════════

    /** Panel atas section dengan judul menonjol bergaris bawah hijau */
    static JPanel buatPanelTop(String judul) {
        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        JLabel lbl = new JLabel(judul);
        lbl.setFont(fBold(15));
        lbl.setForeground(TEXT_PRIMARY);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        top.add(lbl, BorderLayout.WEST);
        return top;
    }

    /** Kartu info (label: value berpasangan) */
    static JPanel buatKartuInfo(String judul, Color accent, String... pairs) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));

        JLabel lbl = new JLabel(judul);
        lbl.setFont(fBold(13));
        lbl.setForeground(accent);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        card.add(lbl, BorderLayout.NORTH);

        JPanel body = new JPanel(new GridLayout(pairs.length / 2, 2, 4, 6));
        body.setOpaque(false);
        for (int i = 0; i < pairs.length; i += 2) {
            JLabel k = new JLabel(pairs[i] + ":");
            k.setFont(fBold(11));
            k.setForeground(TEXT_SECONDARY);
            JLabel v = new JLabel(pairs[i + 1]);
            v.setFont(fPlain(12));
            v.setForeground(TEXT_PRIMARY);
            body.add(k);
            body.add(v);
        }
        card.add(body, BorderLayout.CENTER);
        return card;
    }

    /** Kartu statistik berwarna solid */
    static JPanel buatKartuStat(String judul, String nilai, Color warna, String sub) {
        JPanel c = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(warna);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
            }
        };
        c.setOpaque(false);
        c.setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        JLabel lbl = new JLabel(judul);
        lbl.setFont(fBold(12));
        lbl.setForeground(new Color(255, 255, 255, 180));
        JLabel val = new JLabel(nilai, SwingConstants.CENTER);
        val.setFont(fBold(38));
        val.setForeground(WHITE);
        JLabel s = new JLabel(sub, SwingConstants.CENTER);
        s.setFont(fPlain(11));
        s.setForeground(new Color(255, 255, 255, 170));
        c.add(lbl, BorderLayout.NORTH);
        c.add(val, BorderLayout.CENTER);
        c.add(s, BorderLayout.SOUTH);
        return c;
    }

    /** Kartu ringkasan teks dengan border kiri berwarna */
    static JPanel buatKartuRingkasan(String judul, Color accent, String isi) {
        JPanel c = new JPanel(new BorderLayout(0, 8));
        c.setBackground(WHITE);
        c.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 4, 0, 0, accent),
                BorderFactory.createEmptyBorder(14, 16, 14, 16)));
        JLabel lbl = new JLabel(judul);
        lbl.setFont(fBold(13));
        lbl.setForeground(accent);
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        JLabel body = new JLabel("<html>" + isi.replace("\n", "<br>") + "</html>");
        body.setFont(fPlain(12));
        body.setForeground(TEXT_SECONDARY);
        c.add(lbl, BorderLayout.NORTH);
        c.add(body, BorderLayout.CENTER);
        return c;
    }

    /** Renderer baris warna status KRS */
    static DefaultTableCellRenderer statusRenderer(DefaultTableModel model, int statusCol) {
        return new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable t, Object val, boolean sel, boolean foc, int row,
                    int col) {
                Component c = super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                if (!sel) {
                    String st = (String) model.getValueAt(t.convertRowIndexToModel(row), statusCol);
                    if ("Disetujui".equals(st))
                        c.setBackground(new Color(220, 247, 230));
                    else if ("Ditolak".equals(st))
                        c.setBackground(new Color(255, 224, 224));
                    else
                        c.setBackground(new Color(255, 252, 215));
                }
                ((JLabel) c).setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                return c;
            }
        };
    }

    static JTextField buatTextField(int cols) {
        JTextField f = new JTextField(cols);
        f.setFont(fPlain(12));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(4, 10, 4, 10)));
        return f;
    }

    static JLabel buatBadge(String teks, Color bg, Color fg) {
        JLabel l = new JLabel("  " + teks + "  ");
        l.setFont(fPlain(11));
        l.setBackground(bg);
        l.setForeground(fg);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createLineBorder(bg.darker(), 1));
        return l;
    }

    static JLabel buatBadgeGray(String teks) {
        JLabel l = new JLabel("  " + teks + "  ");
        l.setFont(fPlain(11));
        l.setForeground(TEXT_SECONDARY);
        l.setBackground(new Color(235, 240, 237));
        l.setOpaque(true);
        l.setBorder(BorderFactory.createLineBorder(BORDER));
        return l;
    }

    static JLabel buatBadgeGreen(String teks) {
        JLabel l = new JLabel("  " + teks + "  ");
        l.setFont(fBold(11));
        l.setForeground(GREEN_PRIMARY);
        l.setBackground(GREEN_LIGHT);
        l.setOpaque(true);
        l.setBorder(BorderFactory.createLineBorder(GREEN_MEDIUM));
        l.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return l;
    }
}