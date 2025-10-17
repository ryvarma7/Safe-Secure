package com.example.securefiletransfer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

public class AdminPanel extends JPanel {
    private final JTable requestsTable;
    private final DefaultTableModel requestsTableModel;
    private final JTable filesTable;
    private final DefaultTableModel filesTableModel;
    private final JLabel totalFilesLabel, pendingRequestsLabel, approvedRequestsLabel;

    public AdminPanel() {
        setLayout(new BorderLayout(10, 15));
        setBackground(UITheme.BACKGROUND_COLOR);
        setBorder(UITheme.PADDED_BORDER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Administrator Dashboard");
        welcomeLabel.setFont(UITheme.TITLE_FONT);
        welcomeLabel.setForeground(UITheme.HEADER_COLOR);
        JButton logoutButton = new JButton("Logout");
        UITheme.styleButton(logoutButton, UITheme.REJECT_COLOR, UITheme.ICON_LOGOUT);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        cardsPanel.setOpaque(false);
        totalFilesLabel = new JLabel("0");
        pendingRequestsLabel = new JLabel("0");
        approvedRequestsLabel = new JLabel("0");
        cardsPanel.add(UITheme.createDashboardCard("Total Files", totalFilesLabel, new Color(23, 162, 184)));
        cardsPanel.add(UITheme.createDashboardCard("Pending Requests", pendingRequestsLabel, new Color(255, 193, 7)));
        cardsPanel.add(UITheme.createDashboardCard("Approved Today", approvedRequestsLabel, new Color(40, 167, 69)));
        
        JPanel requestsPanel = new JPanel(new BorderLayout(10, 10));
        requestsPanel.setBackground(UITheme.PANEL_BACKGROUND_COLOR);
        requestsPanel.setBorder(UITheme.PADDED_BORDER);
        
        JLabel tableTitle = new JLabel("Manage File Access Requests");
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tableTitle.setForeground(UITheme.FONT_COLOR);
        tableTitle.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        requestsPanel.add(tableTitle, BorderLayout.NORTH);

        requestsTableModel = new DefaultTableModel(new String[]{"Req ID", "Username", "Filename", "Status"}, 0){
             @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        requestsTable = new JTable(requestsTableModel);
        JScrollPane requestsScrollPane = new JScrollPane(requestsTable);
        UITheme.styleTable(requestsTable, requestsScrollPane);
        requestsTable.getColumn("Status").setCellRenderer(new StatusCellRenderer());
        requestsPanel.add(requestsScrollPane, BorderLayout.CENTER);

        JPanel requestButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        requestButtons.setBackground(UITheme.PANEL_BACKGROUND_COLOR);
        JButton approveButton = new JButton("Approve");
        JButton rejectButton = new JButton("Reject");
        JButton viewUserDetailsButton = new JButton("User Details");
        UITheme.styleButton(approveButton, UITheme.APPROVE_COLOR, UITheme.ICON_APPROVE);
        UITheme.styleButton(rejectButton, UITheme.REJECT_COLOR, UITheme.ICON_REJECT);
        UITheme.styleButton(viewUserDetailsButton, UITheme.VIEW_COLOR, UITheme.ICON_DETAILS);
        requestButtons.add(approveButton);
        requestButtons.add(rejectButton);
        requestButtons.add(viewUserDetailsButton);
        requestsPanel.add(requestButtons, BorderLayout.SOUTH);

        // Create Files Panel
        JPanel filesPanel = new JPanel(new BorderLayout(10, 10));
        filesPanel.setBackground(UITheme.PANEL_BACKGROUND_COLOR);
        filesPanel.setBorder(UITheme.PADDED_BORDER);
        
        JLabel filesTitle = new JLabel("Uploaded Files");
        filesTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        filesTitle.setForeground(UITheme.FONT_COLOR);
        filesTitle.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        filesPanel.add(filesTitle, BorderLayout.NORTH);

        filesTableModel = new DefaultTableModel(new String[]{"File ID", "Filename", "Uploaded By", "Upload Date"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        filesTable = new JTable(filesTableModel);
        JScrollPane filesScrollPane = new JScrollPane(filesTable);
        UITheme.styleTable(filesTable, filesScrollPane);
        filesPanel.add(filesScrollPane, BorderLayout.CENTER);

        // Add buttons for file management
        JPanel filesButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        filesButtonPanel.setBackground(UITheme.PANEL_BACKGROUND_COLOR);
        
        JButton viewFileButton = new JButton("View File");
        JButton deleteFileButton = new JButton("Delete File");
        
        UITheme.styleButton(viewFileButton, UITheme.VIEW_COLOR, UITheme.ICON_VIEW);
        UITheme.styleButton(deleteFileButton, UITheme.REJECT_COLOR, UITheme.ICON_REJECT);
        
        JButton fileUploadButton = new JButton("Upload File");
        UITheme.styleButton(fileUploadButton, UITheme.PRIMARY_COLOR, UITheme.ICON_APPROVE);
        
        filesButtonPanel.add(fileUploadButton);
        filesButtonPanel.add(viewFileButton);
        filesButtonPanel.add(deleteFileButton);
        filesPanel.add(filesButtonPanel, BorderLayout.SOUTH);
        
        fileUploadButton.addActionListener(e -> handleUpload());
        viewFileButton.addActionListener(e -> handleViewFile());
        deleteFileButton.addActionListener(e -> handleDeleteFile());

        // Create split content panel
        JPanel contentPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        contentPanel.setOpaque(false);
        contentPanel.add(requestsPanel);
        contentPanel.add(filesPanel);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 15));
        centerPanel.setOpaque(false);
        centerPanel.add(cardsPanel, BorderLayout.NORTH);
        centerPanel.add(contentPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        approveButton.addActionListener(e -> handleApprove());
        rejectButton.addActionListener(e -> handleReject());
        viewUserDetailsButton.addActionListener(e -> handleViewUserDetails());
        logoutButton.addActionListener(e -> SecureFileTransfer.logoutUser());

        loadDashboardData();
        loadRequests();
        loadFiles();
    }
    
    private void handleViewUserDetails() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a request.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }
        String username = (String) requestsTableModel.getValueAt(selectedRow, 1);
        String sql = "SELECT full_name, email, mobile, address FROM users WHERE username = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String details = "<html><b>Full Name:</b> " + rs.getString("full_name") + "<br>" +
                                     "<b>Email:</b> " + rs.getString("email") + "<br>" +
                                     "<b>Mobile:</b> " + rs.getString("mobile") + "<br>" +
                                     "<b>Address:</b> " + rs.getString("address") + "</html>";
                    JOptionPane.showMessageDialog(this, new JLabel(details), "Details for " + username, JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleApprove() {
    int selectedRow = requestsTable.getSelectedRow();
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this,
                "Please select a request to approve",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
        return;
    }

    int requestId = (int) requestsTableModel.getValueAt(selectedRow, 0);
    String key = UUID.randomUUID().toString().substring(0, 8);

    // Updated SQL query to set expiry_time to NULL for permanent access
    String sql = "UPDATE requests SET status = 'approved', request_key = ?, expiry_time = NULL WHERE id = ?";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement statement = conn.prepareStatement(sql)) {

        statement.setString(1, key);
        statement.setInt(2, requestId);
        statement.executeUpdate();

        // Simplified confirmation message without expiry information
        JOptionPane.showMessageDialog(this,
                "Request approved.\nAccess key: " + key,
                "Key Generated",
                JOptionPane.INFORMATION_MESSAGE);

        loadRequests();
        loadDashboardData();
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this,
                "Error approving request: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

    private void handleReject() {
        int selectedRow = requestsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a request to reject",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        int requestId = (int) requestsTableModel.getValueAt(selectedRow, 0);
        String sql = "UPDATE requests SET status = 'rejected' WHERE id = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, requestId);
            statement.executeUpdate();
            loadRequests();
            loadDashboardData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error rejecting request: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDeleteFile() {
        int selectedRow = filesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a file to delete",
                "No Selection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int fileId = (int) filesTableModel.getValueAt(selectedRow, 0);
        String fileName = (String) filesTableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to delete the file '" + fileName + "'?\n" +
            "This will also remove all associated access requests.",
            "Confirm Delete",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DatabaseManager.getConnection()) {
            // First delete associated requests
            String deleteRequestsSql = "DELETE FROM requests WHERE file_id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteRequestsSql)) {
                stmt.setInt(1, fileId);
                stmt.executeUpdate();
            }

            // Then delete the file
            String deleteFileSql = "DELETE FROM files WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(deleteFileSql)) {
                stmt.setInt(1, fileId);
                stmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this,
                "File deleted successfully",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);

            loadFiles();
            loadRequests();
            loadDashboardData();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Error deleting file: " + ex.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void loadDashboardData() {
        String sql = "SELECT (SELECT COUNT(*) FROM files) as total_files, " +
                     "(SELECT COUNT(*) FROM requests WHERE status = 'pending') as pending_requests, " +
                     "(SELECT COUNT(*) FROM requests WHERE status = 'approved' AND DATE(request_date) = CURDATE()) as approved_today";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                totalFilesLabel.setText(String.valueOf(rs.getInt("total_files")));
                pendingRequestsLabel.setText(String.valueOf(rs.getInt("pending_requests")));
                approvedRequestsLabel.setText(String.valueOf(rs.getInt("approved_today")));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading dashboard data: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadRequests() {
        requestsTableModel.setRowCount(0); 
        String sql = "SELECT r.id, u.username, f.filename, r.status FROM requests r " +
                     "JOIN users u ON r.user_id = u.id " +
                     "JOIN files f ON r.file_id = f.id ORDER BY r.request_date DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("filename"),
                    rs.getString("status")
                };
                requestsTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading requests: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpload() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select File to Upload");
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Allowed Files (*.txt, *.pdf, *.doc, *.docx, *.png, *.jpg, *.jpeg, *.gif)", 
            "txt", "pdf", "doc", "docx", "png", "jpg", "jpeg", "gif"
        );
        fileChooser.setFileFilter(filter);

        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String sql = "INSERT INTO files (filename, file_data, upload_date, uploaded_by) VALUES (?, ?, CURRENT_TIMESTAMP, ?)";
            
            try (Connection conn = DatabaseManager.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql);
                 FileInputStream fis = new FileInputStream(selectedFile)) {
                
                stmt.setString(1, selectedFile.getName());
                stmt.setBinaryStream(2, fis, selectedFile.length());
                stmt.setInt(3, SecureFileTransfer.getUserId()); // Use user ID instead of username
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(this, "File uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                loadDashboardData(); // Refresh the dashboard counts
                loadFiles(); // Refresh the files table
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Error uploading file: " + ex.getMessage(), 
                    "Upload Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void loadFiles() {
        filesTableModel.setRowCount(0);
        String sql = "SELECT f.id, f.filename, u.username as uploader, f.upload_date " +
                    "FROM files f JOIN users u ON f.uploaded_by = u.id " +
                    "ORDER BY f.upload_date DESC";
        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Object[] row = new Object[]{
                    rs.getInt("id"),
                    rs.getString("filename"),
                    rs.getString("uploader"),
                    rs.getTimestamp("upload_date").toString()
                };
                filesTableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error loading files: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleViewFile() {
        int selectedRow = filesTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Please select a file to view.", 
                "No File Selected", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        int fileId = (int) filesTableModel.getValueAt(selectedRow, 0);
        FileHandler.viewFileInDialog(this, fileId);
    }
}