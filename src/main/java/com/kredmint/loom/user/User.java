package com.kredmint.loom.user;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String username;
    private String email;
    private String password;
    private UserRole role;
    
    @Builder.Default
    private boolean active = true;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
