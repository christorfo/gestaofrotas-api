package com.frotahucp.gestaofrotas.config;

import com.frotahucp.gestaofrotas.model.*;
import com.frotahucp.gestaofrotas.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

        @Autowired
        private AdministradorRepository administradorRepository;
        @Autowired
        private MotoristaRepository motoristaRepository;
        @Autowired
        private VeiculoRepository veiculoRepository;
        @Autowired
        private AgendamentoRepository agendamentoRepository;
        @Autowired
        private AbastecimentoRepository abastecimentoRepository;
        @Autowired
        private ManutencaoRepository manutencaoRepository;
        @Autowired
        private HistoricoStatusAgendamentoRepository historicoRepository;
        @Autowired
        private PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public void run(String... args) throws Exception {
                if (administradorRepository.count() > 0) {
                        logger.info("Banco de dados já populado.");
                        return;
                }

                logger.info("Populando banco de dados com dados iniciais para teste...");

                // --- 3 Administradores ---
                Administrador admin1 = new Administrador();
                admin1.setNome("Lucas Administrador");
                admin1.setEmail("admin@frotahucp.com");
                admin1.setSenha(passwordEncoder.encode("admin123"));
                Administrador admin2 = new Administrador();
                admin2.setNome("Amanda Supervisora");
                admin2.setEmail("amanda@frotahucp.com");
                admin2.setSenha(passwordEncoder.encode("amanda123"));
                Administrador admin3 = new Administrador();
                admin3.setNome("Jorge Gerente");
                admin3.setEmail("jorge@frotahucp.com");
                admin3.setSenha(passwordEncoder.encode("jorge123"));
                administradorRepository.saveAll(List.of(admin1, admin2, admin3));
                logger.info("3 administradores criados.");

                // --- 5 Motoristas ---
                Motorista mot1 = new Motorista();
                mot1.setNomeCompleto("Paulo da Silva");
                mot1.setEmail("paulo.silva@email.com");
                mot1.setSenha(passwordEncoder.encode("senha123"));
                mot1.setCpf("111.111.111-11");
                mot1.setCnhNumero("11111111111");
                mot1.setCnhValidade(LocalDate.now().plusYears(2));
                mot1.setTelefone("(41) 91111-1111");
                Motorista mot2 = new Motorista();
                mot2.setNomeCompleto("Carla Souza");
                mot2.setEmail("carla.souza@email.com");
                mot2.setSenha(passwordEncoder.encode("senha123"));
                mot2.setCpf("222.222.222-22");
                mot2.setCnhNumero("22222222222");
                mot2.setCnhValidade(LocalDate.now().plusYears(3));
                mot2.setTelefone("(41) 92222-2222");
                Motorista mot3 = new Motorista();
                mot3.setNomeCompleto("Roberta Lima");
                mot3.setEmail("roberta.lima@email.com");
                mot3.setSenha(passwordEncoder.encode("senha123"));
                mot3.setCpf("333.333.333-33");
                mot3.setCnhNumero("33333333333");
                mot3.setCnhValidade(LocalDate.now().plusYears(1));
                mot3.setTelefone("(41) 93333-3333");
                Motorista mot4 = new Motorista();
                mot4.setNomeCompleto("Diego Alves");
                mot4.setEmail("diego.alves@email.com");
                mot4.setSenha(passwordEncoder.encode("senha123"));
                mot4.setCpf("444.444.444-44");
                mot4.setCnhNumero("44444444444");
                mot4.setCnhValidade(LocalDate.now().plusYears(4));
                mot4.setTelefone("(41) 94444-4444");
                Motorista mot5 = new Motorista();
                mot5.setNomeCompleto("Thiago Mendes");
                mot5.setEmail("thiago.mendes@email.com");
                mot5.setSenha(passwordEncoder.encode("senha123"));
                mot5.setCpf("555.555.555-55");
                mot5.setCnhNumero("55555555555");
                mot5.setCnhValidade(LocalDate.now().plusYears(2));
                mot5.setTelefone("(41) 95555-5555");
                List<Motorista> motoristas = motoristaRepository.saveAll(List.of(mot1, mot2, mot3, mot4, mot5));
                logger.info("5 motoristas criados.");

                // --- 6 Veículos ---
                Veiculo v1 = new Veiculo();
                v1.setPlaca("ABC-1A11");
                v1.setModelo("Furgão A");
                v1.setTipo("Furgão");
                v1.setAno(2022);
                v1.setQuilometragemAtual(15000);
                v1.setStatus(StatusVeiculo.DISPONIVEL);
                Veiculo v2 = new Veiculo();
                v2.setPlaca("DEF-2B22");
                v2.setModelo("Caminhonete B");
                v2.setTipo("Caminhonete");
                v2.setAno(2021);
                v2.setQuilometragemAtual(45000);
                v2.setStatus(StatusVeiculo.DISPONIVEL);
                Veiculo v3 = new Veiculo();
                v3.setPlaca("GHI-3C33");
                v3.setModelo("Van C");
                v3.setTipo("Van");
                v3.setAno(2023);
                v3.setQuilometragemAtual(5000);
                v3.setStatus(StatusVeiculo.DISPONIVEL);
                Veiculo v4 = new Veiculo();
                v4.setPlaca("JKL-4D44");
                v4.setModelo("Carro Sedan");
                v4.setTipo("Carro");
                v4.setAno(2022);
                v4.setQuilometragemAtual(32000);
                v4.setStatus(StatusVeiculo.EM_MANUTENCAO);
                Veiculo v5 = new Veiculo();
                v5.setPlaca("MNO-5E55");
                v5.setModelo("Carro Hatch");
                v5.setTipo("Carro");
                v5.setAno(2020);
                v5.setQuilometragemAtual(80000);
                v5.setStatus(StatusVeiculo.INATIVO);
                Veiculo v6 = new Veiculo();
                v6.setPlaca("PQR-6F66");
                v6.setModelo("Furgão D");
                v6.setTipo("Furgão");
                v6.setAno(2023);
                v6.setQuilometragemAtual(2000);
                v6.setStatus(StatusVeiculo.DISPONIVEL);
                List<Veiculo> veiculos = veiculoRepository.saveAll(List.of(v1, v2, v3, v4, v5, v6));
                logger.info("6 veículos criados.");

                // --- 10 Agendamentos ---
                criarAgendamento(motoristas.get(0), veiculos.get(0), LocalDateTime.now().plusDays(1),
                                "Centro de Convenções", StatusAgendamento.AGENDADO);
                criarAgendamento(motoristas.get(1), veiculos.get(1), LocalDateTime.now().plusDays(2),
                                "Aeroporto Afonso Pena", StatusAgendamento.AGENDADO);
                criarAgendamento(motoristas.get(2), veiculos.get(2), LocalDateTime.now().plusDays(3),
                                "Porto de Paranaguá", StatusAgendamento.AGENDADO);
                criarAgendamentoFinalizado(motoristas.get(3), veiculos.get(5), LocalDateTime.now().minusDays(1),
                                "Cliente A", 2000, 2150);
                criarAgendamentoFinalizado(motoristas.get(4), veiculos.get(0), LocalDateTime.now().minusDays(2),
                                "Cliente B", 15000, 15200);
                criarAgendamentoEmUso(motoristas.get(0), veiculos.get(2), LocalDateTime.now().minusHours(3),
                                "Fornecedor Z", 5250);
                criarAgendamento(motoristas.get(1), veiculos.get(5), LocalDateTime.now().plusDays(5), "Cliente C",
                                StatusAgendamento.AGENDADO);
                criarAgendamento(motoristas.get(2), veiculos.get(0), LocalDateTime.now().plusDays(6), "Cliente D",
                                StatusAgendamento.AGENDADO);
                criarAgendamentoFinalizado(motoristas.get(3), veiculos.get(1), LocalDateTime.now().minusDays(3),
                                "Cliente E", 45200, 45400);
                criarAgendamento(motoristas.get(4), veiculos.get(2), LocalDateTime.now().plusDays(4), "Sede da Empresa",
                                StatusAgendamento.CANCELADO);
                logger.info("10 agendamentos criados.");

                // --- 3 Registros de Manutenção ---
                Manutencao m1 = new Manutencao();
                m1.setVeiculo(veiculos.get(3));
                m1.setData(LocalDate.now().minusDays(5));
                m1.setTipo(TipoManutencao.CORRETIVA);
                m1.setDescricao("Troca da correia dentada.");
                m1.setValor(new BigDecimal("1200.00"));
                m1.setQuilometragem(31900);
                manutencaoRepository.save(m1);
                logger.info("Registros de manutenção criados.");

                // --- 5 Registros de Abastecimento ---
                Abastecimento ab1 = new Abastecimento();
                ab1.setVeiculo(veiculos.get(0));
                ab1.setData(LocalDate.now().minusDays(2));
                ab1.setTipoCombustivel("Diesel");
                ab1.setValor(new BigDecimal("250.00"));
                ab1.setQuilometragem(15200);
                ab1.setMotoristaResponsavel("Paulo da Silva");
                abastecimentoRepository.save(ab1);
                logger.info("Registros de abastecimento criados.");

                logger.info("População inicial do banco de dados concluída.");
        }

        // Métodos auxiliares para criar os dados de forma mais limpa
        private void criarAgendamento(Motorista motorista, Veiculo veiculo, LocalDateTime saida, String destino,
                        StatusAgendamento status) {
                Agendamento ag = new Agendamento();
                ag.setMotorista(motorista);
                ag.setVeiculo(veiculo);
                ag.setDataHoraSaida(saida);
                ag.setDestino(destino);
                ag.setStatus(status);
                agendamentoRepository.save(ag);
                registrarHistorico(ag, null, status, "SISTEMA");
        }

        private void criarAgendamentoFinalizado(Motorista motorista, Veiculo veiculo, LocalDateTime saida,
                        String destino, int kmSaida, int kmFinal) {
                Agendamento ag = new Agendamento();
                ag.setMotorista(motorista);
                ag.setVeiculo(veiculo);
                ag.setDataHoraSaida(saida);
                ag.setDestino(destino);
                ag.setStatus(StatusAgendamento.FINALIZADO);
                ag.setDataHoraInicioViagem(saida.plusMinutes(5));
                ag.setQuilometragemSaida(kmSaida);
                ag.setDataHoraRetorno(saida.plusHours(3));
                ag.setQuilometragemFinal(kmFinal);
                agendamentoRepository.save(ag);
                registrarHistorico(ag, null, StatusAgendamento.AGENDADO, "SISTEMA");
                registrarHistorico(ag, StatusAgendamento.AGENDADO, StatusAgendamento.EM_USO, motorista.getEmail());
                registrarHistorico(ag, StatusAgendamento.EM_USO, StatusAgendamento.FINALIZADO, motorista.getEmail());
        }

        private void criarAgendamentoEmUso(Motorista motorista, Veiculo veiculo, LocalDateTime saida, String destino,
                        int kmSaida) {
                Agendamento ag = new Agendamento();
                ag.setMotorista(motorista);
                ag.setVeiculo(veiculo);
                ag.setDataHoraSaida(saida);
                ag.setDestino(destino);
                ag.setStatus(StatusAgendamento.EM_USO);
                ag.setDataHoraInicioViagem(saida.plusMinutes(5));
                ag.setQuilometragemSaida(kmSaida);
                agendamentoRepository.save(ag);
                registrarHistorico(ag, null, StatusAgendamento.AGENDADO, "SISTEMA");
                registrarHistorico(ag, StatusAgendamento.AGENDADO, StatusAgendamento.EM_USO, motorista.getEmail());
        }

        private void registrarHistorico(Agendamento agendamento, StatusAgendamento statusAnterior,
                        StatusAgendamento statusNovo, String usuarioEmail) {
                HistoricoStatusAgendamento historico = new HistoricoStatusAgendamento(agendamento, statusAnterior,
                                statusNovo, usuarioEmail);
                historicoRepository.save(historico);
        }
}