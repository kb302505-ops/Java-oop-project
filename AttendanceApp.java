import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

/* =========================================================
   UI THEME - colorful gradients, rounded cards, flower accents
   ========================================================= */
class Theme {
    static final Color PURPLE = new Color(124, 77, 208);
    static final Color PINK = new Color(236, 84, 150);
    static final Color BLUE = new Color(59, 130, 220);
    static final Color TEAL = new Color(20, 170, 150);
    static final Color GREEN = new Color(46, 175, 110);
    static final Color ORANGE = new Color(240, 150, 45);
    static final Color RED = new Color(224, 80, 80);
    static final Color YELLOW = new Color(240, 190, 40);

    static final Color BG = new Color(247, 244, 250);
    static final Color CARD_BG = Color.WHITE;
    static final Color BORDER = new Color(230, 224, 240);
    static final Color TEXT = new Color(35, 32, 48);
    static final Color MUTED = new Color(135, 128, 150);
    static final Color ROW_ALT = new Color(250, 246, 252);

    static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 24);
    static final Font FONT_SUBTITLE = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FONT_HEADING = new Font("SansSerif", Font.BOLD, 16);
    static final Font FONT_LABEL = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FONT_FIELD = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FONT_BUTTON = new Font("SansSerif", Font.BOLD, 13);
    static final Font FONT_TABLE = new Font("SansSerif", Font.PLAIN, 13);
    static final Font FONT_TABLE_HEADER = new Font("SansSerif", Font.BOLD, 13);

    static JButton pillButton(String text, Color bg) {
        JButton b = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(FONT_BUTTON);
        b.setForeground(Color.WHITE);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder(10, 22, 10, 22));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Color base = bg, hover = bg.darker();
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { b.setBackground(hover); }
            public void mouseExited(MouseEvent e) { b.setBackground(base); }
        });
        return b;
    }

    static JTextField field() {
        JTextField f = new JTextField(16);
        f.setFont(FONT_FIELD);
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER, 1, true),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)));
        return f;
    }

    static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT);
        return l;
    }

    static RoundedPanel card() {
        RoundedPanel p = new RoundedPanel(18);
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createEmptyBorder(20, 22, 20, 22));
        return p;
    }

    static void styleTable(JTable table) {
        table.setFont(FONT_TABLE);
        table.setRowHeight(32);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(233, 220, 250));
        table.setSelectionForeground(TEXT);
        table.setFillsViewportHeight(true);
        table.getTableHeader().setFont(FONT_TABLE_HEADER);
        table.getTableHeader().setBackground(PURPLE);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setPreferredSize(new Dimension(100, 38));
        table.setBackground(Color.WHITE);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                             boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                return c;
            }
        };
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }
    }

    /** Colorful renderer that colors the Status column text (Present=green, Absent=red, Late=orange). */
    static void colorStatusColumn(JTable table, int statusColumnIndex) {
        table.getColumnModel().getColumn(statusColumnIndex).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value, boolean isSelected,
                                                             boolean hasFocus, int row, int column) {
                JLabel c = (JLabel) super.getTableCellRendererComponent(t, value, isSelected, hasFocus, row, column);
                c.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                if (!isSelected) c.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                String v = String.valueOf(value);
                c.setFont(new Font("SansSerif", Font.BOLD, 13));
                if (v.equals("Present")) c.setForeground(GREEN);
                else if (v.equals("Absent")) c.setForeground(RED);
                else if (v.equals("Late")) c.setForeground(ORANGE);
                else c.setForeground(TEXT);
                return c;
            }
        });
    }

    /** Gradient header bar (purple -> pink) with a flower/icon badge and decorative petals. */
    static JPanel headerBar(String icon, String titleText, String subtitleText) {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, PURPLE, getWidth(), getHeight(), PINK);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());

                // decorative translucent flower-like circles (petals) in the corner
                g2.setColor(new Color(255, 255, 255, 35));
                int cx = getWidth() - 70, cy = getHeight() / 2;
                int r = 16;
                double[] angles = {0, 60, 120, 180, 240, 300};
                for (double a : angles) {
                    int dx = (int) (Math.cos(Math.toRadians(a)) * 22);
                    int dy = (int) (Math.sin(Math.toRadians(a)) * 22);
                    g2.fillOval(cx + dx - r / 2, cy + dy - r / 2, r, r);
                }
                g2.setColor(new Color(255, 255, 255, 60));
                g2.fillOval(cx - 7, cy - 7, 14, 14);
                g2.dispose();
            }
        };
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(18, 26, 18, 26));

        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));

        JPanel titleRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        titleRow.setOpaque(false);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("SansSerif", Font.PLAIN, 26));
        JLabel title = new JLabel(titleText);
        title.setFont(FONT_TITLE);
        title.setForeground(Color.WHITE);
        titleRow.add(iconLabel);
        titleRow.add(title);
        left.add(titleRow);

        if (subtitleText != null) {
            JLabel subtitle = new JLabel(subtitleText);
            subtitle.setFont(FONT_SUBTITLE);
            subtitle.setForeground(new Color(245, 230, 250));
            subtitle.setBorder(BorderFactory.createEmptyBorder(4, 40, 0, 0));
            left.add(subtitle);
        }

        header.add(left, BorderLayout.WEST);
        return header;
    }
}

/** A JPanel with rounded corners and a subtle drop shadow, used for cards. */
class RoundedPanel extends JPanel {
    private final int radius;

    RoundedPanel(int radius) {
        this.radius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(0, 0, 0, 18));
        g2.fillRoundRect(3, 4, getWidth() - 6, getHeight() - 6, radius, radius);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 4, getHeight() - 4, radius, radius);
        g2.setColor(Theme.BORDER);
        g2.drawRoundRect(0, 0, getWidth() - 5, getHeight() - 5, radius, radius);
        g2.dispose();
        super.paintComponent(g);
    }
}

/* =========================================================
   CUSTOM DATE CLASS
   ========================================================= */
class MyDate {
    private int day, month, year;

    public MyDate(int day, int month, int year) {
        if (!isValidDate(day, month, year)) {
            throw new IllegalArgumentException("Invalid date: " + day + "-" + month + "-" + year);
        }
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public MyDate(LocalDate localDate) {
        this(localDate.getDayOfMonth(), localDate.getMonthValue(), localDate.getYear());
    }

    public static MyDate today() {
        return new MyDate(LocalDate.now());
    }

    public static MyDate fromUtilDate(Date utilDate) {
        LocalDate ld = utilDate.toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
        return new MyDate(ld);
    }

    public static MyDate fromIsoString(String isoText) {
        return new MyDate(LocalDate.parse(isoText));
    }

    public LocalDate toLocalDate() {
        return LocalDate.of(year, month, day);
    }

    public String toIsoString() {
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public static boolean isValidDate(int d, int m, int y) {
        if (y < 1900 || y > 2100) return false;
        if (m < 1 || m > 12) return false;
        int[] daysInMonth = {31, isLeapYear(y) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        return d >= 1 && d <= daysInMonth[m - 1];
    }

    public static boolean isLeapYear(int y) {
        return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0);
    }

    public int getDay() { return day; }
    public int getMonth() { return month; }
    public int getYear() { return year; }

    @Override
    public String toString() {
        return String.format("%02d-%02d-%04d", day, month, year);
    }
}

/* =========================================================
   ENUM CLASS
   ========================================================= */
enum AttendanceStatus {
    PRESENT("Present"),
    ABSENT("Absent"),
    LATE("Late");

    private final String label;

    AttendanceStatus(String label) { this.label = label; }

    public String getLabel() { return label; }

    public static AttendanceStatus fromString(String text) {
        for (AttendanceStatus s : values()) {
            if (s.name().equalsIgnoreCase(text)) return s;
        }
        throw new IllegalArgumentException("Unknown status: " + text);
    }

    @Override
    public String toString() { return label; }
}

/* =========================================================
   MODEL CLASSES
   ========================================================= */
class Student {
    private int studentId;
    private String name, email, phone, department;

    public Student() { }

    public Student(int studentId, String name, String email, String phone, String department) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() { return studentId + " - " + name; }
}

class Course {
    private int courseId;
    private String courseName;
    private double credit;

    public Course() { }

    public Course(int courseId, String courseName, double credit) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.credit = credit;
    }

    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public double getCredit() { return credit; }
    public void setCredit(double credit) { this.credit = credit; }

    @Override
    public String toString() { return courseId + " - " + courseName; }
}

/** LINK ENTITY: connects Student and Course, carries date + status */
class Attendance {
    private int attendanceId, studentId, courseId;
    private MyDate date;
    private AttendanceStatus status;
    private String studentName, courseName;

    public Attendance() { }

    public Attendance(int attendanceId, int studentId, int courseId, MyDate date, AttendanceStatus status) {
        this.attendanceId = attendanceId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.date = date;
        this.status = status;
    }

    public int getAttendanceId() { return attendanceId; }
    public void setAttendanceId(int attendanceId) { this.attendanceId = attendanceId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public MyDate getDate() { return date; }
    public void setDate(MyDate date) { this.date = date; }
    public AttendanceStatus getStatus() { return status; }
    public void setStatus(AttendanceStatus status) { this.status = status; }
    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
}

/* =========================================================
   JDBC CONNECTION (SQLite - file based, no server needed)
   ========================================================= */
class DBConnection {
    private static final String URL = "jdbc:sqlite:attendance.db";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException(
                    "SQLite JDBC Driver not found. Add sqlite-jdbc.jar to the project.",
                    e);
        }

        Connection con = DriverManager.getConnection(URL);
        initializeTables(con);
        return con;
    }

    private static void initializeTables(Connection con) throws SQLException {
        try (Statement st = con.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON");

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS student (" +
                    "student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT NOT NULL, " +
                    "email TEXT, " +
                    "phone TEXT, " +
                    "department TEXT)");

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS course (" +
                    "course_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "course_name TEXT NOT NULL, " +
                    "credit REAL)");

            st.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS attendance (" +
                    "attendance_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "student_id INTEGER NOT NULL, " +
                    "course_id INTEGER NOT NULL, " +
                    "attendance_date TEXT NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "FOREIGN KEY (student_id) REFERENCES student(student_id) ON DELETE CASCADE, " +
                    "FOREIGN KEY (course_id) REFERENCES course(course_id) ON DELETE CASCADE, " +
                    "UNIQUE (student_id, course_id, attendance_date))");
        }
    }
}

/* =========================================================
   DAO CLASSES (JDBC CRUD)
   ========================================================= */
class StudentDAO {
    public boolean addStudent(Student s) {
        String sql = "INSERT INTO student (name, email, phone, department) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getDepartment());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            showDatabaseError("Unable to add student", e);
            return false;
        }
    }

    public boolean updateStudent(Student s) {
        String sql = "UPDATE student SET name=?, email=?, phone=?, department=? WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setString(2, s.getEmail());
            ps.setString(3, s.getPhone());
            ps.setString(4, s.getDepartment());
            ps.setInt(5, s.getStudentId());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            showDatabaseError("Unable to update student", e);
            return false;
        }
    }

    public boolean deleteStudent(int studentId) {
        String sql = "DELETE FROM student WHERE student_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, studentId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            showDatabaseError("Unable to delete student", e);
            return false;
        }
    }

    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT student_id, name, email, phone, department " +
                     "FROM student ORDER BY student_id";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Student(
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("department")));
            }

        } catch (SQLException e) {
            showDatabaseError("Unable to load students", e);
        }
        return list;
    }

    private void showDatabaseError(String message, SQLException e) {
        e.printStackTrace();
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(
                        null,
                        message + "\n\n" + e.getMessage(),
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE));
    }
}

class CourseDAO {
    public boolean addCourse(Course c) {
        String sql = "INSERT INTO course (course_name, credit) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setDouble(2, c.getCredit());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean updateCourse(Course c) {
        String sql = "UPDATE course SET course_name=?, credit=? WHERE course_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, c.getCourseName());
            ps.setDouble(2, c.getCredit());
            ps.setInt(3, c.getCourseId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCourse(int courseId) {
        String sql = "DELETE FROM course WHERE course_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, courseId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT * FROM course ORDER BY course_id";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Course(rs.getInt("course_id"), rs.getString("course_name"), rs.getDouble("credit")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}

class AttendanceDAO {
    public boolean addAttendance(Attendance a) {
        String sql = "INSERT INTO attendance (student_id, course_id, attendance_date, status) VALUES (?, ?, ?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getStudentId());
            ps.setInt(2, a.getCourseId());
            ps.setString(3, a.getDate().toIsoString());
            ps.setString(4, a.getStatus().name());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteAttendance(int attendanceId) {
        String sql = "DELETE FROM attendance WHERE attendance_id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, attendanceId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Attendance> getAllAttendance() {
        List<Attendance> list = new ArrayList<>();
        String sql = "SELECT a.attendance_id, a.student_id, a.course_id, a.attendance_date, a.status, "
                   + "s.name AS student_name, c.course_name "
                   + "FROM attendance a "
                   + "JOIN student s ON a.student_id = s.student_id "
                   + "JOIN course c ON a.course_id = c.course_id "
                   + "ORDER BY a.attendance_date DESC, a.attendance_id DESC";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Attendance a = new Attendance(
                        rs.getInt("attendance_id"), rs.getInt("student_id"), rs.getInt("course_id"),
                        MyDate.fromIsoString(rs.getString("attendance_date")),
                        AttendanceStatus.fromString(rs.getString("status")));
                a.setStudentName(rs.getString("student_name"));
                a.setCourseName(rs.getString("course_name"));
                list.add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}

/* =========================================================
   ENTRY POINT + DASHBOARD
   ========================================================= */
public class AttendanceApp {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        SwingUtilities.invokeLater(() -> new MainFrame().setVisible(true));
    }
}

class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("Attendance Management System");
        setSize(640, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG);

        add(Theme.headerBar("\uD83C\uDF38", "Attendance Management System", "Java Swing  \u2022  JDBC  \u2022  SQLite"), BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBackground(Theme.BG);
        center.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel cardPanel = new JPanel(new GridLayout(1, 3, 18, 0));
        cardPanel.setOpaque(false);
        cardPanel.setPreferredSize(new Dimension(560, 230));

        cardPanel.add(navCard("\uD83C\uDF3A", "Student", "Add, edit & view students", Theme.BLUE,
                () -> new StudentForm().setVisible(true)));
        cardPanel.add(navCard("\uD83C\uDF3B", "Course", "Add, edit & view courses", Theme.TEAL,
                () -> new CourseForm().setVisible(true)));
        cardPanel.add(navCard("\uD83C\uDF3C", "Attendance", "Mark daily attendance", Theme.PINK,
                () -> new AttendanceForm().setVisible(true)));

        center.add(cardPanel);
        add(center, BorderLayout.CENTER);

        JLabel footer = new JLabel("\uD83C\uDF3F  Attendance Management System \u00A9 2026 \u2014 Java Swing & JDBC  \uD83C\uDF3F", SwingConstants.CENTER);
        footer.setFont(new Font("SansSerif", Font.PLAIN, 11));
        footer.setForeground(Theme.MUTED);
        footer.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
        add(footer, BorderLayout.SOUTH);
    }

    private RoundedPanel navCard(String icon, String title, String subtitle, Color color, Runnable onClick) {
        RoundedPanel card = new RoundedPanel(20);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));
        card.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));

        JPanel inner = new JPanel();
        inner.setOpaque(false);
        inner.setLayout(new BoxLayout(inner, BoxLayout.Y_AXIS));

        JLabel iconBadge = new JLabel(icon, SwingConstants.CENTER);
        iconBadge.setFont(new Font("SansSerif", Font.PLAIN, 36));
        iconBadge.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconBadge.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        titleLabel.setForeground(Theme.TEXT);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("<html><div style='text-align:center;width:130px;'>" + subtitle + "</div></html>", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        subtitleLabel.setForeground(Theme.MUTED);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 14, 0));

        JPanel stripe = new JPanel();
        stripe.setBackground(color);
        stripe.setPreferredSize(new Dimension(46, 4));
        stripe.setMaximumSize(new Dimension(46, 4));
        stripe.setAlignmentX(Component.CENTER_ALIGNMENT);

        inner.add(iconBadge);
        inner.add(titleLabel);
        inner.add(subtitleLabel);
        inner.add(stripe);

        card.add(inner, BorderLayout.CENTER);

        card.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { onClick.run(); }
            public void mouseEntered(MouseEvent e) { card.setBackground(new Color(252, 249, 253)); card.repaint(); }
            public void mouseExited(MouseEvent e) { card.setBackground(Color.WHITE); card.repaint(); }
        });
        return card;
    }
}

/* =========================================================
   FORM 1: STUDENT (blue accent)
   ========================================================= */
class StudentForm extends JFrame {
    private final StudentDAO studentDAO = new StudentDAO();
    private JTextField nameField, emailField, phoneField, departmentField;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedStudentId = -1;

    public StudentForm() {
        setTitle("Student Form");
        setSize(840, 580);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG);

        add(Theme.headerBar("\uD83C\uDF3A", "Student Records", "Manage enrolled students"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setBackground(Theme.BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        content.add(buildFormCard(), BorderLayout.NORTH);
        content.add(buildTableCard(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadTable();
    }

    private RoundedPanel buildFormCard() {
        RoundedPanel outer = Theme.card();
        outer.setLayout(new BorderLayout(0, 14));

        JLabel heading = new JLabel("\uD83D\uDCDD  Student Details");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.BLUE);
        outer.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        nameField = Theme.field();
        emailField = Theme.field();
        phoneField = Theme.field();
        departmentField = Theme.field();

        gc.gridx = 0; gc.gridy = 0; form.add(Theme.label("Name"), gc);
        gc.gridx = 1; form.add(nameField, gc);
        gc.gridx = 2; form.add(Theme.label("Email"), gc);
        gc.gridx = 3; form.add(emailField, gc);

        gc.gridx = 0; gc.gridy = 1; form.add(Theme.label("Phone"), gc);
        gc.gridx = 1; form.add(phoneField, gc);
        gc.gridx = 2; form.add(Theme.label("Department"), gc);
        gc.gridx = 3; form.add(departmentField, gc);

        outer.add(form, BorderLayout.CENTER);

        JButton addBtn = Theme.pillButton("\u2795 Add", Theme.GREEN);
        JButton updateBtn = Theme.pillButton("\u270E Update", Theme.YELLOW);
        JButton deleteBtn = Theme.pillButton("\uD83D\uDDD1 Delete", Theme.RED);
        JButton clearBtn = Theme.pillButton("\u21BB Clear", Theme.MUTED);

        addBtn.addActionListener(e -> addStudent());
        updateBtn.addActionListener(e -> updateStudent());
        deleteBtn.addActionListener(e -> deleteStudent());
        clearBtn.addActionListener(e -> clearForm());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(addBtn); btnPanel.add(updateBtn); btnPanel.add(deleteBtn); btnPanel.add(clearBtn);
        outer.add(btnPanel, BorderLayout.SOUTH);

        return outer;
    }

    private RoundedPanel buildTableCard() {
        RoundedPanel outer = Theme.card();
        outer.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Email", "Phone", "Department"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getTableHeader().setBackground(Theme.BLUE);
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Student s : studentDAO.getAllStudents()) {
            tableModel.addRow(new Object[]{s.getStudentId(), s.getName(), s.getEmail(), s.getPhone(), s.getDepartment()});
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        selectedStudentId = ((Number) tableModel.getValueAt(row, 0)).intValue();
        nameField.setText(String.valueOf(tableModel.getValueAt(row, 1)));
        emailField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        phoneField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        departmentField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
    }

    private void addStudent() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        Student s = new Student(
                0,
                name,
                emailField.getText().trim(),
                phoneField.getText().trim(),
                departmentField.getText().trim());

        if (studentDAO.addStudent(s)) {
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Student added successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void updateStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a student from the table first.",
                    "Update Student", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name is required.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            nameField.requestFocus();
            return;
        }

        Student s = new Student(
                selectedStudentId,
                name,
                emailField.getText().trim(),
                phoneField.getText().trim(),
                departmentField.getText().trim());

        if (studentDAO.updateStudent(s)) {
            loadTable();
            clearForm();
            JOptionPane.showMessageDialog(this, "Student updated successfully.",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void deleteStudent() {
        if (selectedStudentId == -1) {
            JOptionPane.showMessageDialog(this,
                    "Select a student from the table first.",
                    "Delete Student", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (studentDAO.deleteStudent(selectedStudentId)) {
                loadTable();
                clearForm();
                JOptionPane.showMessageDialog(this,
                        "Student deleted successfully.",
                        "Success", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedStudentId = -1;
        nameField.setText(""); emailField.setText(""); phoneField.setText(""); departmentField.setText("");
        table.clearSelection();
    }
}

/* =========================================================
   FORM 2: COURSE (teal accent)
   ========================================================= */
class CourseForm extends JFrame {
    private final CourseDAO courseDAO = new CourseDAO();
    private JTextField courseNameField, creditField;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedCourseId = -1;

    public CourseForm() {
        setTitle("Course Form");
        setSize(780, 540);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG);

        add(Theme.headerBar("\uD83C\uDF3B", "Course Records", "Manage available courses"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setBackground(Theme.BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        content.add(buildFormCard(), BorderLayout.NORTH);
        content.add(buildTableCard(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadTable();
    }

    private RoundedPanel buildFormCard() {
        RoundedPanel outer = Theme.card();
        outer.setLayout(new BorderLayout(0, 14));

        JLabel heading = new JLabel("\uD83D\uDCDD  Course Details");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.TEAL);
        outer.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        courseNameField = Theme.field();
        creditField = Theme.field();

        gc.gridx = 0; gc.gridy = 0; form.add(Theme.label("Course Name"), gc);
        gc.gridx = 1; form.add(courseNameField, gc);
        gc.gridx = 2; form.add(Theme.label("Credit"), gc);
        gc.gridx = 3; form.add(creditField, gc);

        outer.add(form, BorderLayout.CENTER);

        JButton addBtn = Theme.pillButton("\u2795 Add", Theme.GREEN);
        JButton updateBtn = Theme.pillButton("\u270E Update", Theme.YELLOW);
        JButton deleteBtn = Theme.pillButton("\uD83D\uDDD1 Delete", Theme.RED);
        JButton clearBtn = Theme.pillButton("\u21BB Clear", Theme.MUTED);

        addBtn.addActionListener(e -> addCourse());
        updateBtn.addActionListener(e -> updateCourse());
        deleteBtn.addActionListener(e -> deleteCourse());
        clearBtn.addActionListener(e -> clearForm());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(addBtn); btnPanel.add(updateBtn); btnPanel.add(deleteBtn); btnPanel.add(clearBtn);
        outer.add(btnPanel, BorderLayout.SOUTH);

        return outer;
    }

    private RoundedPanel buildTableCard() {
        RoundedPanel outer = Theme.card();
        outer.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Course Name", "Credit"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        table.getTableHeader().setBackground(Theme.TEAL);
        table.getSelectionModel().addListSelectionListener(e -> fillFormFromSelectedRow());

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Course c : courseDAO.getAllCourses()) {
            tableModel.addRow(new Object[]{c.getCourseId(), c.getCourseName(), c.getCredit()});
        }
    }

    private void fillFormFromSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        selectedCourseId = (int) tableModel.getValueAt(row, 0);
        courseNameField.setText((String) tableModel.getValueAt(row, 1));
        creditField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
    }

    private void addCourse() {
        if (courseNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Course name is required.");
            return;
        }
        try {
            double credit = Double.parseDouble(creditField.getText().trim());
            Course c = new Course(0, courseNameField.getText().trim(), credit);
            if (courseDAO.addCourse(c)) { loadTable(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "Failed to add course.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit must be a number.");
        }
    }

    private void updateCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Select a course from the table first.");
            return;
        }
        try {
            double credit = Double.parseDouble(creditField.getText().trim());
            Course c = new Course(selectedCourseId, courseNameField.getText().trim(), credit);
            if (courseDAO.updateCourse(c)) { loadTable(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "Failed to update course.");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Credit must be a number.");
        }
    }

    private void deleteCourse() {
        if (selectedCourseId == -1) {
            JOptionPane.showMessageDialog(this, "Select a course from the table first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this course?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (courseDAO.deleteCourse(selectedCourseId)) { loadTable(); clearForm(); }
            else JOptionPane.showMessageDialog(this, "Failed to delete. (Course may have attendance records.)");
        }
    }

    private void clearForm() {
        selectedCourseId = -1;
        courseNameField.setText(""); creditField.setText("");
        table.clearSelection();
    }
}

/* =========================================================
   FORM 3: ATTENDANCE (pink accent, link entity, date picker, radio buttons)
   ========================================================= */
class AttendanceForm extends JFrame {
    private final AttendanceDAO attendanceDAO = new AttendanceDAO();
    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    private JComboBox<Student> studentCombo;
    private JComboBox<Course> courseCombo;
    private JSpinner dateSpinner;
    private JRadioButton presentRadio, absentRadio, lateRadio;
    private JTable table;
    private DefaultTableModel tableModel;
    private int selectedAttendanceId = -1;

    public AttendanceForm() {
        setTitle("Attendance Form");
        setSize(900, 620);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BG);

        add(Theme.headerBar("\uD83C\uDF3C", "Attendance Records", "Link entity: Student \u2194 Course"), BorderLayout.NORTH);

        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setBackground(Theme.BG);
        content.setBorder(BorderFactory.createEmptyBorder(20, 24, 24, 24));
        content.add(buildFormCard(), BorderLayout.NORTH);
        content.add(buildTableCard(), BorderLayout.CENTER);
        add(content, BorderLayout.CENTER);

        loadCombos();
        loadTable();
    }

    private RoundedPanel buildFormCard() {
        RoundedPanel outer = Theme.card();
        outer.setLayout(new BorderLayout(0, 14));

        JLabel heading = new JLabel("\u2705  Mark Attendance");
        heading.setFont(Theme.FONT_HEADING);
        heading.setForeground(Theme.PINK);
        outer.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(6, 6, 6, 6);
        gc.fill = GridBagConstraints.HORIZONTAL;

        studentCombo = new JComboBox<>();
        studentCombo.setFont(Theme.FONT_FIELD);
        courseCombo = new JComboBox<>();
        courseCombo.setFont(Theme.FONT_FIELD);

        SpinnerDateModel spinnerModel = new SpinnerDateModel();
        dateSpinner = new JSpinner(spinnerModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "dd-MM-yyyy"));
        dateSpinner.setValue(new Date());
        dateSpinner.setFont(Theme.FONT_FIELD);

        presentRadio = new JRadioButton("\u2713 Present", true);
        absentRadio = new JRadioButton("\u2717 Absent");
        lateRadio = new JRadioButton("\u23F0 Late");
        presentRadio.setForeground(Theme.GREEN);
        absentRadio.setForeground(Theme.RED);
        lateRadio.setForeground(Theme.ORANGE);
        for (JRadioButton rb : new JRadioButton[]{presentRadio, absentRadio, lateRadio}) {
            rb.setFont(new Font("SansSerif", Font.BOLD, 13));
            rb.setOpaque(false);
            rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(presentRadio); statusGroup.add(absentRadio); statusGroup.add(lateRadio);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0));
        radioPanel.setOpaque(false);
        radioPanel.add(presentRadio); radioPanel.add(absentRadio); radioPanel.add(lateRadio);

        gc.gridx = 0; gc.gridy = 0; form.add(Theme.label("Student"), gc);
        gc.gridx = 1; form.add(studentCombo, gc);
        gc.gridx = 2; form.add(Theme.label("Course"), gc);
        gc.gridx = 3; form.add(courseCombo, gc);

        gc.gridx = 0; gc.gridy = 1; form.add(Theme.label("Date"), gc);
        gc.gridx = 1; form.add(dateSpinner, gc);
        gc.gridx = 2; form.add(Theme.label("Status"), gc);
        gc.gridx = 3; form.add(radioPanel, gc);

        outer.add(form, BorderLayout.CENTER);

        JButton addBtn = Theme.pillButton("\u2795 Add Attendance", Theme.GREEN);
        JButton deleteBtn = Theme.pillButton("\uD83D\uDDD1 Delete Selected", Theme.RED);
        JButton refreshBtn = Theme.pillButton("\u21BB Refresh", Theme.MUTED);

        addBtn.addActionListener(e -> addAttendance());
        deleteBtn.addActionListener(e -> deleteAttendance());
        refreshBtn.addActionListener(e -> { loadCombos(); loadTable(); });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(addBtn); btnPanel.add(deleteBtn); btnPanel.add(refreshBtn);
        outer.add(btnPanel, BorderLayout.SOUTH);

        return outer;
    }

    private RoundedPanel buildTableCard() {
        RoundedPanel outer = Theme.card();
        outer.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"ID", "Student", "Course", "Date", "Status"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        Theme.styleTable(table);
        Theme.colorStatusColumn(table, 4);
        table.getTableHeader().setBackground(Theme.PINK);
        table.getSelectionModel().addListSelectionListener(e -> {
            int row = table.getSelectedRow();
            selectedAttendanceId = row == -1 ? -1 : (int) tableModel.getValueAt(row, 0);
        });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        outer.add(scroll, BorderLayout.CENTER);
        return outer;
    }

    private void loadCombos() {
        studentCombo.removeAllItems();
        for (Student s : studentDAO.getAllStudents()) studentCombo.addItem(s);
        courseCombo.removeAllItems();
        for (Course c : courseDAO.getAllCourses()) courseCombo.addItem(c);
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        for (Attendance a : attendanceDAO.getAllAttendance()) {
            tableModel.addRow(new Object[]{a.getAttendanceId(), a.getStudentName(), a.getCourseName(),
                    a.getDate().toString(), a.getStatus().getLabel()});
        }
    }

    private AttendanceStatus getSelectedStatus() {
        if (presentRadio.isSelected()) return AttendanceStatus.PRESENT;
        if (absentRadio.isSelected()) return AttendanceStatus.ABSENT;
        return AttendanceStatus.LATE;
    }

    private void addAttendance() {
        Student student = (Student) studentCombo.getSelectedItem();
        Course course = (Course) courseCombo.getSelectedItem();
        if (student == null || course == null) {
            JOptionPane.showMessageDialog(this, "Add at least one student and one course first.");
            return;
        }
        Date pickedDate = (Date) dateSpinner.getValue();
        MyDate myDate = MyDate.fromUtilDate(pickedDate);
        Attendance a = new Attendance(0, student.getStudentId(), course.getCourseId(), myDate, getSelectedStatus());
        if (attendanceDAO.addAttendance(a)) loadTable();
        else JOptionPane.showMessageDialog(this, "Failed to add attendance. (Duplicate for same student/course/date?)");
    }

    private void deleteAttendance() {
        if (selectedAttendanceId == -1) {
            JOptionPane.showMessageDialog(this, "Select an attendance row first.");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this record?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            if (attendanceDAO.deleteAttendance(selectedAttendanceId)) loadTable();
            else JOptionPane.showMessageDialog(this, "Failed to delete record.");
        }
    }
}
