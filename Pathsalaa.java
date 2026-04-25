import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.awt.Desktop;

// ================= MAIN =================
public class Pathsalaa {
    public static void main(String[] args) {
        new SplashScreen();
    }
}

// ================= SPLASH =================
class SplashScreen {
    SplashScreen() {
        JFrame f = new JFrame();
        f.setSize(400, 200);
        f.setUndecorated(true);
        f.getContentPane().setBackground(new Color(135, 206, 235));

        JLabel l = new JLabel("Welcome to Pathsalaa", JLabel.CENTER);
        l.setFont(new Font("Arial", Font.BOLD, 22));
        f.add(l);

        f.setLocationRelativeTo(null);
        f.setVisible(true);

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
        }

        f.dispose();
        new LoginPage();
    }
}

// ================= LOGIN (PRO UI) =================
class LoginPage {
    LoginPage() {
        JFrame f = new JFrame("Pathsalaa");
        f.setSize(450, 400);
        f.setLocationRelativeTo(null);

        JPanel main = new JPanel(new GridBagLayout());
        main.setBackground(new Color(135, 206, 235));

        JPanel card = new JPanel(new GridBagLayout());
        card.setPreferredSize(new Dimension(300, 280));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Pathsalaa", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));

        JLabel sub = new JLabel("Learning Made Easy", JLabel.CENTER);

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();

        JButton login = new JButton("Login");
        JButton register = new JButton("Register");

        login.setBackground(new Color(70, 130, 180));
        login.setForeground(Color.WHITE);
        register.setBackground(new Color(100, 149, 237));
        register.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        card.add(title, gbc);
        gbc.gridy = 1;
        card.add(sub, gbc);

        gbc.gridy = 2;
        gbc.gridwidth = 1;
        card.add(new JLabel("Username"), gbc);
        gbc.gridx = 1;
        card.add(user, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        card.add(new JLabel("Password"), gbc);
        gbc.gridx = 1;
        card.add(pass, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        card.add(login, gbc);
        gbc.gridx = 1;
        card.add(register, gbc);

        main.add(card);
        f.add(main);

        login.addActionListener(e -> {
            try {
                BufferedReader br = new BufferedReader(new FileReader("users.txt"));
                String line;
                boolean found = false;
                String role = "";
                while ((line = br.readLine()) != null) {
                    String[] d = line.split(",");
                    if (d[0].equals(user.getText()) && d[1].equals(new String(pass.getPassword()))) {
                        found = true;
                        role = d[2];
                        break;
                    }
                }
                br.close();

                if (found) {
                    f.dispose();
                    if (role.equals("Teacher"))
                        new TeacherDashboard(user.getText());
                    else
                        new StudentDashboard(user.getText());
                } else
                    JOptionPane.showMessageDialog(f, "Invalid Login");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error");
            }
        });

        register.addActionListener(e -> new Register());

        f.setVisible(true);
    }
}

// ================= REGISTER =================
class Register {
    Register() {
        JFrame f = new JFrame("Register");
        f.setSize(350, 250);
        f.setLayout(new GridLayout(5, 2, 10, 10));
        f.getContentPane().setBackground(new Color(135, 206, 235));

        JTextField user = new JTextField();
        JPasswordField pass = new JPasswordField();
        JComboBox<String> role = new JComboBox<>(new String[] { "Student", "Teacher" });

        JButton btn = new JButton("Register");

        f.add(new JLabel("Username"));
        f.add(user);
        f.add(new JLabel("Password"));
        f.add(pass);
        f.add(new JLabel("Role"));
        f.add(role);
        f.add(new JLabel());
        f.add(btn);

        btn.addActionListener(e -> {
            try (FileWriter fw = new FileWriter("users.txt", true)) {
                fw.write(user.getText() + "," + new String(pass.getPassword()) + "," + role.getSelectedItem() + "\n");
                JOptionPane.showMessageDialog(f, "Registered!");
                f.dispose();
            } catch (Exception ex) {
            }
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

// ================= TEACHER =================
class TeacherDashboard {
    TeacherDashboard(String user) {
        JFrame f = new JFrame("Teacher");
        f.setSize(500, 400);
        f.setLayout(new GridLayout(5, 1, 10, 10));
        f.getContentPane().setBackground(new Color(135, 206, 235));

        JComboBox<String> sub = new JComboBox<>(new String[] { "EDS", "Discrete", "Java", "EVS", "Economics" });
        JButton upload = new JButton("Upload File");
        JButton doubts = new JButton("View Doubts");
        JButton logout = new JButton("Logout");

        f.add(new JLabel("Welcome " + user));
        f.add(sub);
        f.add(upload);
        f.add(doubts);
        f.add(logout);

        upload.addActionListener(e -> {
            JFileChooser ch = new JFileChooser();
            if (ch.showOpenDialog(f) == JFileChooser.APPROVE_OPTION) {
                File file = ch.getSelectedFile();
                try (FileWriter fw = new FileWriter("resources.txt", true)) {
                    fw.write(sub.getSelectedItem() + "," + file.getName() + "," + file.getAbsolutePath() + "\n");
                    JOptionPane.showMessageDialog(f, "Uploaded!");
                } catch (Exception ex) {
                }
            }
        });

        doubts.addActionListener(e -> new TeacherDoubtPanel());
        logout.addActionListener(e -> {
            f.dispose();
            new LoginPage();
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

// ================= STUDENT =================
class StudentDashboard {
    StudentDashboard(String user) {
        JFrame f = new JFrame("Student");
        f.setSize(500, 400);
        f.setLayout(new GridLayout(5, 1, 10, 10));
        f.getContentPane().setBackground(new Color(135, 206, 235));

        JComboBox<String> sub = new JComboBox<>(new String[] { "EDS", "Discrete", "Java", "EVS", "Economics" });
        JButton open = new JButton("Open Subject");
        JButton doubt = new JButton("Ask Doubt");
        JButton logout = new JButton("Logout");

        f.add(new JLabel("Welcome " + user));
        f.add(sub);
        f.add(open);
        f.add(doubt);
        f.add(logout);

        open.addActionListener(e -> new ResourcePage((String) sub.getSelectedItem()));
        doubt.addActionListener(e -> new DoubtPanel(user, (String) sub.getSelectedItem()));
        logout.addActionListener(e -> {
            f.dispose();
            new LoginPage();
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

// ================= RESOURCE =================
class ResourcePage {
    ResourcePage(String subject) {
        JFrame f = new JFrame(subject + " Resources");
        f.setSize(600, 400);
        f.setLayout(new BorderLayout());
        f.getContentPane().setBackground(new Color(135, 206, 235));

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);
        ArrayList<String> paths = new ArrayList<>();

        JPanel top = new JPanel();
        JButton video = new JButton("Videos");
        JButton notes = new JButton("Notes");
        JButton quiz = new JButton("Quiz");

        top.add(video);
        top.add(notes);
        top.add(quiz);

        f.add(top, BorderLayout.NORTH);
        f.add(new JScrollPane(list), BorderLayout.CENTER);

        ActionListener load = e -> {
            model.clear();
            paths.clear();
            String type = ((JButton) e.getSource()).getText();

            try (BufferedReader br = new BufferedReader(new FileReader("resources.txt"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] d = line.split(",", 3);
                    if (d[0].equals(subject)) {
                        String name = d[1].toLowerCase();
                        if (type.equals("Videos") && name.endsWith(".mp4")) {
                            model.addElement("🎥 " + d[1]);
                            paths.add(d[2]);
                        }
                        if (type.equals("Notes") && (name.endsWith(".pdf") || name.endsWith(".txt"))) {
                            model.addElement("📄 " + d[1]);
                            paths.add(d[2]);
                        }
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(f, "Error");
            }
        };

        video.addActionListener(load);
        notes.addActionListener(load);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = list.getSelectedIndex();
                if (i >= 0) {
                    try {
                        Desktop.getDesktop().open(new File(paths.get(i)));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(f, "Cannot open");
                    }
                }
            }
        });

        quiz.addActionListener(e -> new QuizPage(subject));

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

// ================= DOUBT =================
class DoubtPanel {
    DoubtPanel(String user, String sub) {
        JFrame f = new JFrame("Ask Doubt");
        f.setSize(400, 300);

        JTextArea area = new JTextArea();
        JButton send = new JButton("Submit");

        f.add(new JScrollPane(area));
        f.add(send, BorderLayout.SOUTH);

        send.addActionListener(e -> {
            try (FileWriter fw = new FileWriter("doubts.txt", true)) {
                fw.write(user + "," + sub + "," + area.getText() + "\n");
                JOptionPane.showMessageDialog(f, "Submitted!");
                f.dispose();
            } catch (Exception ex) {
            }
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

// ================= TEACHER DOUBTS =================
class TeacherDoubtPanel {
    TeacherDoubtPanel() {
        JFrame f = new JFrame("Doubts");
        f.setSize(500, 400);

        DefaultListModel<String> model = new DefaultListModel<>();
        JList<String> list = new JList<>(model);

        try (BufferedReader br = new BufferedReader(new FileReader("doubts.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                model.addElement(line);
            }
        } catch (Exception ex) {
        }

        f.add(new JScrollPane(list));
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}

// ================= QUIZ =================
class QuizPage {
    QuizPage(String subject) {
        JFrame f = new JFrame("Quiz - " + subject);
        f.setSize(400, 300);
        f.setLayout(new GridLayout(5, 1));

        JLabel q = new JLabel("Java: JVM stands for?");
        JRadioButton a = new JRadioButton("Java Virtual Machine");
        JRadioButton b = new JRadioButton("Java Variable Method");

        ButtonGroup bg = new ButtonGroup();
        bg.add(a);
        bg.add(b);

        JButton submit = new JButton("Submit");

        f.add(q);
        f.add(a);
        f.add(b);
        f.add(submit);

        submit.addActionListener(e -> {
            if (a.isSelected())
                JOptionPane.showMessageDialog(f, "Correct!");
            else
                JOptionPane.showMessageDialog(f, "Wrong!");
        });

        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}