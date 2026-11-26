package com.assettrack.security;
import com.assettrack.repository.UserRepository;import org.springframework.security.core.userdetails.*;import org.springframework.stereotype.Service;import org.springframework.security.core.authority.SimpleGrantedAuthority;import java.util.List;
@Service
public class CustomUserDetailsService implements UserDetailsService {
 private final UserRepository users; public CustomUserDetailsService(UserRepository u){users=u;}
 public UserDetails loadUserByUsername(String email)throws UsernameNotFoundException{
  var u=users.findByEmail(email).orElseThrow(()->new UsernameNotFoundException("User not found"));
  return new org.springframework.security.core.userdetails.User(u.getEmail(),u.getPassword(),u.isActive(),true,true,true,List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().getName())));
 }
}
