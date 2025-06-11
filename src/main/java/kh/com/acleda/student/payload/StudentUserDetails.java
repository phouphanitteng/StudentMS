package kh.com.acleda.student.payload;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class StudentUserDetails implements UserDetails {
    private final StudentReq student;

    public StudentUserDetails(StudentReq student) {
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // You can add roles or authorities if needed.
    }

    @Override
    public String getPassword() {
        return student.getPassword(); // Return the student's password
    }

    @Override
    public String getUsername() {
        return student.getEmail(); // Use email as the username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Or use a field if applicable
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Or use a field if applicable
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Or use a field if applicable
    }

    @Override
    public boolean isEnabled() {
        return "ACTIVE".equals(student.getStatus()); // Check student's status
    }

    public StudentReq getStudent() {
        return student;
    }
}
