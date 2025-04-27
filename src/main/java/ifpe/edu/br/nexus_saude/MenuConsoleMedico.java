package ifpe.edu.br.nexus_saude;

import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Scanner;

@Component
public class MenuConsoleMedico implements CommandLineRunner {

    @Autowired
    private MedicoRepository medicoRepository;

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) {
        int opcao;
        do {
            System.out.println("\n--- MENU MÉDICO ---");
            System.out.println("1 - Cadastrar médico");
            System.out.println("2 - Listar médicos");
            System.out.println("3 - Atualizar médico");
            System.out.println("4 - Deletar médico");
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1 -> cadastrarMedico();
                case 2 -> listarMedicos();
                case 3 -> atualizarMedico();
                case 4 -> deletarMedico();
                case 0 -> System.out.println("Encerrando...");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void cadastrarMedico() {
        Medico medico = new Medico();

        System.out.print("Nome: ");
        medico.setNome(scanner.nextLine());

        System.out.print("Email: ");
        medico.setEmail(scanner.nextLine().toLowerCase());

        System.out.print("Senha: ");
        medico.setSenha(scanner.nextLine());

        System.out.print("CPF: ");
        medico.setCpf(scanner.nextLine());

        System.out.print("CRM: ");
        medico.setCrm(scanner.nextLine());

        System.out.print("Especialidade: ");
        medico.setEspecialidade(scanner.nextLine());

        System.out.print("Sexo (M/F): ");
        medico.setSexo(scanner.nextLine().toUpperCase());

        System.out.print("Telefone do Consultório: ");
        medico.setTelefoneConsultorio(scanner.nextLine());

        System.out.print("Tempo de Consulta (em minutos): ");
        medico.setTempoConsulta(scanner.nextInt());
        scanner.nextLine();

        System.out.print("Tipo: ");
        medico.setTipo(scanner.nextLine());

        System.out.print("UF: ");
        medico.setUf(scanner.nextLine().toUpperCase());

        System.out.print("Valor da Consulta (ex: 150.00): ");
        medico.setValorConsulta(scanner.nextBigDecimal());
        scanner.nextLine();

        System.out.print("Data de Nascimento (AAAA-MM-DD): ");
        medico.setDataNascimento(LocalDate.parse(scanner.nextLine()));

        medico.setDataCadastro(LocalDateTime.now());
        medico.setCreatedAt(LocalDateTime.now());

        medicoRepository.save(medico);
        System.out.println("Médico cadastrado com sucesso!");
    }

    private void listarMedicos() {
        System.out.println("\n--- Lista de Médicos ---");
        medicoRepository.findAll().forEach(m -> {
            System.out.println("ID: " + m.getId());
            System.out.println("Nome: " + m.getNome());
            System.out.println("Email: " + m.getEmail());
            System.out.println("CPF: " + m.getCpf());
            System.out.println("CRM: " + m.getCrm());
            System.out.println("Especialidade: " + m.getEspecialidade());
            System.out.println("Sexo: " + m.getSexo());
            System.out.println("Telefone Consultório: " + m.getTelefoneConsultorio());
            System.out.println("Tempo de Consulta: " + m.getTempoConsulta() + " min");
            System.out.println("Tipo: " + m.getTipo());
            System.out.println("UF: " + m.getUf());
            System.out.println("Valor da Consulta: " + m.getValorConsulta());
            System.out.println("Data de Nascimento: " + m.getDataNascimento());
            System.out.println("Data de Cadastro: " + m.getDataCadastro());
            System.out.println("-----------------------------");
        });
    }

    private void atualizarMedico() {
        System.out.print("ID do médico para atualizar: ");
        Integer id = scanner.nextInt();
        scanner.nextLine();

        Medico medico = medicoRepository.findById(id).orElse(null);

        if (medico != null) {
            System.out.print("Novo nome (" + medico.getNome() + "): ");
            medico.setNome(scanner.nextLine());

            System.out.print("Novo email (" + medico.getEmail() + "): ");
            medico.setEmail(scanner.nextLine().toLowerCase());

            System.out.print("Nova senha: ");
            medico.setSenha(scanner.nextLine());

            System.out.print("Novo CPF (" + medico.getCpf() + "): ");
            medico.setCpf(scanner.nextLine());

            System.out.print("Novo CRM (" + medico.getCrm() + "): ");
            medico.setCrm(scanner.nextLine());

            System.out.print("Nova especialidade (" + medico.getEspecialidade() + "): ");
            medico.setEspecialidade(scanner.nextLine());

            System.out.print("Novo sexo (" + medico.getSexo() + "): ");
            medico.setSexo(scanner.nextLine().toUpperCase());

            System.out.print("Novo telefone consultório (" + medico.getTelefoneConsultorio() + "): ");
            medico.setTelefoneConsultorio(scanner.nextLine());

            System.out.print("Novo tempo de consulta (" + medico.getTempoConsulta() + " min): ");
            medico.setTempoConsulta(scanner.nextInt());
            scanner.nextLine();

            System.out.print("Novo tipo (" + medico.getTipo() + "): ");
            medico.setTipo(scanner.nextLine());

            System.out.print("Nova UF (" + medico.getUf() + "): ");
            medico.setUf(scanner.nextLine().toUpperCase());

            System.out.print("Novo valor da consulta (" + medico.getValorConsulta() + "): ");
            medico.setValorConsulta(scanner.nextBigDecimal());
            scanner.nextLine();

            System.out.print("Nova data de nascimento (" + medico.getDataNascimento() + "): ");
            medico.setDataNascimento(LocalDate.parse(scanner.nextLine()));

            medico.setUpdatedAt(LocalDateTime.now());

            medicoRepository.save(medico);
            System.out.println("Médico atualizado com sucesso!");
        } else {
            System.out.println("Médico não encontrado.");
        }
    }

    private void deletarMedico() {
        System.out.print("ID do médico para deletar: ");
        Integer id = scanner.nextInt();
        scanner.nextLine();

        if (medicoRepository.existsById(id)) {
            medicoRepository.deleteById(id);
            System.out.println("Médico deletado com sucesso!");
        } else {
            System.out.println("Médico não encontrado.");
        }
    }
}
