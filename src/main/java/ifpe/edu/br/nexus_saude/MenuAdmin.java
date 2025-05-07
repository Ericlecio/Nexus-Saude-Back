// package ifpe.edu.br.nexus_saude;

// import ifpe.edu.br.nexus_saude.model.Administrador;
// import ifpe.edu.br.nexus_saude.repository.AdministradorRepository;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.CommandLineRunner;
// import org.springframework.stereotype.Component;
// import org.springframework.security.crypto.password.PasswordEncoder;

// import java.util.Scanner;

// @Component
// public class MenuAdmin implements CommandLineRunner {

//     @Autowired
//     private AdministradorRepository administradorRepository;

//     @Autowired
//     private PasswordEncoder passwordEncoder;

//     private final Scanner scanner = new Scanner(System.in);

//     @Override
//     public void run(String... args) {
//         int opcao;
//         do {
//             System.out.println("\n--- MENU ADMINISTRADOR ---");
//             System.out.println("1 - Cadastrar administrador");
//             System.out.println("2 - Listar administradores");
//             System.out.println("3 - Atualizar administrador");
//             System.out.println("4 - Deletar administrador");
//             System.out.println("0 - Sair");
//             System.out.print("Escolha uma opção: ");
            
//             opcao = scanner.nextInt();
//             scanner.nextLine(); // Consumir quebra de linha

//             switch (opcao) {
//                 case 1 -> cadastrarAdministrador();
//                 case 2 -> listarAdministradores();
//                 case 3 -> atualizarAdministrador();
//                 case 4 -> deletarAdministrador();
//                 case 0 -> System.out.println("Encerrando...");
//                 default -> System.out.println("Opção inválida.");
//             }
//         } while (opcao != 0);
//     }

//     private void cadastrarAdministrador() {
//         Administrador admin = new Administrador();

//         System.out.print("Email: ");
//         admin.setEmail(scanner.nextLine());

//         System.out.print("Senha: ");
//         String senha = scanner.nextLine();
//         admin.setSenha(passwordEncoder.encode(senha));

//         administradorRepository.save(admin);
//         System.out.println("Administrador cadastrado com sucesso!");
//     }

//     private void listarAdministradores() {
//         System.out.println("\n--- Lista de Administradores ---");
//         administradorRepository.findAll().forEach(admin -> {
//             System.out.println("ID: " + admin.getId());
//             System.out.println("Email: " + admin.getEmail());
//             System.out.println("-----------------------------");
//         });
//     }

//     private void atualizarAdministrador() {
//         System.out.print("Informe o ID do administrador que deseja atualizar: ");
//         Integer id = scanner.nextInt();
//         scanner.nextLine(); // Consumir quebra de linha

//         Administrador admin = administradorRepository.findById(id).orElse(null);

//         if (admin != null) {
//             System.out.print("Novo Email: ");
//             admin.setEmail(scanner.nextLine());

//             System.out.print("Nova Senha: ");
//             String novaSenha = scanner.nextLine();
//             admin.setSenha(passwordEncoder.encode(novaSenha));

//             administradorRepository.save(admin);
//             System.out.println("Administrador atualizado com sucesso!");
//         } else {
//             System.out.println("Administrador não encontrado.");
//         }
//     }

//     private void deletarAdministrador() {
//         System.out.print("Informe o ID do administrador que deseja deletar: ");
//         Integer id = scanner.nextInt();
//         scanner.nextLine(); // Consumir quebra de linha

//         if (administradorRepository.existsById(id)) {
//             administradorRepository.deleteById(id);
//             System.out.println("Administrador deletado com sucesso!");
//         } else {
//             System.out.println("Administrador não encontrado.");
//         }
//     }
// }