package com.frotahucp.gestaofrotas.service;

import com.frotahucp.gestaofrotas.model.Motorista;
import com.frotahucp.gestaofrotas.repository.MotoristaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service("userDetailsService") // Nome do bean é importante para o Spring Security encontrar
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Motorista motorista = motoristaRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com o e-mail: " + email));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // Atribuir o papel (role) ao motorista.
        // Futuramente, isso pode vir de um campo na entidade Motorista ou de uma tabela de papéis.
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_MOTORISTA"));
        // Se o motorista também pudesse ser administrador, você adicionaria "ROLE_ADMINISTRADOR" aqui.

        // TODO: Quando tivermos Administradores, precisaremos de uma lógica para carregá-los também.
        // Isso pode envolver verificar outra tabela ou ter uma entidade 'Usuario' genérica.

        return new User(motorista.getEmail(), motorista.getSenha(),
                        motorista.getStatus().equals(com.frotahucp.gestaofrotas.model.StatusUsuario.ATIVO), // enabled
                        true, // accountNonExpired
                        true, // credentialsNonExpired
                        true, // accountNonLocked
                        grantedAuthorities);
    }
}