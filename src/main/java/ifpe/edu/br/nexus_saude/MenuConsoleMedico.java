package ifpe.edu.br.nexus_saude;

import ifpe.edu.br.nexus_saude.model.Medico;
import ifpe.edu.br.nexus_saude.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
            System.out.println("0 - Sair");
            System.out.print("Escolha uma opção: ");
            opcao = scanner.nextInt();
            scanner.nextLine(); // consumir quebra de linha

            switch (opcao) {
                case 1 -> cadastrarMedico();
                case 2 -> listarMedicos();
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
        medico.setEmail(scanner.nextLine());

        System.out.print("Senha: ");
        medico.setSenha(scanner.nextLine());

        System.out.print("CRM: ");
        medico.setCrm(scanner.nextLine());

        System.out.print("Especialidade: ");
        medico.setEspecialidade(scanner.nextLine());

        medicoRepository.save(medico);
        System.out.println("Médico cadastrado com sucesso!");
    }

    private void listarMedicos() {
        System.out.println("\n--- Lista de Médicos ---");
        medicoRepository.findAll().forEach(m -> {
            System.out.println("ID: " + m.getId());
            System.out.println("Nome: " + m.getNome());
            System.out.println("Email: " + m.getEmail());
            System.out.println("CRM: " + m.getCrm());
            System.out.println("Especialidade: " + m.getEspecialidade());
            System.out.println("-----------------------------");
        });
    }
}
