package dal;

import be.Event;
import be.Role;
import be.User;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import exceptions.ErrorCode;
import exceptions.EventException;
import exceptions.ExceptionLogger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class UsersDAO {
    private ConnectionManager connectionManager;
    private FileHandler fileHandler;
    private Cloudinary cloudinary;

    public UsersDAO() throws EventException {
        this.connectionManager = new ConnectionManager();
        this.fileHandler = new FileHandler();
        this.cloudinary = new CloudinaryApp().getCloudinary();
    }

    public Task<List<User>> getEventUsers(int eventId) {
        return getUsers(eventId);
    }

    private Task<List<User>> getUsers(int eventId) {
        Task<List<User>> retrieve = new Task<List<User>>() {
            @Override
            protected List<User> call() throws Exception {
                List<User> evCoord = new ArrayList<>();
                String sql = "SELECT U.UserId,U.FirstName,U.LastName,U.Role FROM USERS AS U " +
                        "Where U.Role Like ? " +
                        "AND U.UserId NOT IN (SELECT us.UserId FROM Users us join UsersEvents ue ON us.UserId=ue.UserId  join Event e ON e.EventId=ue.EventId WHERE e.EventId=?)";
                try (Connection conn = connectionManager.getConnection()) {
                    try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                        psmt.setString(1, Role.EVENT_COORDINATOR.getValue());
                        psmt.setInt(2, eventId);
                        ResultSet rs = psmt.executeQuery();
                        while (rs.next()) {
                            int userId = rs.getInt(1);
                            String firstName = rs.getString(2);
                            String lastName = rs.getString(3);
                            String role = rs.getString(4);
                            User user = new User(firstName, lastName, role);
                            user.setUserId(userId);
                            evCoord.add(user);
                        }
                    }
                } catch (SQLException | EventException e) {
                    ExceptionLogger.getInstance().getLogger().log(Level.SEVERE, e.getMessage());
                    System.out.println(e.getMessage() + e.getCause() + ErrorCode.OPERATION_DB_FAILED);
                    // throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
                }
                ;
                return evCoord;
            }

            ;
        };
        return retrieve;
    }

    public List<User> getEventCoordinators(int eventId) throws EventException {
        String sql = "SELECT u.UserID,u.FirstName,u.LastName,u.Role FROM UsersEvents ue JOIN Users u  ON u.UserId=ue.UserId WHERE ue.EventId=?";
        List<User> evCoord = new ArrayList<>();
        try (Connection conn = connectionManager.getConnection()) {
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                psmt.setInt(1, eventId);
                ResultSet rs = psmt.executeQuery();
                while (rs.next()) {
                    int userId = rs.getInt(1);
                    String firstName = rs.getString(2);
                    String lastName = rs.getString(3);
                    String role = rs.getString(4);
                    User user = new User(firstName, lastName, role);
                    user.setUserId(userId);
                    evCoord.add(user);
                }
            }
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage(), e, ErrorCode.OPERATION_DB_FAILED);
        }
        return evCoord;
    }

    public boolean unassignUser(int entityId, int eventId) throws EventException {
        boolean succeeded = false;
        String sql = "DELETE FROM UsersEvents WHERE  EventId=? AND UserId=?";
        try (Connection conn = connectionManager.getConnection()) {
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                psmt.setInt(1, eventId);
                psmt.setInt(2, entityId);
                psmt.executeUpdate();
            }
            succeeded = true;
        } catch (SQLException | EventException e) {
            throw new EventException(e.getMessage());
        }
        return succeeded;
    }

    public List<User> getAllEvents(int eventId) throws EventException {
        List<User> coordinators = new ArrayList<>();
        String sql = "SELECT U.UserId,U.FirstName,U.LastName,U.Role FROM USERS AS U " +
                "Where U.Role Like ? " +
                "AND U.UserId NOT IN (SELECT us.UserId FROM Users us join UsersEvents ue ON us.UserId=ue.UserId  join Event e ON e.EventId=ue.EventId WHERE e.EventId=?)";

        try (Connection conn = connectionManager.getConnection()) {
            try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                psmt.setString(1, Role.EVENT_COORDINATOR.getValue());
                psmt.setInt(2, eventId);
                ResultSet rs = psmt.executeQuery();
                while (rs.next()) {
                    int userId = rs.getInt(1);
                    String firstName = rs.getString(2);
                    String lastName = rs.getString(3);
                    String role = rs.getString(4);
                    User user = new User(firstName, lastName, role);
                    user.setUserId(userId);
                    coordinators.add(user);
                }
            }
        } catch (SQLException | EventException e) {
            e.printStackTrace();
            throw new EventException(e.getMessage(), e.getCause(), ErrorCode.OPERATION_DB_FAILED);
        }
        return coordinators;
    }

    public boolean assignCoordinatorsToEvent(ObservableList<Integer> selectedUsers, int eventId) throws EventException {
        final int maxRetries = 1;
        int currentTry = 0;
        boolean succeeded = false;
        String sql = "INSERT INTO UsersEvents VALUES(?,?)";
        while (currentTry <= maxRetries && !succeeded) {
            Connection conn = null;
            try {
                conn = connectionManager.getConnection();
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                try (PreparedStatement psmt = conn.prepareStatement(sql)) {
                    for (Integer userId : selectedUsers) {
                        psmt.setInt(1, userId);
                        psmt.setInt(2, eventId);
                        psmt.addBatch();
                    }
                    psmt.executeBatch();
                    conn.commit();
                    succeeded = true;
                }
            } catch (SQLException | EventException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException exc) {
                        ExceptionLogger.getInstance().getLogger().log(Level.WARNING, "Rollback failed: " + exc.getMessage(), exc);
                    }
                }
                if (currentTry == maxRetries) {
                    throw new EventException(e.getMessage(), e, ErrorCode.OPERATION_DB_FAILED);
                }
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        ExceptionLogger.getInstance().getLogger().log(Level.WARNING, "Failed to close connection: " + e.getMessage(), e);
                    }
                }
            }
            currentTry++;
        }
        return succeeded;
    }

    public ObservableMap<Integer,User> getFullUserInfo() throws EventException {
     ObservableMap<Integer,User> fullUsers= FXCollections.observableHashMap();
     String sql ="SELECT U.* ,E.name AS EventName FROM Users U LEFT JOIN UsersEvents UE ON UE.UserId=U.UserId LEFT JOIN Event E ON UE.EventId=E.EventId";

    try(Connection conn = connectionManager.getConnection()) {
        try(PreparedStatement psmt = conn.prepareStatement(sql) ){
             ResultSet rs= psmt.executeQuery();
             while(rs.next()){
                 int userId= rs.getInt("UserId");
                 String firstName = rs.getString("FirstName");
                 String password = rs.getString("Password");
                 String lastName = rs.getString("LastName");
                 String profilePicture= rs.getString("ProfilePicture");
                 String role = rs.getString("Role");
                 String eventName= rs.getString("EventName");
                 User user = fullUsers.get(userId);
                 if (user == null) {
                     List<String> events = new ArrayList<>();
                     if (eventName != null) {
                         events.add(eventName);
                     }
                     user = new User(firstName, lastName, role, password, profilePicture, events);
                     user.setUserId(userId);
                     fullUsers.put(userId, user);
                 } else if (eventName != null) {
                     user.getUserEvents().add(eventName);
                 }
             }
        }
    } catch (SQLException | EventException e) {
        throw new EventException(e.getMessage(),e,ErrorCode.OPERATION_DB_FAILED);
    }
return fullUsers;
    }




    public User saveUserWithCustomImage(User user, File uploadedFile) throws EventException {
        String sql = "INSERT INTO Users VALUES(?,?,?,?,?)";
            Connection conn = null;
            String filePublicId = null;
            try {
                conn = connectionManager.getConnection();
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                String[] results=uploadImage(uploadedFile);
                filePublicId=results[1];
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, user.getFirstName());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getLastName());
                pstmt.setString(4, results[0]);
                pstmt.setString(5, user.getRole());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                }
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    } else {
                        throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                    }
                }
                conn.commit();
                return user;
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                       ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,ex.getMessage());
                    }
                    distroyFile(filePublicId);
                }
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                      ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,e.getMessage());
                    }
                }
            }

        return null;
    }

    private String[] uploadImage(File file) throws EventException {
        String[] uploadedImage =  new String[2];
        try {
            Map uploadResult = cloudinary.uploader().upload(file, ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            String publicId = (String)uploadResult.get("public_id");
            System.out.println("Uploaded image URL: " + url);
            uploadedImage[0]=url;
            uploadedImage[1]=publicId;
            return  uploadedImage;
        } catch (Exception e) {
           // e.printStackTrace();
            throw  new EventException(e.getMessage(),e,ErrorCode.COPY_FAILED);
        }
    }
    private void distroyFile(String filePublicId) throws EventException {
        try {
            Map result = cloudinary.uploader().destroy(filePublicId, ObjectUtils.emptyMap());
            System.out.println(result);
        } catch (Exception e) {
          //  e.printStackTrace();
            throw new EventException(e.getMessage(),e,ErrorCode.COPY_FAILED);
        }
    }

    public User saveUserWithDefaultImage(User user) throws EventException {
        String sql = "INSERT INTO Users VALUES(?,?,?,?,?)";
        Connection conn = null;
        try {
            conn = connectionManager.getConnection();
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
            PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getLastName());
            pstmt.setString(4, user.getUserImageUrl());
            pstmt.setString(5, user.getRole());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new EventException(ErrorCode.OPERATION_DB_FAILED);
            }
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setUserId(generatedKeys.getInt(1));
                } else {
                    throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                }
            }
            conn.commit();
            return user;
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,ex.getMessage());
                }
            }
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,e.getMessage());
                }
            }
        }
        return null;
    }

    public boolean editUserOperation(User selectedUserToEdit, File uploadedImage)  {
        public User saveUserWithDefaultImage(User user) throws EventException {
            String sql = "UPDATE Users VALUES(?,?,?,?,?)";
            Connection conn = null;
            try {
                conn = connectionManager.getConnection();
                conn.setAutoCommit(false);
                conn.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, user.getFirstName());
                pstmt.setString(2, user.getPassword());
                pstmt.setString(3, user.getLastName());
                pstmt.setString(4, user.getUserImageUrl());
                pstmt.setString(5, user.getRole());
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows == 0) {
                    throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                }
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        user.setUserId(generatedKeys.getInt(1));
                    } else {
                        throw new EventException(ErrorCode.OPERATION_DB_FAILED);
                    }
                }
                conn.commit();
                return user;
            } catch (SQLException e) {
                if (conn != null) {
                    try {
                        conn.rollback();
                    } catch (SQLException ex) {
                        ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,ex.getMessage());
                    }
                }
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        ExceptionLogger.getInstance().getLogger().log(Level.SEVERE,e.getMessage());
                    }
                }
            }
            return null;
        }
    }
}
